package plug.language.state_event.diagnosis.dsl.model;

public class VariableRef extends Reference {
    public VariableRef(String name) {
        super(name);
    }

    public VariableRef(String name, int index) {
        super(name, index);
        type = Type.NUMBER;
    }
}
