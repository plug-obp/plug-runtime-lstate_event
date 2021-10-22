package obp2.language.state_event.diagnosis.dsl.model;

import obp2.language.state_event.diagnosis.dsl.DiagnosisModelVisitor;

public class NextVariableRef extends Reference {
    public NextVariableRef(String name) {
        super(name);
    }

    public NextVariableRef(String name, int index) {
        super(name, index);
        type = Type.NUMBER;
    }

    public <T> T accept(DiagnosisModelVisitor<T> visitor) {
        return visitor.visit(this);
    }
}