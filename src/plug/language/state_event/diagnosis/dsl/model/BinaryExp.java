package plug.language.state_event.diagnosis.dsl.model;

public class BinaryExp extends DiagnosisExp {
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
}
