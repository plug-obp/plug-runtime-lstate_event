package plug.language.state_event.diagnosis;

import java.util.StringTokenizer;
import plug.core.IAtomicPropositionsEvaluator;
import plug.core.IConfiguration;
import plug.language.state_event.model.StateEventModel;
import plug.language.state_event.runtime.StateEventConfiguration;

public class SEAtomicPropositionsEvaluator implements IAtomicPropositionsEvaluator {
    String atomicPropositions[];
    EqualityCondition testers[];

    public StateEventModel context;

    public SEAtomicPropositionsEvaluator(StateEventModel context) {
        this.context = context;
    }

    @Override
    public int[] registerAtomicPropositions(String[] atomicPropositions) {
        int[] result = new int[atomicPropositions.length];
        this.atomicPropositions = atomicPropositions;
        testers = new EqualityCondition[atomicPropositions.length];
        for (int i = 0; i<testers.length; i++) {
            testers[i] = parse(atomicPropositions[i]);
            result[i] = i;
        }
        return result;
    }

    EqualityCondition parse(String code) {
        EqualityCondition condition = new EqualityCondition();
        StringTokenizer tokenizer = new StringTokenizer(code);

        condition.variable = tokenizer.nextToken();
        /*String operator =*/ tokenizer.nextToken();
        condition.value = Integer.parseInt(tokenizer.nextToken());
        return condition;
    }

    //TODO: this should probably have source, target, transition
    //public boolean[] getAtomicPropositionValuations(IConfiguration source, IConfiguration target, Object transition);
    
    @Override
    public boolean[] getAtomicPropositionValuations(IConfiguration target) {
        StateEventConfiguration seTarget = (StateEventConfiguration)target;
        boolean valuations[] = new boolean[testers.length];
        for (int i = 0; i<testers.length; i++) {
            valuations[i] = seTarget.values[context.variables.get(testers[i].variable)] == testers[i].value;
        }
        return valuations;
    }
}
