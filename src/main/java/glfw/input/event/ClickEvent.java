package glfw.input.event;

import glfw.input.Mouse;
import glfw.input.MouseButton;

public class ClickEvent extends MouseEvent {

    private final MouseButton button;
    private final boolean isDown;

    public ClickEvent(Mouse mouse, MouseButton button, boolean isDown) {
        super(mouse);
        this.button = button;
        this.isDown = isDown;
    }

    public MouseButton getButton() {
        return button;
    }

    public boolean isDown() {
        return isDown;
    }
}
