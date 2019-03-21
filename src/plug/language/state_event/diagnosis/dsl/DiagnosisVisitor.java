package plug.language.state_event.diagnosis.dsl;

public interface DiagnosisVisitor<T> {
    T visit(DiagnosisExp expr);
}
