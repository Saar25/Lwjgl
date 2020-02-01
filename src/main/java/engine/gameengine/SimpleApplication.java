package engine.gameengine;

import engine.rendering.Camera;
import engine.rendering.RenderManager;
import engine.rendering.Scene;
import glfw.input.Keyboard;
import glfw.input.Mouse;
import glfw.window.Window;

public abstract class SimpleApplication implements Application {

    protected final Loader loader;
    protected final Camera camera;
    protected final Scene scene;

    private boolean closed;

    protected SimpleApplication() {
        this.loader = new Loader();
        this.camera = new Camera();
        this.scene = new Scene(camera);
    }

    @Override
    public final void init(Window window, RenderManager renderer, Keyboard keyboard, Mouse mouse) throws Exception {
        mouse.moveEventListeners().add(scene::onMouseMoveEvent);
        mouse.clickEventListeners().add(scene::onMouseClickEvent);
        mouse.scrollEventListeners().add(scene::onMouseScrollEvent);
        onInit(window, renderer, keyboard, mouse);
    }

    @Override
    public final void update(Keyboard keyboard, Mouse mouse) {
        onUpdate(keyboard, mouse);
    }

    @Override
    public final void render(RenderManager renderer) {
        onRender(renderer);
    }

    /**
     * Cleanup the game, delete all the objects
     */
    @Override
    public void cleanUp() {
        this.scene.delete();
        onCleanUp();
    }

    /**
     * Returns whether the game is closed by self shut down
     *
     * @return true if the game is closed, false otherwise
     */
    public boolean isClosed() {
        return closed;
    }

    protected void setClosed(boolean closed) {
        this.closed = closed;
    }

    protected final void closeGame() {
        this.closed = true;
    }

    /**
     * Initialize the game, load all the objects necessary for the game
     *
     * @param window   the window that will be used during the whole run
     * @param renderer the renderer that will be used during the whole run
     * @param keyboard the keyboard, can be initialize be adding event listeners
     * @param mouse    the mouse, can be initialize be adding event listeners
     * @throws Exception loading files might throw some exceptions
     */
    public abstract void onInit(Window window, RenderManager renderer, Keyboard keyboard, Mouse mouse) throws Exception;

    /**
     * Update the game, as well as getting the input
     *
     * @param keyboard keyboard for getting keyboard input
     * @param mouse    mouse for getting mouse input
     */
    public abstract void onUpdate(Keyboard keyboard, Mouse mouse);

    /**
     * Render the scene
     *
     * @param renderer the renderer to use
     */
    public abstract void onRender(RenderManager renderer);

    /**
     * Cleanup the game, delete all the objects
     */
    public void onCleanUp() {

    }
}
