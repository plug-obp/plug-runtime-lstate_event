package plug.language.state_event.diagnosis.dsl.evaluator;

import plug.language.state_event.diagnosis.dsl.DiagnosisModelVisitor;
import plug.language.state_event.diagnosis.dsl.model.*;
import plug.language.state_event.model.StateEventModel;

public class Linker implements DiagnosisModelVisitor<Boolean> {
    StateEventModel model;

    public void link(DiagnosisExp expression, StateEventModel model) {
        this.model = model;
        expression.accept(this);
    }

    @Override
    public Boolean visit(LiteralExp expr) {
        //nothing to do
        return true;
    }

    @Override
    public Boolean visit(ClockRef expr) {
        Integer index = model.events.get(expr.getName());
        if (index == null) {
            System.err.println("The clock " + expr.getName() + " is not present in the model");
            expr.setIndex(-1);
        }
        expr.setIndex(index);
        return true;
    }

    @Override
    public Boolean visit(TransitionRef expr) {
        if (!(expr.getIndex() >= 0 && expr.getIndex() < model.nbTransitions)) {
            System.err.println("The system does not have a transition at index " + expr.getIndex());
            expr.setIndex(-1);
        }
        return true;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public Boolean visit(VariableRef expr) {
        Integer index = model.variables.get(expr.getName());
        if (index == null) {
            System.err.println("The variable " + expr.getName() + " is not present in the model");
            expr.setIndex(-1);
        }
        expr.setIndex(index);
        return true;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public Boolean visit(NextVariableRef expr) {
        Integer index = model.variables.get(expr.getName());
        if (index == null) {
            System.err.println("The variable " + expr.getName() + " is not present in the model");
            expr.setIndex(-1);
        }
        return true;
    }

    @Override
    public Boolean visit(UnaryExp expr) {
        return expr.getOperand().accept(this);
    }

    @Override
    public Boolean visit(BinaryExp expr) {
        expr.getLhs().accept(this);
        expr.getRhs().accept(this);
        return true;
    }

    @Override
    public Boolean visit(ConditionalExp expr) {
        expr.getCondition().accept(this);
        expr.getTrueBranch().accept(this);
        expr.getFalseBranch().accept(this);
        return true;
    }
}
