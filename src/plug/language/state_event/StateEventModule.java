package plug.language.state_event;

import plug.core.ILanguageLoader;
import plug.core.ILanguageModule;
import plug.core.IRuntimeView;
import plug.language.state_event.runtime.StateEventRuntime;

/**
 * Created by Ciprian TEODOROV on 03/03/17.
 */
public class StateEventModule implements ILanguageModule<StateEventRuntime> {
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
    public IRuntimeView getRuntimeView(StateEventRuntime runtime) {
        return new StateEventRuntimeView(runtime);
    }
}
