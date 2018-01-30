package plug.language.state_event;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import plug.core.ILanguageLoader;
import plug.core.ILanguageRuntime;
import plug.language.state_event.diagnosis.SEAtomicPropositionsEvaluator;
import plug.language.state_event.model.StateEventDenseReader;
import plug.language.state_event.model.StateEventModel;
import plug.language.state_event.runtime.StateEventRuntime;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Map;

/**
 * @author Ciprian Teodorov (ciprian.teodorov@ensta-bretagne.fr)
 *         Created on 10/03/16.
 */
public class StateEventLoader implements ILanguageLoader {

    //TODO: these functions are development helpers
    @Deprecated
    public static ILanguageRuntime loadRuntime(File modelFile) {
        return (new StateEventLoader())._loadRuntime(modelFile);
    }

    @Deprecated
    public static StateEventModel load(File modelFile) {
        return (new StateEventLoader()).loadModel(modelFile);
    }

    public ILanguageRuntime _loadRuntime(File modelFile) {
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
    public ILanguageRuntime getRuntime(URI modelURI, Map<String, Object> options) {
        return _loadRuntime(new File(modelURI));
    }
}
