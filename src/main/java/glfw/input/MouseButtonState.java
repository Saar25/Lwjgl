package glfw.input;

import org.lwjgl.glfw.GLFW;

public enum MouseButtonState {

    RELEASED(GLFW.GLFW_RELEASE),
    PRESSED(GLFW.GLFW_PRESS),
    REPEATED(GLFW.GLFW_REPEAT),
    ;

    private final int value;

    MouseButtonState(int value) {
        this.value = value;
    }

    public int get() {
        return value;
    }

    public static MouseButtonState valueOf(int value) {
        switch (value) {
            case GLFW.GLFW_RELEASE:
                return RELEASED;
            case GLFW.GLFW_PRESS:
                return PRESSED;
            case GLFW.GLFW_REPEAT:
                return REPEATED;
        }
        throw new IllegalArgumentException("MouseButtonState non found: " + value);
    }
}
