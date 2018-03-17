package plug.language.state_event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import plug.core.IRuntimeView;
import plug.core.view.ConfigurationItem;
import plug.language.state_event.model.StateEventTransition;
import plug.language.state_event.runtime.StateEventConfiguration;
import plug.language.state_event.runtime.StateEventTransitionRelation;

/**
 * Created by Ciprian TEODOROV on 03/03/17.
 */
public class StateEventRuntimeView implements IRuntimeView<StateEventConfiguration, StateEventTransition> {
    private final StateEventTransitionRelation runtime;

    public StateEventRuntimeView(StateEventTransitionRelation runtime) {
        this.runtime = runtime;
    }

    @Override
    public StateEventTransitionRelation getRuntime() {
        return runtime;
    }

	@Override
	public List<ConfigurationItem> getConfigurationItems(StateEventConfiguration value) {
		List<ConfigurationItem> variableList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : runtime.program.variables.entrySet()) {
        		variableList.add(new ConfigurationItem("variable", entry.getKey() + " = " + value.values[entry.getValue()], null, null));
        }
		return variableList;
	}

	@Override
	public String getFireableTransitionDescription(StateEventTransition transition) {
		return transition.toString();
	}
}
