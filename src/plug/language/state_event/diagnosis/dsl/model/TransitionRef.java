package plug.language.state_event.diagnosis.dsl.model;

import plug.language.state_event.diagnosis.dsl.DiagnosisModelVisitor;

public class TransitionRef extends Reference {
    public TransitionRef(String name) {
        super(name);
    }

    public TransitionRef(String name, int index) {
        super(name, index);
    }
    public TransitionRef(int index) {
        super(null, index);
        type = Type.BOOLEAN;
    }

    public <T> T accept(DiagnosisModelVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
