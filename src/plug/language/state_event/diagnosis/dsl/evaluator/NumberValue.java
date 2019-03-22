package plug.language.state_event.diagnosis.dsl.evaluator;



public class NumberValue extends Value {
    int value;
    public NumberValue(int value) {
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }
}
