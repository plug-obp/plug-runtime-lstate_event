package obp2.language.state_event.diagnosis.dsl.evaluator;

public class BooleanValue extends Value {
    boolean value;

    public static final BooleanValue tt = new BooleanValue(true);
    public static final BooleanValue ff = new BooleanValue(false);

    private BooleanValue(boolean value) {
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }
}
