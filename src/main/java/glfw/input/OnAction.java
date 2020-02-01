package glfw.input;

import glfw.input.event.Event;
import glfw.input.event.EventListener;

public interface OnAction<T extends Event> {

    void perform(EventListener<T> listener);

}
