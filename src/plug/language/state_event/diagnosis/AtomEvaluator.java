package plug.language.state_event.diagnosis;

import plug.language.state_event.model.StateEventModel;
import plug.language.state_event.runtime.StateEventConfiguration;
import properties.PropositionalLogic.interpreter.IAtomEvaluator;

import java.util.StringTokenizer;

/**
 * @author Ciprian Teodorov (ciprian.teodorov@ensta-bretagne.fr)
 *         Created on 11/03/16.
 */
public class AtomEvaluator implements IAtomEvaluator {
    public StateEventConfiguration configuration;
    public StateEventModel program;
    @Override
    public Object parse(String code) {
        EqualityCondition condition = new EqualityCondition();
        StringTokenizer tokenizer = new StringTokenizer(code);

        condition.variable = tokenizer.nextToken();
        /*String operator =*/ tokenizer.nextToken();
        condition.value = Integer.parseInt(tokenizer.nextToken());
        return condition;
    }

    @Override
    public boolean evaluate(Object atomicBooleanExpression) {
        EqualityCondition condition = (EqualityCondition)atomicBooleanExpression;

        return configuration.values[program.variables.get(condition.variable)] == condition.value;
    }
}

