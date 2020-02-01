package engine.gameengine;

import engine.rendering.RenderManager;
import glfw.input.Keyboard;
import glfw.input.Mouse;
import glfw.window.Window;

public interface Application {

    void init(Window window, RenderManager renderer, Keyboard keyboard, Mouse mouse) throws Exception;

    void update(Keyboard keyboard, Mouse mouse);

    void render(RenderManager renderer);

    void cleanUp();

    boolean isClosed();

}
