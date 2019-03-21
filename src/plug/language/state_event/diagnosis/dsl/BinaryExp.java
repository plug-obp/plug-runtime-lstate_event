package plug.language.state_event.diagnosis.dsl;

public class BinaryExp extends DiagnosisExp {
    DiagnosisExp lhs;
    DiagnosisExp rhs;
    enum BinaryOperator {
        PLUS, MINUS, EQ, NEQ, LEQ, LT, GEQ, GT, MULT
    }
    BinaryOperator operator;

}
