package plug.language.state_event.diagnosis.dsl.model;

import plug.language.state_event.diagnosis.dsl.DiagnosisModelVisitor;

public class VariableRef extends Reference {
    public VariableRef(String name) {
        super(name);
    }

    public VariableRef(String name, int index) {
        super(name, index);
        type = Type.NUMBER;
    }

    public <T> T accept(DiagnosisModelVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
