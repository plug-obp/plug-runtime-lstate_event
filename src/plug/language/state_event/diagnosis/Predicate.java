package plug.language.state_event.diagnosis;

import plug.language.state_event.runtime.StateEventConfiguration;
import properties.PropositionalLogic.PropositionalLogicModel.Expression;
import properties.PropositionalLogic.interpreter.Evaluator;

/**
 * @author Ciprian Teodorov (ciprian.teodorov@ensta-bretagne.fr)
 *         Created on 11/03/16.
 */
public class Predicate implements java.util.function.Predicate<StateEventConfiguration> {
    AtomEvaluator atomEvaluator;
    Evaluator evaluator;
    Expression predicate;

    public Predicate(Expression predicate, Evaluator evaluator, AtomEvaluator atomEvaluator) {
        this.evaluator = evaluator;
        this.predicate = predicate;
        this.atomEvaluator = atomEvaluator;
    }

    @Override
    public boolean test(StateEventConfiguration configuration) {
        atomEvaluator.configuration = configuration;
        boolean result = evaluator.evaluate(predicate);
        return result;
    }
}
