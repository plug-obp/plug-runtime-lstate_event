package plug.language.state_event.diagnosis.dsl.model;

public class LiteralExp extends DiagnosisExp {
    public int getValue() {
        return value;
    }

    int value;

    public LiteralExp(int value) {
        this.value = value;
        type = Type.NUMBER;
    }
}
