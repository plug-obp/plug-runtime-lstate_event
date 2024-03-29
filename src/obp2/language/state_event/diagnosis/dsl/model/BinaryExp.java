package obp2.language.state_event.diagnosis.dsl.model;

import obp2.language.state_event.diagnosis.dsl.DiagnosisModelVisitor;

public class BinaryExp extends DiagnosisExp {
    public DiagnosisExp getLhs() {
        return lhs;
    }

    public DiagnosisExp getRhs() {
        return rhs;
    }

    public Operator getOperator() {
        return operator;
    }

    DiagnosisExp lhs;
    DiagnosisExp rhs;
    public enum Operator {
        MULT, MOD, PLUS, MINUS, LT, LTE, GT, GTE, EQ, NEQ
    }
    Operator operator;

    public BinaryExp(DiagnosisExp lhs, Operator operator, DiagnosisExp rhs) {
        this.lhs = lhs;
        this.operator = operator;
        this.rhs = rhs;
    }

    public <T> T accept(DiagnosisModelVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Type getType() {
        if (type == null || type == Type.UNKNOWN) {
            Type lhsT = lhs.getType();
            Type rhsT = rhs.getType();

            switch (operator) {
                case MULT:
                    if (lhsT != Type.NUMBER || rhsT != Type.NUMBER) {
                        throw new RuntimeException("BinaryExp expecting number operands");
                    }
                    type = Type.NUMBER;
                    break;
                case MOD:
                    if (lhsT != Type.NUMBER || rhsT != Type.NUMBER) {
                        throw new RuntimeException("BinaryExp expecting number operands");
                    }
                    type = Type.NUMBER;
                    break;
                case PLUS:
                    if (lhsT != Type.NUMBER || rhsT != Type.NUMBER) {
                        throw new RuntimeException("BinaryExp expecting number operands");
                    }
                    type = Type.NUMBER;
                    break;
                case MINUS:
                    if (lhsT != Type.NUMBER || rhsT != Type.NUMBER) {
                        throw new RuntimeException("BinaryExp expecting number operands");
                    }
                    type = Type.NUMBER;
                    break;
                case LT:
                    if (lhsT != Type.NUMBER || rhsT != Type.NUMBER) {
                        throw new RuntimeException("BinaryExp expecting number operands");
                    }
                    type = Type.BOOLEAN;
                    break;
                case LTE:
                    if (lhsT != Type.NUMBER || rhsT != Type.NUMBER) {
                        throw new RuntimeException("BinaryExp expecting number operands");
                    }
                    type = Type.BOOLEAN;
                    break;
                case GT:
                    if (lhsT != Type.NUMBER || rhsT != Type.NUMBER) {
                        throw new RuntimeException("BinaryExp expecting number operands");
                    }
                    type = Type.BOOLEAN;
                    break;
                case GTE:
                    if (lhsT != Type.NUMBER || rhsT != Type.NUMBER) {
                        throw new RuntimeException("BinaryExp expecting number operands");
                    }
                    type = Type.BOOLEAN;
                    break;
                case EQ:
                    type = Type.BOOLEAN;
                    break;
                case NEQ:
                    type = Type.BOOLEAN;
                    break;
            }
            return type;
        }
        return type;
    }
}
