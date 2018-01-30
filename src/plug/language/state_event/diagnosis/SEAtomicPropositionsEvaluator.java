package plug.language.state_event.diagnosis;

import plug.core.IAtomicPropositionsEvaluator;
import plug.core.IConfiguration;
import plug.language.state_event.model.StateEventModel;
import plug.language.state_event.runtime.StateEventConfiguration;

import java.util.StringTokenizer;

public class SEAtomicPropositionsEvaluator implements IAtomicPropositionsEvaluator {
    String atomicPropositions[];
    EqualityCondition testers[];

    public StateEventModel context;

    public SEAtomicPropositionsEvaluator(StateEventModel context) {
        this.context = context;
    }

    @Override
    public void registerAtomicPropositions(String[] atomicPropositions) {


        this.atomicPropositions = atomicPropositions;
        testers = new EqualityCondition[atomicPropositions.length];
        for (int i = 0; i<testers.length; i++) {

            testers[i] = parse(atomicPropositions[i]);
        }
    }

    EqualityCondition parse(String code) {
        EqualityCondition condition = new EqualityCondition();
        StringTokenizer tokenizer = new StringTokenizer(code);

        condition.variable = tokenizer.nextToken();
        /*String operator =*/ tokenizer.nextToken();
        condition.value = Integer.parseInt(tokenizer.nextToken());
        return condition;
    }

    @Override
    public boolean[] getAtomicPropositionValuations(IConfiguration source, IConfiguration target, Object transition) {
        StateEventConfiguration seTarget = (StateEventConfiguration)target;
        boolean valuations[] = new boolean[testers.length];
        for (int i = 0; i<testers.length; i++) {
            valuations[i] = seTarget.values[context.variables.get(testers[i].variable)] == testers[i].value;
        }
        return valuations;
    }
}
