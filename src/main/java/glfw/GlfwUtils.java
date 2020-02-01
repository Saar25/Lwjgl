package glfw;

import org.lwjgl.glfw.GLFW;

public final class GlfwUtils {

    private GlfwUtils() {
        throw new UnsupportedOperationException("Cannot make instance of GlfwUtils class");
    }

    public static void init() {
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
    }

}
