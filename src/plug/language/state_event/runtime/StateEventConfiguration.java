package plug.language.state_event.runtime;

import plug.core.IConfiguration;

import java.util.Arrays;

public class StateEventConfiguration implements IConfiguration<StateEventConfiguration> {
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
