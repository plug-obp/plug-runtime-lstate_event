package obp2.language.state_event.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ciprian Teodorov (ciprian.teodorov@ensta-bretagne.fr)
 *         Created on 10/03/16.
 */
public class StateEventModel {
	public int nbConfigurations;
	public int nbTransitions;
	public int nbEvents;
	public int nbVariables;

	public Map<String, Integer> variables;
	public Map<String, Integer> events;
	public Map<Integer, String> idx2events;
	public int initialConfiguration;
	public int configurations[][];
	public Map<Integer, Collection<StateEventTransition>> fanout; //an entry in the array for fanout of each state

	public Collection<StateEventTransition> getFireableTransitions(int stateID) {
		return fanout.get(stateID);
	}

	public void link() {
		idx2events = new HashMap<>();
		for (Map.Entry<String, Integer> entry : events.entrySet()) {
			idx2events.put(entry.getValue(), entry.getKey());
		}
	}
}
