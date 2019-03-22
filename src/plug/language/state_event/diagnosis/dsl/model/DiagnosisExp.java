package plug.language.state_event.diagnosis.dsl.model;

import plug.language.state_event.diagnosis.dsl.DiagnosisVisitor;

public class DiagnosisExp {
    public <T> T accept(DiagnosisVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
