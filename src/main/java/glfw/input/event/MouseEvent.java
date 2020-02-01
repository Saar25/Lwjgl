package glfw.input.event;

import glfw.input.Mouse;

public class MouseEvent extends Event {

    private final Mouse mouse;

    public MouseEvent(Mouse mouse) {
        this.mouse = mouse;
    }

    public Mouse getMouse() {
        return mouse;
    }
}
