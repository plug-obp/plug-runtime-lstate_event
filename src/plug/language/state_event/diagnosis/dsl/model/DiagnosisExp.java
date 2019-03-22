package plug.language.state_event.diagnosis.dsl.model;

import plug.language.state_event.diagnosis.dsl.DiagnosisModelVisitor;

public abstract class DiagnosisExp {
    Type type = Type.UNKNOWN;
    public <T> T accept(DiagnosisModelVisitor<T> visitor) {
        return visitor.visit(this);
    }
    public Type getType() {
        return type;
    }

}
