package plug.language.state_event.diagnosis.dsl.model;

import plug.language.state_event.diagnosis.dsl.DiagnosisModelVisitor;

public abstract class DiagnosisExp {
    Type type = Type.UNKNOWN;
    public abstract  <T> T accept(DiagnosisModelVisitor<T> visitor);
    public Type getType() {
        return type;
    }

}
