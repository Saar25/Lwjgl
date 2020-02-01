package glfw.input;

import org.lwjgl.glfw.GLFW;

public enum MouseButton {

    NONE(-1),

    BTN_1(GLFW.GLFW_MOUSE_BUTTON_1), // PRIMARY
    BTN_2(GLFW.GLFW_MOUSE_BUTTON_2), // SECONDARY
    BTN_3(GLFW.GLFW_MOUSE_BUTTON_3), // MIDDLE
    BTN_4(GLFW.GLFW_MOUSE_BUTTON_4),
    BTN_5(GLFW.GLFW_MOUSE_BUTTON_5),
    BTN_6(GLFW.GLFW_MOUSE_BUTTON_6),
    BTN_7(GLFW.GLFW_MOUSE_BUTTON_7),
    BTN_8(GLFW.GLFW_MOUSE_BUTTON_8), // LAST
    ;

    public static final MouseButton PRIMARY = BTN_1;
    public static final MouseButton SECONDARY = BTN_2;
    public static final MouseButton MIDDLE = BTN_3;
    public static final MouseButton LAST = BTN_8;

    private final int value;

    MouseButton(int value) {
        this.value = value;
    }

    public static MouseButton valueOf(int button) {
        switch (button) {
            case 0: return BTN_1;
            case 1: return BTN_2;
            case 2: return BTN_3;
            case 3: return BTN_4;
            case 4: return BTN_5;
            case 5: return BTN_6;
            case 6: return BTN_7;
            case 7: return BTN_8;
            default: return NONE;
        }
    }

    public int get() {
        return value;
    }

    public boolean isPrimary() {
        return this == PRIMARY;
    }

    public boolean isSecondary() {
        return this == SECONDARY;
    }

}
