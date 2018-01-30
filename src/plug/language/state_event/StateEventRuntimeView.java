package plug.language.state_event;

import javafx.scene.control.TreeItem;
import plug.core.IRuntimeView;
import plug.language.state_event.model.StateEventTransition;
import plug.language.state_event.runtime.StateEventConfiguration;
import plug.language.state_event.runtime.StateEventRuntime;

import javax.swing.tree.TreeNode;
import java.util.Map;

/**
 * Created by Ciprian TEODOROV on 03/03/17.
 */
public class StateEventRuntimeView implements IRuntimeView<StateEventConfiguration, StateEventTransition> {
    private final StateEventRuntime runtime;

    public StateEventRuntimeView(StateEventRuntime runtime) {
        this.runtime = runtime;
    }

    @Override
    public StateEventRuntime getRuntime() {
        return runtime;
    }

    @Override
    public TreeNode getConfigurationTreeModel(StateEventConfiguration value) {
        return null;
    }

    @Override
    public TreeItem getConfigurationTreeModelFx(StateEventConfiguration value) {
        TreeItem tree = new TreeItem("explicit");
        for (Map.Entry<String, Integer> entry : runtime.program.variables.entrySet()) {
            TreeItem var = new TreeItem(entry.getKey() + " = " + value.values[entry.getValue()]);
            tree.getChildren().add(var);
        }
        return tree;
    }

    @Override
    public TreeNode getFireableTransitionTreeModel(StateEventTransition transition) {
        return null;
    }
}
