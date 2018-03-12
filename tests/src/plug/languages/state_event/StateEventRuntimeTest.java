package plug.languages.state_event;

import java.io.PrintWriter;
import java.nio.file.Paths;
import org.junit.Assert;
import org.junit.Test;
import plug.core.ILanguageModule;
import plug.core.ILanguageRuntime;
import plug.core.IRuntimeView;
import plug.core.RuntimeDescription;
import plug.events.PropertyEvent;
import plug.explorer.AbstractExplorer;
import plug.explorer.BFSExplorer;
import plug.explorer.buchi.nested_dfs.BA_GaiserSchwoon_Iterative;
import plug.language.buchikripke.runtime.KripkeBuchiProductSemantics;
import plug.language.state_event.StateEventModule;
import plug.language.state_event.diagnosis.AtomEvaluator;
import plug.language.state_event.diagnosis.Predicate;
import plug.language.state_event.runtime.StateEventConfiguration;
import plug.language.state_event.runtime.StateEventRuntime;
import plug.statespace.SimpleStateSpaceManager;
import plug.verifiers.deadlock.DeadlockVerifier;
import plug.verifiers.deadlock.FinalStateDetected;
import plug.verifiers.predicates.PredicateVerifier;
import plug.verifiers.predicates.PredicateViolationEvent;
import plug.visualisation.StateSpace2TGF;
import properties.BuchiAutomata.BuchiAutomataModel.BuchiDeclaration;
import properties.LTL.parser.Parser;
import properties.LTL.transformations.LTL2Buchi;
import properties.PropositionalLogic.PropositionalLogicModel.DeclarationBlock;
import properties.PropositionalLogic.PropositionalLogicModel.Expression;
import properties.PropositionalLogic.interpreter.Evaluator;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;

/**
 * @author Ciprian Teodorov (ciprian.teodorov@ensta-bretagne.fr)
 *         Created on 10/03/16.
 */
public class StateEventRuntimeTest {

    ILanguageModule module = new StateEventModule();

    ILanguageRuntime load(String fileName) {
        return module.getLoader().getRuntime(fileName);
    }

    @Test
    public void testDifferentRuntimeViews() {
        IRuntimeView view1 = module.getRuntimeView(new StateEventRuntime());
        IRuntimeView view2 = module.getRuntimeView(new StateEventRuntime());
        assertNotSame(view1, view2);
    }

    @Test
    public void testLoad() {
        StateEventRuntime runtime = (StateEventRuntime) load("tests/resources/stateSpaceExample.sek");
        assertThat(runtime.program, is(notNullValue()));
        assertThat(runtime.program.variables, is(notNullValue()));
        assertThat(runtime.program.variables.size(), is(2));
        assertThat(runtime.program.variables.get("v1"), is(1));
        assertThat(runtime.program.variables.get("v2"), is(2));
        assertThat(runtime.program.initialConfiguration, is(0));
        assertThat(runtime.program.configurations.length, is(3));
        assertThat(runtime.program.fanout.get(0).size(), is(1));
        assertThat(runtime.program.fanout.get(1).size(), is(1));
    }

    @Test
    public void petterson() {
        ILanguageRuntime runtime = load("tests/resources/peterson.sek");
        explore(runtime,10, 16);
    }

    @Test
    public void petersonMutualExclusion() {
        String ltl = "exclusion = ![]!(|p1.state == 2| && |p2.state == 2|)";
        verifyLTL("tests/resources/peterson.sek", ltl, false, 10, null);
    }

    @Test
    public void petersonFairness() {
        String ltl =
                "//if one sets a flag eventually it gets to the critical section\n" +
                "fair = let\n" +
                "\tp1cs \t= |p1.state == 2|,\n" +
                "\tp1GetsIn = [] (|p1.flag == 1| => <> p1cs),\n" +
                "\tp2cs\t\t= |p2.state == 2|,\n" +
                "\tp2GetsIn \t= [] (|p2.flag == 1| => <> p2cs)\n" +
                "in !(p1GetsIn && p2GetsIn)";
        verifyLTL("tests/resources/peterson.sek", ltl, false, 18, null);
    }

    @Test
    public void petersonIdling() {
        String ltl =
                "idle = let\n" +
                "\tp1cs \t= |p1.state == 2|,\n" +
                "\tp1SetsFlag = |p1.flag == 1|,\n" +
                "in ! ( ([] !p1SetsFlag) => ([] ! p1cs) )";
        verifyLTL("tests/resources/peterson.sek", ltl, false, 3, null);

        ltl =
                "idle = let\n" +
                "\tp2cs \t= |p2.state == 2|,\n" +
                "\tp2SetsFlag = |p2.flag == 1|,\n" +
                "in ! ( ([] !p2SetsFlag) => ([] ! p2cs) )";
        verifyLTL("tests/resources/peterson.sek", ltl, false, 4, null);
    }


    @Test
    public void peterson1Infoften() {
        String ltl =
                "infoften = ! ([] <> |p1.state == 2| )";
        verifyLTL("tests/resources/peterson.sek", ltl, true, 18, "p1infoften.tgf");
    }

    @Test
    public void peterson2Infoften() {
        String ltl =
                "infoften = ! ([] <> |p2.state == 2| )";
        verifyLTL("tests/resources/peterson.sek", ltl, true, 18, "p2infoften.tgf");
    }

    @Test
    public void testSimple() {
        ILanguageRuntime runtime = load("tests/resources/stateSpaceExample.sek");
        explore(runtime,3, 3);
    }

    @Test
    public void testAlice_Bob() {
        ILanguageRuntime runtime = load("tests/resources/alice-bob.sek");
        AbstractExplorer controller = explore(runtime, 11, 18);
        StateSpace2TGF.toTGF(controller.getStateSpaceManager().getGraphView(), true, "alice_bob.tgf");
    }

    @Test
    public void testPredicateAlice_Bob() {
        ILanguageRuntime runtime = load("tests/resources/alice-bob.sek");
        assertThat(verify(runtime, "!(|alice.state == 2| && |bob.state == 3|)", 11, 18), is(true));
    }

    @Test
    public void testFairnessSimple0() {
        String ltl = "pred = ![](|a == 1| => (<> |b == 1|))";
        verifyLTL("tests/resources/simple0.sek", ltl, true, 3, "simple0.tgf");
    }

    @Test
    public void testFairnessSimple() {
        String ltl = "pred = ![](|a == 1| => (<> |b == 1|))";
        verifyLTL("tests/resources/simple.sek", ltl, true, 7, "simple.tgf");
    }

    @Test
    public void testMutualExclusion() {
        String ltl = "mutualExclusion = ! [] ! (|alice.state == 2| && |bob.state == 3|)";
        verifyLTL("tests/resources/alice-bob.sek", ltl, false, 11, "mutual_exclusion.tgf");
    }

    @Test
    public void testIdlingAlice() {
        String ltl =
                "fair = let\n" +
                "\taliceInCS \t= |alice.state == 2|,\n" +
                "\taliceSetsFlag = |alice.flag == 1|,\n" +
                "in ! ( ([] !aliceSetsFlag) => ([] ! aliceInCS) )";
        verifyLTL("tests/resources/alice-bob.sek", ltl, false, 3, "alice_idling.tgf");
    }

    @Test
    public void testIdlingBob() {
        String ltl =
                "fair = let\n" +
                        "\tbobInCS \t= |bob.state == 3|,\n" +
                        "\tbobSetsFlag = |bob.flag == 1|,\n" +
                        "in ! ( ([] !bobSetsFlag) => ([] ! bobInCS) )";
        verifyLTL("tests/resources/alice-bob.sek", ltl, false, 3, "bob_idling.tgf");
    }

    @Test
    public void testInfoftenAlice() {
        String ltl =
                "fair = ! ([] <> |alice.state == 2| )";
        verifyLTL("tests/resources/alice-bob.sek", ltl, true, 19, "alice_infoften.tgf");
    }

    @Test
    public void testInfoftenBob() {
        String ltl =
                "fair = ! ([] <> |bob.state == 3| )";
        verifyLTL("tests/resources/alice-bob.sek", ltl, true, 22, "bob_infoften.tgf");
    }

    @Test
    public void testWeakFairnessAlice() {
        String ltl =
                "//if alice sets the flag eventually she gets to the critical section\n" +
                "fair = let\n" +
                "\taliceInCS \t= |alice.state == 2|,\n" +
                "\taliceGetsIn = (|alice.flag == 1| => <> aliceInCS),\n" +
                "in !aliceGetsIn";
        verifyLTL("tests/resources/alice-bob.sek", ltl, false, 4, "alice_weak_fairness.tgf");
    }

    @Test
    public void testWeakFairnessBob() {
        String ltl =
                "//if bob sets a flag eventually he gets to the critical section\n" +
                "fair = let\n" +
                "\tbobInCS\t\t= |bob.state == 3|,\n" +
                "\tbobGetsIn \t= (|bob.flag == 1| => <> bobInCS)\n" +
                "in ! bobGetsIn";
        verifyLTL("tests/resources/alice-bob.sek", ltl, false, 6, "bob_weak_fairness.tgf");
    }

    @Test
    public void testWeakFairnessAliceAndBob() {
        String ltl =
                "//if one sets a flag eventually it gets to the critical section\n" +
                        "fair = let\n" +
                        "\taliceInCS \t= |alice.state == 2|,\n" +
                        "\taliceGetsIn = (|alice.flag == 1| => <> aliceInCS),\n" +
                        "\tbobInCS\t\t= |bob.state == 3|,\n" +
                        "\tbobGetsIn \t= (|bob.flag == 1| => <> bobInCS)\n" +
                        "in !(aliceGetsIn && bobGetsIn)";

        verifyLTL("tests/resources/alice-bob.sek", ltl, false, 9, "alice_and_bob_weak_fairness.tgf");
    }

    @Test
    public void testFairnessAlice() {
        String ltl =
                "//if alice sets the flag eventually she gets to the critical section\n" +
                "fair = let\n" +
                "\taliceInCS \t= |alice.state == 2|,\n" +
                "\taliceGetsIn = [] (|alice.flag == 1| => <> aliceInCS),\n" +
                "in !aliceGetsIn";
        verifyLTL("tests/resources/alice-bob.sek", ltl, false, 15, "alice_fairness.tgf");
    }

    @Test
    public void testFairnessBob() {
        String ltl =
                "//if bob sets a flag eventually he gets to the critical section\n" +
                "fair = let\n" +
                "\tbobInCS\t\t= |bob.state == 3|,\n" +
                "\tbobGetsIn \t= [] (|bob.flag == 1| => <> bobInCS)\n" +
                "in ! bobGetsIn";

        verifyLTL("tests/resources/alice-bob.sek", ltl, true, 17, "bob_fairness.tgf");
    }

    @Test
    public void testFairnessAliceAndBob() {
        String ltl =
                "//if one sets a flag eventually it gets to the critical section\n" +
                "fair = let\n" +
                "\taliceInCS \t= |alice.state == 2|,\n" +
                "\taliceGetsIn = [] (|alice.flag == 1| => <> aliceInCS),\n" +
                "\tbobInCS\t\t= |bob.state == 3|,\n" +
                "\tbobGetsIn \t= [] (|bob.flag == 1| => <> bobInCS)\n" +
                "in !(aliceGetsIn && bobGetsIn)";

        verifyLTL("tests/resources/alice-bob.sek", ltl, true, 21, "alice_and_bob_fairness.tgf");
    }

    private void verifyLTL(String fileName, String ltl, boolean hasAcceptanceCycle, int expectedSize, String tgfPath) {
        RuntimeDescription kripke = new RuntimeDescription(Paths.get(fileName));
        BuchiDeclaration buchiAutomaton = getBuchiDeclaration(ltl);
        plug.language.buchi.runtime.BuchiRuntime buchiRuntime = new plug.language.buchi.runtime.BuchiRuntime(buchiAutomaton);

        KripkeBuchiProductSemantics kbProductSemantics = new KripkeBuchiProductSemantics(kripke, buchiRuntime);

        BA_GaiserSchwoon_Iterative verifier = new BA_GaiserSchwoon_Iterative(kbProductSemantics);


        boolean[] result = new boolean[] { true };
        verifier.getAnnouncer().when(PropertyEvent.class, (announcer, event) -> {
            result[0] &= event.isVerified();
        });
        verifier.execute();

        if (result[0] == hasAcceptanceCycle) {
            Assert.fail("Property " + (result[0] ? "is verified but shouldn't" : "isn't verified but should"));
        }
    }

    BuchiDeclaration getBuchiDeclaration(String ltlFormula) {
        DeclarationBlock propertiesBlock = Parser.parseBlock(ltlFormula);
        Expression property = propertiesBlock.getDeclarations().iterator().next().getExpression();
        LTL2Buchi convertor = new LTL2Buchi(new PrintWriter(System.out));

        BuchiDeclaration decl = convertor.convert(property);
        return decl;
    }

//    DFSExplorer4Buchi verifyLTL_old_and_sick(String modelPath, String ltlFormula, boolean hasAcceptanceCycle, int expectedSize, String tgfPath) {
//        DeclarationBlock propertiesBlock = Parser.parseBlock(ltlFormula);
//        Expression property = propertiesBlock.getDeclarations().iterator().next().getExpression();
//        LTL2Buchi convertor = new LTL2Buchi(new PrintWriter(System.out));
//
//        BuchiDeclaration decl = convertor.convert(property);
//
//        DFSExplorer4Buchi controller = new DFSExplorer4Buchi();
//        controller.stateSpaceManager = new SimpleStateSpaceManager();
//        controller.stateSpaceManager.fullTransitionStorage();
//
//
//        ILanguageRuntime runtime = load(modelPath);
//        controller.addRuntime(runtime);
//
//
//        BuchiRuntime br = new BuchiRuntime();
//        br.buchi = decl;
//
//
//        Evaluator evaluator = new Evaluator();
//        AtomEvaluator atomE = new AtomEvaluator();
//        atomE.program = ((StateEventRuntime)runtime).program;
//        evaluator.addAtomEvaluator("sek", atomE);
//        evaluator.setDefaultEvaluator(atomE);
//
//
//        br.predicate = (CompositeConfiguration configuration, Expression expression) -> {
//            atomE.configuration = (StateEventConfiguration) configuration.get(0);
//            return evaluator.evaluate(expression);
//        };
//
//
//        QueueBuchiVerifier bV = new QueueBuchiVerifier(controller.getAnnouncer(), br);
//
//        boolean acceptanceCycle[] = new boolean[] {false};
//        bV.announcer.when(AcceptanceCycleDetectedEvent.class, (Announcer ann, AcceptanceCycleDetectedEvent evt) -> {
//            System.out.println(evt.getAcceptingConfiguration());
//            acceptanceCycle[0] = true;
//        });
//
//        BiConsumer<Announcer, ExecutionEndedEvent> sizeChecker;
//        controller.announcer.when(ExecutionEndedEvent.class, sizeChecker = (a, e) -> assertEquals(expectedSize, e.getSource().getStateSpaceManager().size()));
//
//        controller.explore();
//
//        if (tgfPath != null) {
//            StateSpace2TGF.toTGF(controller.getStateSpaceManager().getGraphView(), true, tgfPath);
//        }
//
//        assertThat(acceptanceCycle[0], is(hasAcceptanceCycle));
//
//        return controller;
//    }

    @Test
    public void testDeadlockfreeSimple() {
        ILanguageRuntime runtime = load("tests/resources/stateSpaceExample.sek");
        assertThat(deadlockfree(runtime, 3, 4), is(true));
    }

    @Test
    public void testDeadlockfreeAlice_Bob() {
        ILanguageRuntime runtime = load("tests/resources/alice-bob.sek");
        assertThat(deadlockfree(runtime, 11, 17), is(true));
    }


    public boolean deadlockfree(ILanguageRuntime runtime, int expectedStateCount, int expectedTransitionCount) {
        BFSExplorer explorer = new BFSExplorer(runtime, new SimpleStateSpaceManager(true));

        DeadlockVerifier dV = new DeadlockVerifier(explorer.getAnnouncer());

        boolean deadLockFree[] = new boolean[] { true };
        dV.announcer.when(FinalStateDetected.class, (ann, ev) -> {
            System.out.println("Final state detected: " + ev.getFinalState() );
            deadLockFree[0] = false;
        });

        explorer.execute();

        return deadLockFree[0];
    }

    public boolean verify(ILanguageRuntime runtime, String predicate, int expectedStateCount, int expectedTransitionCount) {
        BFSExplorer explorer = new BFSExplorer(runtime, new SimpleStateSpaceManager(true));

        PredicateVerifier<StateEventConfiguration> pV = new PredicateVerifier<>(explorer.getAnnouncer());

        //create the predicate
        Expression exp = Parser.parse(predicate);
        Evaluator evaluator = new Evaluator();
        AtomEvaluator atomE = new AtomEvaluator();
        atomE.program = ((StateEventRuntime)runtime).program;
        evaluator.addAtomEvaluator("sek", atomE);
        evaluator.setDefaultEvaluator(atomE);
        Predicate ipred = new Predicate(exp, evaluator, atomE);

        pV.predicates.add(ipred);

        boolean ret[] = new boolean[] {true};
        pV.announcer.when(PredicateViolationEvent.class, (announcer, event) -> {
            System.err.println("predicate violated: " + event.getViolated());
            explorer.hasToFinish();
            ret[0] = false;
        });

        explorer.execute();

        Assert.assertEquals(expectedStateCount, explorer.getStateSpaceManager().size());
        Assert.assertEquals(expectedTransitionCount, explorer.getStateSpaceManager().transitionCount());

        return ret[0];
    }

    public AbstractExplorer explore(ILanguageRuntime runtime, int expectedStateCount, int expectedTransitionCount) {
        BFSExplorer explorer = new BFSExplorer(runtime, new SimpleStateSpaceManager(true));
        explorer.execute();

        Assert.assertEquals(expectedStateCount, explorer.getStateSpaceManager().size());
        Assert.assertEquals(expectedTransitionCount, explorer.getStateSpaceManager().transitionCount());

        return explorer;
    }
}
