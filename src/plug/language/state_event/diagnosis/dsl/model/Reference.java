package plug.language.state_event.diagnosis.dsl.model;

public class Reference extends DiagnosisExp {
    String name;
    int index;

    public Reference(String name) {
        this(name, -1);
    }

    public Reference(String name, int index) {
        this.name = name;
        this.index = index;
    }
}
