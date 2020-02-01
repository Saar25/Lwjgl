package glfw.input.event;

import glfw.input.Mouse;

public class ScrollEvent extends MouseEvent {

    private final double offset;

    public ScrollEvent(Mouse mouse, double offset) {
        super(mouse);
        this.offset = offset;
    }

    public double getOffset() {
        return offset;
    }
}
