package obp2.language.state_event;

import obp2.runtime.core.ILanguageModule;
import obp2.runtime.core.ILanguagePlugin;
import obp2.runtime.core.LanguageModule;
import obp2.language.state_event.diagnosis.SEAtomicPropositionsEvaluator;
import obp2.language.state_event.model.StateEventDenseReader;
import obp2.language.state_event.model.StateEventModel;
import obp2.language.state_event.model.StateEventTransition;
import obp2.language.state_event.runtime.StateEventConfiguration;
import obp2.language.state_event.runtime.StateEventTransitionRelation;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.function.Function;

/**
 * Created by Ciprian TEODOROV on 03/03/17.
 */
public class StateEventPlugin implements ILanguagePlugin<URI, StateEventConfiguration, StateEventTransition, Void> {

    @Override
    public String[] getExtensions() {
        return new String[]{".sek"};
    }

    @Override
    public Function<URI, ILanguageModule<StateEventConfiguration, StateEventTransition, Void>> languageModuleFunction() {
        return this::getModule;
    }

    public ILanguageModule<StateEventConfiguration, StateEventTransition, Void> getModule(URI explicitProgramURI) {
        return getModule(new File(explicitProgramURI));
    }

    public ILanguageModule<StateEventConfiguration, StateEventTransition, Void> getModule(String explicitProgramFileName) {
        return getModule(new File(explicitProgramFileName));
    }

    public ILanguageModule<StateEventConfiguration, StateEventTransition, Void> getModule(File programFile) {
        StateEventModel model = loadModel(programFile);
        StateEventTransitionRelation transitionRelation = new StateEventTransitionRelation(programFile.getName(), model);

        return new LanguageModule<>(transitionRelation, new SEAtomicPropositionsEvaluator(model), new StateEventRuntimeView(transitionRelation));
    }

    public StateEventModel loadModel(File modelFile) {
        try {
            StateEventDenseReader reader = new StateEventDenseReader();
            return reader.read(modelFile);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getName() {
        return "StateEvent";
    }

}
