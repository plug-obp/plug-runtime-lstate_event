package plug.language.state_event.diagnosis.dsl.evaluator;


import plug.language.state_event.diagnosis.dsl.DiagnosisModelVisitor;
import plug.language.state_event.diagnosis.dsl.model.*;
import plug.language.state_event.model.StateEventModel;

public class SEDiagnosisEvaluator implements DiagnosisModelVisitor<Value> {
    EvaluationEnvironment environment;

    boolean evaluate(DiagnosisExp expression, StateEventModel model, EvaluationEnvironment environment) {
        //link the expression with the model
        Linker linker = new Linker();
        linker.link(expression, model);
        //set the model in the environment
        environment.setModel(model);
        //evaluate the expression
        return evaluate(expression, environment);
    }

    boolean evaluate(DiagnosisExp expression, EvaluationEnvironment environment) {
        this.environment = environment;
        return (boolean)expression.accept(this).getValue();
    }

    @Override
    public Value visit(DiagnosisExp expr) {
        //nothing to do
        return null;
    }

    @Override
    public Value visit(LiteralExp expr) {
        return new NumberValue(expr.getValue());
    }

    @Override
    public Value visit(ClockRef expr) {
        for (int clki : environment.fireable.events) {
            if (clki == expr.getIndex()) {
                return BooleanValue.tt;
            }
        }
        return BooleanValue.ff;
    }

    @Override
    public Value visit(TransitionRef expr) {
        return environment.fireable.id == expr.getIndex() ? BooleanValue.tt : BooleanValue.ff;
    }

    @Override
    public Value visit(VariableRef expr) {
        return new NumberValue(environment.source.values[expr.getIndex()]);
    }

    @Override
    public Value visit(NextVariableRef expr) {
        return new NumberValue(environment.target.values[expr.getIndex()]);
    }

    @Override
    public Value visit(UnaryExp expr) {
        switch (expr.getOperator()) {
            case NOT:
                return (expr.getOperand().accept(this).getValue() == BooleanValue.tt)
                        ? BooleanValue.ff : BooleanValue.tt;
            case MINUS:
                return new NumberValue(-(int)(expr.getOperand().accept(this)).getValue());
        }
        return null;
    }

    @Override
    public Value visit(BinaryExp expr) {
        Value lhsV = expr.getLhs().accept(this);
        Value rhsV = expr.getRhs().accept(this);
        switch (expr.getOperator()) {
            case MULT: {
                int lhs = (int) lhsV.getValue();
                int rhs = (int) rhsV.getValue();
                return new NumberValue(lhs * rhs);
            }
            case MOD:{
                int lhs = (int) lhsV.getValue();
                int rhs = (int) rhsV.getValue();
                return new NumberValue(lhs % rhs);
            }
            case PLUS:{
                int lhs = (int) lhsV.getValue();
                int rhs = (int) rhsV.getValue();
                return new NumberValue(lhs + rhs);
            }
            case MINUS:{
                int lhs = (int) lhsV.getValue();
                int rhs = (int) rhsV.getValue();
                return new NumberValue(lhs - rhs);
            }
            case LT:{
                int lhs = (int) lhsV.getValue();
                int rhs = (int) rhsV.getValue();
                return lhs < rhs ? BooleanValue.tt : BooleanValue.ff;
            }
            case LTE:{
                int lhs = (int) lhsV.getValue();
                int rhs = (int) rhsV.getValue();
                return lhs <= rhs ? BooleanValue.tt : BooleanValue.ff;
            }
            case GT:{
                int lhs = (int) lhsV.getValue();
                int rhs = (int) rhsV.getValue();
                return lhs > rhs ? BooleanValue.tt : BooleanValue.ff;
            }
            case GTE:{
                int lhs = (int) lhsV.getValue();
                int rhs = (int) rhsV.getValue();
                return lhs >= rhs ? BooleanValue.tt : BooleanValue.ff;
            }
            case EQ: return lhsV.getValue() == rhsV.getValue() ? BooleanValue.tt : BooleanValue.ff;
            case NEQ:return lhsV.getValue() == rhsV.getValue() ? BooleanValue.ff : BooleanValue.tt;
        }
        return null;
    }

    @Override
    public Value visit(ConditionalExp expr) {
        BooleanValue condition = (BooleanValue)expr.getCondition().accept(this).getValue();
        if (condition == BooleanValue.tt) {
            return expr.getTrueBranch().accept(this);
        }
        return expr.getFalseBranch().accept(this);
    }
}
