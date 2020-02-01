package engine.rendering;

import java.util.ArrayList;
import java.util.List;

public class GlThread {

    private static final List<Action> actions = new ArrayList<>();

    public static void addAction(Action action) {
        synchronized (actions) {
            actions.add(action);
        }
    }

    public static void act() {
        synchronized (actions) {
            for (Action action : actions) {
                action.act();
            }
        }
    }

    public interface Action {
        void act();
    }
}
