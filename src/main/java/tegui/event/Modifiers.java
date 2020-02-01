package tegui.event;

public class Modifiers {

    private static final int ALT_MASK = 1;
    private static final int SHIFT_MASK = 1 << 1;
    private static final int CTRL_MASK = 1 << 2;

    private int modifiers = 0;

    public Modifiers() {

    }

    public void altDown() {
        modifiers |= ALT_MASK;
    }

    public void shiftDown() {
        modifiers |= SHIFT_MASK;
    }

    public void ctrlDown() {
        modifiers |= CTRL_MASK;
    }

    public boolean isAltDown() {
        return (modifiers & ALT_MASK) != 0;
    }

    public boolean isShiftDown() {
        return (modifiers & SHIFT_MASK) != 0;
    }

    public boolean isCtrlDown() {
        return (modifiers & CTRL_MASK) != 0;
    }
}
