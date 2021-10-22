package obp2.language.state_event;

import obp2.runtime.core.TreeItem;
import obp2.runtime.core.defaults.DefaultTreeProjector;
import obp2.language.state_event.model.StateEventTransition;
import obp2.language.state_event.runtime.StateEventConfiguration;
import obp2.language.state_event.runtime.StateEventTransitionRelation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Ciprian TEODOROV on 03/03/17.
 */
public class StateEventRuntimeView extends DefaultTreeProjector<StateEventConfiguration, StateEventTransition, Void> {
    private final StateEventTransitionRelation runtime;

    public StateEventRuntimeView(StateEventTransitionRelation runtime) {
        this.runtime = runtime;
    }

	@Override
    public TreeItem projectConfiguration(StateEventConfiguration value) {
		List<TreeItem> variableList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : runtime.program.variables.entrySet()) {
        		variableList.add(new TreeItem("variable", entry.getKey() + " = " + value.values[entry.getValue()], null, null));
        }
		return new TreeItem(runtime.name, variableList);
	}
}
