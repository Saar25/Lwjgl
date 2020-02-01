package glfw.input.event;

public interface EventListener<T extends Event> {

    void actionPerformed(T e);

}
