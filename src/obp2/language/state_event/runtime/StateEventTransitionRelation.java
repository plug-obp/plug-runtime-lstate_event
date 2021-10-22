package obp2.language.state_event.runtime;

import obp2.core.IFiredTransition;
import obp2.core.defaults.FiredTransition;
import obp2.runtime.core.IConcurrentTransitionRelation;
import obp2.runtime.core.ITransitionRelation;
import obp2.runtime.core.defaults.DefaultLanguageService;
import obp2.language.state_event.model.StateEventModel;
import obp2.language.state_event.model.StateEventTransition;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class StateEventTransitionRelation
		extends DefaultLanguageService<StateEventConfiguration, StateEventTransition, Void>
		implements ITransitionRelation<StateEventConfiguration, StateEventTransition, Void>,
		IConcurrentTransitionRelation<StateEventTransitionRelation, StateEventConfiguration, StateEventTransition, Void> {
	public StateEventModel program;
	public String name;

	public StateEventTransitionRelation(String name, StateEventModel model) {
		this.name = name;
		this.program = model;
	}

	@Override
	public StateEventTransitionRelation createCopy() {
		return new StateEventTransitionRelation(name, program);
	}
	
	@Override
	public Set<StateEventConfiguration> initialConfigurations() {
		StateEventConfiguration config = new StateEventConfiguration();
		config.id = program.initialConfiguration;
		config.values = program.configurations[program.initialConfiguration];
		return Collections.singleton(config);
	}

	@Override
	public Collection<StateEventTransition> fireableTransitionsFrom(StateEventConfiguration source) {
		return program.getFireableTransitions(source.id);
	}

	@Override
	public IFiredTransition<StateEventConfiguration, StateEventTransition, Void> fireOneTransition(StateEventConfiguration source, StateEventTransition transition) {
		//create the new configuration
		StateEventConfiguration target = new StateEventConfiguration();
		target.id 	  = transition.target;
		target.values = program.configurations[transition.target];

		return new FiredTransition(source, target, transition) {
		};
	}
}
