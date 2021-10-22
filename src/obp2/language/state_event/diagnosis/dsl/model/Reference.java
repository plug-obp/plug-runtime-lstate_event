package obp2.language.state_event.diagnosis.dsl.model;

public abstract class Reference extends DiagnosisExp {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

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
