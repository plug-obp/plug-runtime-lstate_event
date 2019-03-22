package plug.language.state_event.diagnosis.dsl.model;

public class UnaryExp extends DiagnosisExp {
    public DiagnosisExp getOperand() {
        return operand;
    }

    DiagnosisExp operand;

    public enum Operator {
        NOT, MINUS
    }

    public Operator getOperator() {
        return operator;
    }

    Operator operator;

    public UnaryExp(Operator operator, DiagnosisExp operand) {
        this.operator = operator;
        this.operand = operand;
    }

    @Override
    public Type getType() {
        if (type == null || type == Type.UNKNOWN) {
            Type oT = operand.getType();

            switch (operator) {

                case NOT:
                    if (oT != Type.BOOLEAN) {
                        throw new RuntimeException("NOT expects Boolean operand");
                    }
                    type = Type.BOOLEAN;
                    break;
                case MINUS:
                    if (oT != Type.NUMBER) {
                        throw new RuntimeException("MINUS expects Boolean operand");
                    }
                    type = Type.NUMBER;
                    break;
            }
        }
        return type;
    }
}
