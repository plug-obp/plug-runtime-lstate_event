package obp2.language.state_event.diagnosis.dsl.model;

import obp2.language.state_event.diagnosis.dsl.DiagnosisModelVisitor;

public class ClockRef extends Reference {

    public ClockRef(String name) {
        super(name);
    }

    public ClockRef(String name, int index) {
        super(name, index);
        this.type = Type.BOOLEAN;
    }

    public <T> T accept(DiagnosisModelVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
