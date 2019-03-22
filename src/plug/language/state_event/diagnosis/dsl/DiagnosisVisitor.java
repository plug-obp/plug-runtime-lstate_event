package plug.language.state_event.diagnosis.dsl;

import plug.language.state_event.diagnosis.dsl.model.DiagnosisExp;

public interface DiagnosisVisitor<T> {
    T visit(DiagnosisExp expr);
}
