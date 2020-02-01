package glfw.input.event;

public class KeyReleaseEvent extends Event {

    private final int keyCode;

    public KeyReleaseEvent(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }
}
