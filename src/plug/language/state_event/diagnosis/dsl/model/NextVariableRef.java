package plug.language.state_event.diagnosis.dsl.model;

public class NextVariableRef extends Reference {
    public NextVariableRef(String name) {
        super(name);
    }

    public NextVariableRef(String name, int index) {
        super(name, index);
        type = Type.NUMBER;
    }
}