package plug.language.state_event.diagnosis.dsl;

import plug.language.state_event.diagnosis.dsl.model.*;

public interface DiagnosisModelVisitor<T> {
    T visit(DiagnosisExp expr);
    T visit(LiteralExp expr);
    T visit(ClockRef expr);
    T visit(TransitionRef expr);
    T visit(VariableRef expr);
    T visit(NextVariableRef expr);
    T visit(UnaryExp expr);
    T visit(BinaryExp expr);
    T visit(ConditionalExp expr);

}
