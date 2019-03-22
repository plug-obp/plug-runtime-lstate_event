package plug.language.state_event.diagnosis.dsl.evaluator;

import plug.language.state_event.model.StateEventModel;
import plug.language.state_event.model.StateEventTransition;
import plug.language.state_event.runtime.StateEventConfiguration;

public class EvaluationEnvironment {
    StateEventModel model;
    StateEventTransition fireable;
    StateEventPayload payload; class StateEventPayload {} //TODO: when payload added in FiredTransition it gets here
    StateEventConfiguration target;

    public EvaluationEnvironment(StateEventModel model) {
        this.model = model;
    }

    public StateEventModel getModel() {
        return model;
    }

    public void setModel(StateEventModel model) {
        this.model = model;
    }

    public StateEventConfiguration getSource() {
        return source;
    }

    public void setSource(StateEventConfiguration source) {
        this.source = source;
    }

    StateEventConfiguration source;

    public StateEventTransition getFireable() {
        return fireable;
    }

    public void setFireable(StateEventTransition fireable) {
        this.fireable = fireable;
    }

    public StateEventPayload getPayload() {
        return payload;
    }

    public void setPayload(StateEventPayload payload) {
        this.payload = payload;
    }

    public StateEventConfiguration getTarget() {
        return target;
    }

    public void setTarget(StateEventConfiguration target) {
        this.target = target;
    }


}
