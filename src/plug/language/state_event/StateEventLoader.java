package plug.language.state_event;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Map;
import plug.core.ILanguageLoader;
import plug.language.state_event.diagnosis.SEAtomicPropositionsEvaluator;
import plug.language.state_event.model.StateEventDenseReader;
import plug.language.state_event.model.StateEventModel;
import plug.language.state_event.runtime.StateEventTransitionRelation;

/**
 * @author Ciprian Teodorov (ciprian.teodorov@ensta-bretagne.fr)
 *         Created on 10/03/16.
 */
public class StateEventLoader implements ILanguageLoader<StateEventTransitionRelation> {

    public StateEventTransitionRelation _loadRuntime(File modelFile) throws IOException {
        StateEventTransitionRelation runtime = new StateEventTransitionRelation();
        runtime.program = loadModel(modelFile);
        runtime.atomicPropositionsEvaluator = new SEAtomicPropositionsEvaluator(runtime.program);
        return runtime;
    }
    
    public StateEventModel loadModel(File modelFile) throws IOException {
        StateEventDenseReader reader = new StateEventDenseReader();
        return reader.read(modelFile);
    }

    @Override
    public StateEventTransitionRelation getRuntime(URI modelURI, Map<String, Object> options) throws IOException {
        return _loadRuntime(new File(modelURI));
    }
}
