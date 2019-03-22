package plug.language.state_event.diagnosis.dsl.model;

public class UnaryExp extends DiagnosisExp {
    DiagnosisExp operand;

    public enum Operator {
        NOT, MINUS
    }
    Operator operator;

    public UnaryExp(Operator operator, DiagnosisExp operand) {
        this.operator = operator;
        this.operand = operand;
    }
}
