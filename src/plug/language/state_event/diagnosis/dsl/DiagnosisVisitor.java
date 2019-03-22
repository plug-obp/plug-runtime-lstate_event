package plug.language.state_event.diagnosis.dsl;

import plug.language.state_event.diagnosis.dsl.model.*;

public interface DiagnosisVisitor<T> {
    T visit(DiagnosisExp expr);
    T visit(ValueExp expr);
    T visit(ClockRef expr);
    T visit(TransitionRef expr);
    T visit(VariableRef expr);
    T visit(NextExp expr);
    T visit(UnaryExp expr);
    T visit(BinaryExp expr);
    T visit(ConditionalExp expr);

}
