package glfw.input;

import engine.util.ModifiableList;
import glfw.input.event.EventListener;
import glfw.input.event.KeyPressEvent;
import glfw.input.event.KeyReleaseEvent;
import org.lwjgl.glfw.GLFW;

import java.util.LinkedList;
import java.util.List;

public class Keyboard {

    private final long window;

    private List<EventListener<KeyPressEvent>> keyPressEventListeners = new LinkedList<>();
    private List<EventListener<KeyReleaseEvent>> keyReleaseEventListeners = new LinkedList<>();

    private ModifiableList<EventListener<KeyPressEvent>> keyPressModifiable = new ModifiableList<>(keyPressEventListeners);
    private ModifiableList<EventListener<KeyReleaseEvent>> keyReleaseModifiable = new ModifiableList<>(keyReleaseEventListeners);

    public Keyboard(long window) {
        this.window = window;
        init();
    }

    public void init() {
        GLFW.glfwSetKeyCallback(window, (window, key, scanCode, action, mods) -> {
            if (action == GLFW.GLFW_PRESS) {
                final KeyPressEvent event = new KeyPressEvent(key);
                keyPressEventListeners.forEach(l -> l.actionPerformed(event));
            } else if (action == GLFW.GLFW_RELEASE) {
                final KeyReleaseEvent event = new KeyReleaseEvent(key);
                keyReleaseEventListeners.forEach(l -> l.actionPerformed(event));
            }
        });
    }

    public boolean isKeyPressed(int keyCode) {
        return GLFW.glfwGetKey(window, keyCode) == GLFW.GLFW_PRESS;
    }

    public boolean allKeysPressed(int... keyCodes) {
        for (int keyCode : keyCodes) {
            if (!isKeyPressed(keyCode)) {
                return false;
            }
        }
        return true;
    }

    public boolean anyKeyPressed(int... keyCodes) {
        for (int keyCode : keyCodes) {
            if (isKeyPressed(keyCode)) {
                return true;
            }
        }
        return false;
    }

    public ModifiableList<EventListener<KeyPressEvent>> keyPressEventListeners() {
        return keyPressModifiable;
    }

    public ModifiableList<EventListener<KeyReleaseEvent>> keyReleaseEventListeners() {
        return keyReleaseModifiable;
    }

    public OnAction<KeyPressEvent> onKeyPress(char keyChar) {
        return onKeyPress((int) keyChar);
    }

    public OnAction<KeyPressEvent> onKeyPress(int keyCode) {
        return listener -> keyPressEventListeners().add(e -> {
            if (e.getKeyCode() == keyCode) {
                listener.actionPerformed(e);
            }
        });
    }

    public OnAction<KeyReleaseEvent> onKeyRelease(int keyCode) {
        return listener -> keyReleaseEventListeners().add(e -> {
            if (e.getKeyCode() == keyCode) {
                listener.actionPerformed(e);
            }
        });
    }
}
