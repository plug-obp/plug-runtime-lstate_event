package obp2.language.state_event.diagnosis;

import obp2.runtime.core.IAtomicPropositionsEvaluator;
import obp2.runtime.core.defaults.DefaultLanguageService;
import obp2.language.state_event.diagnosis.dsl.evaluator.EvaluationEnvironment;
import obp2.language.state_event.diagnosis.dsl.evaluator.Linker;
import obp2.language.state_event.diagnosis.dsl.evaluator.SEDiagnosisEvaluator;
import obp2.language.state_event.diagnosis.dsl.model.DiagnosisExp;
import obp2.language.state_event.diagnosis.dsl.parsing.SEDiagnosisParser;
import obp2.language.state_event.model.StateEventModel;
import obp2.language.state_event.model.StateEventTransition;
import obp2.language.state_event.runtime.StateEventConfiguration;

public class SEAtomicPropositionsEvaluator
        extends DefaultLanguageService<StateEventConfiguration, StateEventTransition, Void>
        implements IAtomicPropositionsEvaluator<StateEventConfiguration, StateEventTransition, Void> {
    String atomicPropositions[];

    public StateEventModel model;
    EvaluationEnvironment environment;
    DiagnosisExp propositions[];

    Linker linker = new Linker();
    SEDiagnosisEvaluator evaluator = new SEDiagnosisEvaluator();

    public SEAtomicPropositionsEvaluator(StateEventModel model) {
        this.model = model;
        this.environment = new EvaluationEnvironment(this.model);
    }

    @Override
    public int[] registerAtomicPropositions(String[] atomicPropositions) {
        int[] result = new int[atomicPropositions.length];
        this.atomicPropositions = atomicPropositions;
        propositions = new DiagnosisExp[atomicPropositions.length];
        for (int i = 0; i<propositions.length; i++) {
            propositions[i] = parse(atomicPropositions[i]);
            result[i] = i;
        }
        return result;
    }

    DiagnosisExp parse(String code) {
        DiagnosisExp expression = SEDiagnosisParser.parse(code, (e)->System.err.println(e.getMessage()));
        linker.link(expression, model);
        return expression;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public boolean[] getAtomicPropositionValuations(StateEventConfiguration configuration) {
        environment.setSource((StateEventConfiguration) configuration);

        boolean valuations[] = new boolean[propositions.length];
        for (int i = 0; i<propositions.length;i++) {
            valuations[i] = evaluator.evaluate(propositions[i], environment);
        }
        return valuations;
    }


    @SuppressWarnings("Duplicates")
    @Override
    public boolean[] getAtomicPropositionValuations(StateEventConfiguration source, StateEventTransition fireable, Void payload, StateEventConfiguration target) {
        environment.setSource((StateEventConfiguration) source);
        environment.setFireable((StateEventTransition) fireable);
        environment.setPayload(payload);
        environment.setTarget((StateEventConfiguration) target);

        boolean valuations[] = new boolean[propositions.length];
        for (int i = 0; i<propositions.length;i++) {
            valuations[i] = evaluator.evaluate(propositions[i], environment);
        }
        return valuations;
    }
}
