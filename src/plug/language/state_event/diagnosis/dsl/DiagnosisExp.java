package plug.language.state_event.diagnosis.dsl;

public class DiagnosisExp {
    public <T> T accept(DiagnosisVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
