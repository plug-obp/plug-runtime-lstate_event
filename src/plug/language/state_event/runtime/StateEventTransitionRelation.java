package plug.language.state_event.runtime;

import plug.runtime.core.IAtomicPropositionsEvaluator;
import plug.runtime.core.IConcurrentTransitionRelation;
import plug.core.IFiredTransition;
import plug.language.state_event.diagnosis.SEAtomicPropositionsEvaluator;
import plug.language.state_event.model.StateEventModel;
import plug.language.state_event.model.StateEventTransition;
import plug.statespace.transitions.FiredTransition;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class StateEventTransitionRelation implements IConcurrentTransitionRelation<StateEventTransitionRelation, StateEventConfiguration, StateEventTransition> {
	public StateEventModel program;

	//TODO: the @SEAtomicPropositionsEvaluator should not be in this class
	public SEAtomicPropositionsEvaluator atomicPropositionsEvaluator;
	//TODO: the @getAtomicPropositionEvaluator should not be in this class
	@Override
	public IAtomicPropositionsEvaluator getAtomicPropositionEvaluator() {
		return atomicPropositionsEvaluator;
	}

	@Override
	public StateEventTransitionRelation createCopy() {
		return new StateEventTransitionRelation();
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
	public IFiredTransition<StateEventConfiguration, ?> fireOneTransition(StateEventConfiguration source, StateEventTransition transition) {
		//create the new configuration
		StateEventConfiguration target = new StateEventConfiguration();
		target.id 	  = transition.target;
		target.values = program.configurations[transition.target];

		return new FiredTransition<>(source, target, transition);
	}
}
