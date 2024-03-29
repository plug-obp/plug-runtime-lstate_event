package obp2.language.state_event.runtime;

import obp2.core.defaults.DefaultConfiguration;

import java.util.Arrays;

public class StateEventConfiguration extends DefaultConfiguration<StateEventConfiguration> {
	int id; //the state id is here only to simplify the getFireableTransitions
	public int[] values;
	
	@Override
	public StateEventConfiguration createCopy() {
		StateEventConfiguration newC = new StateEventConfiguration();
		newC.id = id;
		newC.values = Arrays.copyOf(values, values.length);
		return newC;
	}
	
	@Override
	public int hashCode() {
		return values.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj instanceof StateEventConfiguration) {
			StateEventConfiguration other = (StateEventConfiguration) obj;
			return Arrays.equals(values, other.values);
		}
		return false;
	}

	@Override
	public String toString() {
		return Arrays.toString(values);
	}
}
