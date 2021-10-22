package obp2.language.state_event.model;

/**
 * Created by Ciprian TEODOROV on 14/04/17.
 */
public class StateEventTransition {
    public StateEventModel model;
    public int id;
    public int[] events;
    public int target;

    @Override
    public String toString() {
        String transition = "{";
        boolean first = true;
        for (int i : events) {
            if (first) {
                first = false;
            } else {
                transition += ", ";
            }
            transition += model.idx2events.get(i);
        }
        transition += "} -> " + target;
        return transition;
    }
}
