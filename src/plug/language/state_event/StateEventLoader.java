package plug.language.state_event;

import java.io.File;
import java.net.URI;
import java.util.Map;
import plug.core.ILanguageLoader;
import plug.core.ITransitionRelation;
import plug.language.state_event.diagnosis.SEAtomicPropositionsEvaluator;
import plug.language.state_event.model.StateEventDenseReader;
import plug.language.state_event.model.StateEventModel;
import plug.language.state_event.runtime.StateEventRuntime;

/**
 * @author Ciprian Teodorov (ciprian.teodorov@ensta-bretagne.fr)
 *         Created on 10/03/16.
 */
public class StateEventLoader implements ILanguageLoader<StateEventRuntime> {

    //TODO: these functions are development helpers
    @Deprecated
    public static ITransitionRelation loadRuntime(File modelFile) {
        return (new StateEventLoader())._loadRuntime(modelFile);
    }

    @Deprecated
    public static StateEventModel load(File modelFile) {
        return (new StateEventLoader()).loadModel(modelFile);
    }

    public StateEventRuntime _loadRuntime(File modelFile) {
        StateEventRuntime runtime = new StateEventRuntime();
        runtime.program = loadModel(modelFile);
        runtime.atomicPropositionsEvaluator = new SEAtomicPropositionsEvaluator(runtime.program);
        return runtime;
    }
    
    public StateEventModel loadModel(File modelFile) {
        StateEventDenseReader reader = new StateEventDenseReader();
        return reader.read(modelFile);
    }

    @Override
    public StateEventRuntime getRuntime(URI modelURI, Map<String, Object> options) {
        return _loadRuntime(new File(modelURI));
    }
}
