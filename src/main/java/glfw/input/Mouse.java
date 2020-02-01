package glfw.input;

import engine.util.ModifiableList;
import glfw.input.event.*;
import opengl.utils.MemoryUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mouse {

    private static final DoubleBuffer xPos = MemoryUtils.allocDouble(1);
    private static final DoubleBuffer yPos = MemoryUtils.allocDouble(1);

    private final long window;

    private final List<MouseButton> pressedButtons = new ArrayList<>();
    private final List<MouseButton> pressedButtonsUnmodifiable = Collections.unmodifiableList(pressedButtons);

    private final List<EventListener<ScrollEvent>> scrollEventListeners = new ArrayList<>();
    private final List<EventListener<ClickEvent>> clickEventListeners = new ArrayList<>();
    private final List<EventListener<MoveEvent>> moveEventListeners = new ArrayList<>();

    private final ModifiableList<EventListener<ScrollEvent>> scrollEventListenersModifiable = new ModifiableList<>(scrollEventListeners);
    private final ModifiableList<EventListener<ClickEvent>> clickEventListenersModifiable = new ModifiableList<>(clickEventListeners);
    private final ModifiableList<EventListener<MoveEvent>> moveEventListenersModifiable = new ModifiableList<>(moveEventListeners);

    private boolean shown = true;

    public Mouse(long window) {
        this.window = window;
        init();
    }

    public static void cleanUp() {
        MemoryUtil.memFree(xPos);
        MemoryUtil.memFree(yPos);
    }

    public Observer createObserver() {
        Observer observer = new Observer();
        observer.init();
        return observer;
    }

    public void init() {
        GLFW.glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
            final MouseButton mouseButton = MouseButton.valueOf(button);
            if (action == GLFW.GLFW_PRESS) {
                pressedButtons.add(mouseButton);
            } else {
                pressedButtons.remove(mouseButton);
            }

            final ClickEvent event = new ClickEvent(this, mouseButton, action == GLFW.GLFW_PRESS);
            new ArrayList<>(clickEventListeners).forEach(l -> l.actionPerformed(event));
        });
        GLFW.glfwSetCursorPosCallback(window, (window, xPos, yPos) -> {
            final MoveEvent event = new MoveEvent(this, xPos, yPos);
            new ArrayList<>(moveEventListeners).forEach(l -> l.actionPerformed(event));
        });
        GLFW.glfwSetScrollCallback(window, (window, xOffset, yOffset) -> {
            final ScrollEvent event = new ScrollEvent(this, yOffset);
            new ArrayList<>(scrollEventListeners).forEach(l -> l.actionPerformed(event));
        });
    }

    public void show() {
        if (!isShown()) {
            GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
            shown = true;
        }
    }

    public void hide() {
        if (isShown()) {
            GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
            shown = false;
        }
    }

    public boolean isShown() {
        return shown;
    }

    public void setShown(boolean shown) {
        if (shown) {
            show();
        } else {
            hide();
        }
    }

    public int getXPos() {
        GLFW.glfwGetCursorPos(window, xPos, yPos);
        int x = (int) xPos.get();
        xPos.flip();
        return x;
    }

    public int getYPos() {
        GLFW.glfwGetCursorPos(window, xPos, yPos);
        int y = (int) yPos.get();
        yPos.flip();
        return y;
    }

    public boolean isButtonDown(MouseButton button) {
        return GLFW.glfwGetMouseButton(window, button.get()) == GLFW.GLFW_PRESS;
    }

    public MouseButtonState getButtonState(MouseButton button) {
        final int state = GLFW.glfwGetMouseButton(window, button.get());
        return MouseButtonState.valueOf(state);
    }

    public List<MouseButton> getPressedButtons() {
        return pressedButtonsUnmodifiable;
    }

    public ModifiableList<EventListener<ScrollEvent>> scrollEventListeners() {
        return scrollEventListenersModifiable;
    }

    public ModifiableList<EventListener<ClickEvent>> clickEventListeners() {
        return clickEventListenersModifiable;
    }

    public ModifiableList<EventListener<MoveEvent>> moveEventListeners() {
        return moveEventListenersModifiable;
    }

    public OnAction<ClickEvent> onClick(MouseButton button) {
        return listener -> clickEventListeners().add(e -> {
            if (e.getButton() == button) {
                listener.actionPerformed(e);
            }
        });
    }

    public class Observer {

        private double scrollChange = 0;
        private double lastXPos = 0;
        private double lastYPos = 0;
        private double xPos = 0;
        private double yPos = 0;

        private Observer() {
        }

        public void init() {
            Mouse.this.moveEventListeners().add(e -> {
                xPos = e.getX();
                yPos = e.getY();
            });
            Mouse.this.scrollEventListeners().add(
                    e -> scrollChange += e.getOffset());
        }

        public double getDeltaX() {
            return xPos - lastXPos;
        }

        public double getDeltaY() {
            return yPos - lastYPos;
        }

        public void update() {
            lastXPos = xPos;
            lastYPos = yPos;
        }

        public double getScrollChange() {
            double change = scrollChange;
            scrollChange = 0;
            return change;
        }
    }
}
