package glfw.window;

import glfw.input.Keyboard;
import glfw.input.Mouse;
import opengl.utils.MemoryUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Window {

    private static Window current = null;

    private final IntBuffer xPos = MemoryUtils.allocInt(1);
    private final IntBuffer yPos = MemoryUtils.allocInt(1);

    private final String title;
    private long id;
    private int width;
    private int height;

    private Mouse mouse;
    private Keyboard keyboard;

    private boolean resized;
    private boolean vSync;

    public Window(String title, int width, int height, boolean vSync) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;
        this.resized = false;
        Window.current = this;
    }

    /**
     * Returns the current focused window
     *
     * @return the current window
     */
    public static Window current() {
        return current;
    }

    public void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        setHint(WindowHint.VISIBLE, false); // the window will stay hidden after creation
        setHint(WindowHint.RESIZABLE, true); // the window will be resizable
        setHint(WindowHint.CONTEXT_VERSION_MAJOR, 3);
        setHint(WindowHint.CONTEXT_VERSION_MINOR, 2);
        setHint(WindowHint.OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        setHint(WindowHint.OPENGL_FORWARD_COMPAT, true);
        //setHint(WindowHint.MAXIMIZED, true);

        // Create the window
        id = glfwCreateWindow(width, height, title, 0, 0);
        if (id == 0) {
            throw new RuntimeException("Failed to init the GLFW window");
        }

        // Setup resize callback
        glfwSetFramebufferSizeCallback(id, (window, width, height) -> {
            this.width = width;
            this.height = height;
            this.resized = true;
        });

        // Get the resolution of the primary monitor
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        if (vidMode != null) {
            glfwSetWindowPos(id,
                    (vidMode.width() - width) / 2,
                    (vidMode.height() - height) / 2
            );
        }

        // Make the OpenGL context current
        makeContextCurrent();

        if (isvSync()) {
            // Enable v-sync
            glfwSwapInterval(1);
        }

        // Make the window visible
        setVisible(true);

        GL.createCapabilities();

        this.mouse = new Mouse(id);
        this.keyboard = new Keyboard(id);
    }

    private void setHint(WindowHint hint, int value) {
        GLFW.glfwWindowHint(hint.get(), value);
    }

    private void setHint(WindowHint hint, boolean value) {
        GLFW.glfwWindowHint(hint.get(), value ? 1 : 0);
    }

    private void makeContextCurrent() {
        GLFW.glfwMakeContextCurrent(id);
    }

    private void setVisible(boolean visable) {
        if (visable) {
            show();
        } else {
            hide();
        }
    }

    private void show() {
        GLFW.glfwShowWindow(id);
    }

    private void hide() {
        GLFW.glfwHideWindow(id);
    }

    /**
     * Creates the mouse that corresponds to this window
     *
     * @return the mouse
     */
    public Mouse getMouse() {
        return mouse;
    }

    /**
     * Returns the keyboard that corresponds to this window
     *
     * @return the keyboard
     */
    public Keyboard getKeyboard() {
        return keyboard;
    }

    /**
     * Returns whether the window has been closed by the user
     *
     * @return true if window has been close else false
     */
    public boolean windowShouldClose() {
        return glfwWindowShouldClose(id);
    }

    /**
     * Returns whether the window has been closed by the user
     *
     * @return true if window has been close else false
     */
    public boolean isOpen() {
        return !glfwWindowShouldClose(id);
    }

    /**
     * Returns the window's title
     *
     * @return the window's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Return the window's width
     *
     * @return the window's width
     */
    public int getX() {
        glfwGetWindowPos(id, xPos, yPos);
        int x = xPos.get();
        xPos.flip();
        return x;
    }

    /**
     * Return the window's height
     *
     * @return the window's height
     */
    public int getY() {
        glfwGetWindowPos(id, xPos, yPos);
        int y = yPos.get();
        yPos.flip();
        return y;
    }

    /**
     * Return the window's width
     *
     * @return the window's width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Return the window's height
     *
     * @return the window's height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns whether the window is vSync
     *
     * @return true if window is vSync else false
     */
    public boolean isvSync() {
        return vSync;
    }

    /**
     * Returns whether the window has been resized by the user since last updated
     *
     * @return true if window has been resized else false
     */
    public boolean isResized() {
        return resized;
    }

    /**
     * Sets the window should close flag. Used for closing up the program
     *
     * @param shouldClose true if wants the window to close else false
     */
    public void setWindowShouldClose(boolean shouldClose) {
        glfwSetWindowShouldClose(id, shouldClose);
    }

    /**
     * Updates the window
     */
    public void update(boolean swapBuffers) {
        if (swapBuffers) {
            swapBuffers();
        }
        resized = false;
        Window.current = this;
    }

    public void swapBuffers() {
        GLFW.glfwSwapBuffers(id);
    }

    public void pollEvents() {
        GLFW.glfwPollEvents();
    }

    public void waitEvents() {
        GLFW.glfwWaitEvents();
    }
}
