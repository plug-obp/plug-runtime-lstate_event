package plug.languages.state_event;

import org.junit.Assert;
import org.junit.Test;
import plug.core.ILanguagePlugin;
import plug.core.IRuntimeView;
import plug.core.ITransitionRelation;
import plug.core.RuntimeDescription;
import plug.events.PropertyEvent;
import plug.explorer.AbstractExplorer;
import plug.explorer.BFSExplorer;
import plug.explorer.buchi.nested_dfs.BA_GaiserSchwoon_Iterative;
import plug.language.buchikripke.runtime.KripkeBuchiProductSemantics;
import plug.language.state_event.StateEventPlugin;
import plug.language.state_event.runtime.StateEventTransitionRelation;
import plug.statespace.SimpleStateSpaceManager;
import plug.verifiers.deadlock.DeadlockVerifier;
import plug.verifiers.deadlock.FinalStateDetected;
import plug.visualisation.StateSpace2TGF;
import properties.BuchiAutomata.BuchiAutomataModel.BuchiDeclaration;
import properties.LTL.transformations.LTL2Buchi;

import java.io.PrintWriter;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;

/**
 * @author Ciprian Teodorov (ciprian.teodorov@ensta-bretagne.fr)
 *         Created on 10/03/16.
 */
public class StateEventRuntimeTest {

    ILanguagePlugin module = new StateEventPlugin();

    ITransitionRelation load(String fileName) throws Exception {
        return module.getLoader().getRuntime(fileName);
    }

    @Test
    public void testDifferentRuntimeViews() throws Exception {
        IRuntimeView view1 = module.getRuntimeView(new StateEventTransitionRelation());
        IRuntimeView view2 = module.getRuntimeView(new StateEventTransitionRelation());
        assertNotSame(view1, view2);
    }

    @Test
    public void testLoad() throws Exception {
        StateEventTransitionRelation runtime = (StateEventTransitionRelation) load("tests/resources/stateSpaceExample.sek");
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
    public void petterson() throws Exception {
        ITransitionRelation runtime = load("tests/resources/peterson.sek");
        explore(runtime,10, 16);
    }

    @Test
    public void petersonMutualExclusion() throws Exception {
        String ltl = "exclusion = []!(|v[p1.state] = 2| && |v[p2.state] = 2|)";
        verifyLTL("tests/resources/peterson.sek", ltl, false, 11, null);
    }

    @Test
    public void petersonFairness() throws Exception {
        String ltl =
                "//if one sets a flag eventually it gets to the critical section\n" +
                "fair = let\n" +
                "\tp1cs \t= |v[p1.state] = 2|,\n" +
                "\tp1GetsIn = [] (|v[p1.flag] = 1| => <> p1cs),\n" +
                "\tp2cs\t\t= |v[p2.state] = 2|,\n" +
                "\tp2GetsIn \t= [] (|v[p2.flag] = 1| => <> p2cs)\n" +
                "in (p1GetsIn && p2GetsIn)";
        verifyLTL("tests/resources/peterson.sek", ltl, false, 19, null);
    }

    @Test
    public void petersonIdling() throws Exception {
        String ltl =
                "idle = let\n" +
                "\tp1cs \t= |v[p1.state] = 2|,\n" +
                "\tp1SetsFlag = |v[p1.flag] = 1|,\n" +
                "in ( ([] !p1SetsFlag) => ([] ! p1cs) )";
        verifyLTL("tests/resources/peterson.sek", ltl, false, 4, null);

        ltl =
                "idle = let\n" +
                "\tp2cs \t= |v[p2.state] = 2|,\n" +
                "\tp2SetsFlag = |v[p2.flag] = 1|,\n" +
                "in ( ([] !p2SetsFlag) => ([] ! p2cs) )";
        verifyLTL("tests/resources/peterson.sek", ltl, false, 5, null);
    }


    @Test
    public void peterson1Infoften() throws Exception {
        String ltl =
                "infoften = ! ([] <> |v[p1.state] = 2| )";
        verifyLTL("tests/resources/peterson.sek", ltl, true, 10, "p1infoften.tgf");
    }

    @Test
    public void peterson2Infoften() throws Exception {
        String ltl =
                "infoften = ! ([] <> |v[p2.state] = 2| )";
        verifyLTL("tests/resources/peterson.sek", ltl, true, 8, "p2infoften.tgf");
    }

    @Test
    public void testSimple() throws Exception {
        ITransitionRelation runtime = load("tests/resources/stateSpaceExample.sek");
        explore(runtime,3, 3);
    }

    @Test
    public void testAlice_Bob() throws Exception {
        ITransitionRelation runtime = load("tests/resources/alice-bob.sek");
        AbstractExplorer controller = explore(runtime, 11, 18);
        StateSpace2TGF.toTGF(controller.getStateSpaceManager().getGraphView(), true, "alice_bob.tgf");
    }

    @Test
    public void testFairnessSimple0() throws Exception {
        String ltl = "pred = ![](|v[a] = 1| => (<> |v[b] = 1|))";
        verifyLTL("tests/resources/simple0.sek", ltl, true, 2, "simple0.tgf");
    }

    @Test
    public void testFairnessSimple() throws Exception {
        String ltl = "pred = ![](|v[a] = 1| => (<> |v[b] = 1|))";
        verifyLTL("tests/resources/simple.sek", ltl, true, 4, "simple.tgf");
    }

    @Test
    public void testMutualExclusion() throws Exception {
        String ltl = "mutualExclusion = [] ! (|v[alice.state] = 2| && |v[bob.state] = 3|)";
        verifyLTL("tests/resources/alice-bob.sek", ltl, false, 12, "mutual_exclusion.tgf");
    }

    @Test
    public void testIdlingAlice() throws Exception {
        String ltl =
                "fair = let\n" +
                "\taliceInCS \t= |v[alice.state] = 2|,\n" +
                "\taliceSetsFlag = |v[alice.flag] = 1|,\n" +
                "in  ( ([] !aliceSetsFlag) => ([] ! aliceInCS) )";
        verifyLTL("tests/resources/alice-bob.sek", ltl, false, 4, "alice_idling.tgf");
    }

    @Test
    public void testIdlingBob() throws Exception {
        String ltl =
                "fair = let\n" +
                        "\tbobInCS \t= |v[bob.state] = 3|,\n" +
                        "\tbobSetsFlag = |v[bob.flag] = 1|,\n" +
                        "in ( ([] !bobSetsFlag) => ([] ! bobInCS) )";
        verifyLTL("tests/resources/alice-bob.sek", ltl, false, 4, "bob_idling.tgf");
    }

    @Test
    public void testInfoftenAlice() throws Exception {
        String ltl =
                "fair = ([] <> |v[alice.state] = 2| )";
        verifyLTL("tests/resources/alice-bob.sek", ltl, true, 10, "alice_infoften.tgf");
    }

    @Test
    public void testInfoftenBob() throws Exception {
        String ltl =
                "fair = ! ([] <> |v[bob.state] = 3| )";
        verifyLTL("tests/resources/alice-bob.sek", ltl, true, 12, "bob_infoften.tgf");
    }

    @Test
    public void testWeakFairnessAlice() throws Exception {
        String ltl =
                "//if alice sets the flag eventually she gets to the critical section\n" +
                "fair = let\n" +
                "\taliceInCS \t= |v[alice.state] = 2|,\n" +
                "\taliceGetsIn = (|v[alice.flag] = 1| => <> aliceInCS),\n" +
                "in aliceGetsIn";
        verifyLTL("tests/resources/alice-bob.sek", ltl, false, 1, "alice_weak_fairness.tgf");
    }

    @Test
    public void testWeakFairnessBob() throws Exception {
        String ltl =
                "//if bob sets a flag eventually he gets to the critical section\n" +
                "fair = let\n" +
                "\tbobInCS\t\t= |v[bob.state] = 3|,\n" +
                "\tbobGetsIn \t= (|v[bob.flag] = 1| => <> bobInCS)\n" +
                "in bobGetsIn";
        verifyLTL("tests/resources/alice-bob.sek", ltl, false, 1, "bob_weak_fairness.tgf");
    }

    @Test
    public void testWeakFairnessAliceAndBob() throws Exception {
        String ltl =
                "//if one sets a flag eventually it gets to the critical section\n" +
                        "fair = let\n" +
                        "\taliceInCS \t= |v[alice.state] = 2|,\n" +
                        "\taliceGetsIn = (|v[alice.flag] = 1| => <> aliceInCS),\n" +
                        "\tbobInCS\t\t= |v[bob.state] = 3|,\n" +
                        "\tbobGetsIn \t= (|v[bob.flag] = 1| => <> bobInCS)\n" +
                        "in (aliceGetsIn && bobGetsIn)";

        verifyLTL("tests/resources/alice-bob.sek", ltl, false, 1, "alice_and_bob_weak_fairness.tgf");
    }

    @Test
    public void testFairnessAlice() throws Exception {
        String ltl =
                "//if alice sets the flag eventually she gets to the critical section\n" +
                "fair = let\n" +
                "\taliceInCS \t= |v[alice.state] = 2|,\n" +
                "\taliceGetsIn = [] (|v[alice.flag] = 1| => <> aliceInCS),\n" +
                "in aliceGetsIn";
        verifyLTL("tests/resources/alice-bob.sek", ltl, false, 16, "alice_fairness.tgf");
    }

    @Test
    public void testFairnessBob() throws Exception {
        String ltl =
                "//if bob sets a flag eventually he gets to the critical section\n" +
                "fair = let\n" +
                "\tbobInCS\t\t= |v[bob.state] = 3|,\n" +
                "\tbobGetsIn \t= [] (|v[bob.flag] = 1| => <> bobInCS)\n" +
                "in ! bobGetsIn";

        verifyLTL("tests/resources/alice-bob.sek", ltl, true, 4, "bob_fairness.tgf");
    }

    @Test
    public void testFairnessAliceAndBob() throws Exception {
        String ltl =
                "//if one sets a flag eventually it gets to the critical section\n" +
                "fair = let\n" +
                "\taliceInCS \t= |v[alice.state] = 2|,\n" +
                "\taliceGetsIn = [] (|v[alice.flag] = 1| => <> aliceInCS),\n" +
                "\tbobInCS\t\t= |v[bob.state] = 3|,\n" +
                "\tbobGetsIn \t= [] (|v[bob.flag] = 1| => <> bobInCS)\n" +
                "in !(aliceGetsIn && bobGetsIn)";

        verifyLTL("tests/resources/alice-bob.sek", ltl, true, 4, "alice_and_bob_fairness.tgf");
    }

    private void verifyLTL(String fileName, String ltl, boolean hasAcceptanceCycle, int expectedSize, String tgfPath) throws Exception {
        RuntimeDescription kripke = new RuntimeDescription(Paths.get(fileName));
        LTL2Buchi buchiLoader = new LTL2Buchi(new PrintWriter(System.out));
        BuchiDeclaration buchiAutomaton = buchiLoader.getBuchiDeclaration(ltl);
        plug.language.buchi.runtime.BuchiRuntime buchiRuntime = new plug.language.buchi.runtime.BuchiRuntime(buchiAutomaton);

        BA_GaiserSchwoon_Iterative verifier =
                new BA_GaiserSchwoon_Iterative<>(
                        new KripkeBuchiProductSemantics(kripke, buchiRuntime),
                        new SimpleStateSpaceManager(true));

        boolean[] result = new boolean[] { true };
        verifier.getAnnouncer().when(PropertyEvent.class, (announcer, event) -> {
            result[0] &= event.isVerified();
        });
        verifier.execute();

        if (tgfPath != null) {
            StateSpace2TGF.toTGF(verifier.getStateSpaceManager().getGraphView(), true, tgfPath);
        }

        if (result[0] == hasAcceptanceCycle) {
            Assert.fail("Property " + (result[0] ? "is verified but shouldn't" : "isn't verified but should"));
        }
        assertThat(verifier.getStateSpaceManager().size(), is(expectedSize));
    }

    @Test
    public void testDeadlockfreeSimple() throws Exception {
        ITransitionRelation runtime = load("tests/resources/stateSpaceExample.sek");
        assertThat(deadlockfree(runtime, 3, 4), is(true));
    }

    @Test
    public void testDeadlockfreeAlice_Bob() throws Exception {
        ITransitionRelation runtime = load("tests/resources/alice-bob.sek");
        assertThat(deadlockfree(runtime, 11, 17), is(true));
    }


    public boolean deadlockfree(ITransitionRelation runtime, int expectedStateCount, int expectedTransitionCount) {
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

    public AbstractExplorer explore(ITransitionRelation runtime, int expectedStateCount, int expectedTransitionCount) {
        BFSExplorer explorer = new BFSExplorer(runtime, new SimpleStateSpaceManager(true));
        explorer.execute();

        Assert.assertEquals(expectedStateCount, explorer.getStateSpaceManager().size());
        Assert.assertEquals(expectedTransitionCount, explorer.getStateSpaceManager().transitionCount());

        return explorer;
    }
}
