package plug.language.state_event;

import plug.runtime.core.ILanguageLoader;
import plug.runtime.core.ILanguagePlugin;
import plug.runtime.core.v0.IRuntimeView;
import plug.language.state_event.runtime.StateEventTransitionRelation;

/**
 * Created by Ciprian TEODOROV on 03/03/17.
 */
public class StateEventPlugin implements ILanguagePlugin<StateEventTransitionRelation> {
    StateEventLoader loader = new StateEventLoader();

    @Override
    public String[] getExtensions() {
        return new String[] {".sek"};
    }

    @Override
    public String getName() {
        return "StateEvent";
    }

    @Override
    public ILanguageLoader getLoader() {
        return loader;
    }

    @Override
    public IRuntimeView getRuntimeView(StateEventTransitionRelation runtime) {
        return new StateEventRuntimeView(runtime);
    }
}
