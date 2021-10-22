package obp2.language.state_event.diagnosis.dsl.model;

import obp2.language.state_event.diagnosis.dsl.DiagnosisModelVisitor;

public class LiteralExp extends DiagnosisExp {
    public int getValue() {
        return value;
    }

    int value;

    public LiteralExp(int value) {
        this.value = value;
        type = Type.NUMBER;
    }

    public <T> T accept(DiagnosisModelVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
