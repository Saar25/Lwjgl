package tegui.style.property;

import glfw.window.Window;
import tegui.style.Style;
import engine.util.lengths.ILength;
import engine.util.lengths.Pixels;
import engine.util.lengths.Proportion;

public abstract class Length {

    private final Style origin;

    private ILength length = Proportion.ONE;

    private Length(Style origin) {
        this.origin = origin;
    }

    public void add(int length) {
        int pixels = get() + length;
        set(new Pixels(pixels));
    }

    public void sub(int length) {
        int pixels = get() - length;
        set(new Pixels(pixels));
    }

    public void set(int length) {
        set(new Pixels(length));
    }

    public void set(Length length) {
        set(l -> length.get());
    }

    public void set(ILength length) {
        this.length = length;
    }

    public int get() {
        return length.proportionTo(proportion());
    }

    abstract int proportion();

    public static class Horizontal extends Length {
        public Horizontal(Style origin) {
            super(origin);
        }

        @Override
        int proportion() {
            if (super.origin.getParent() == null) {
                return Window.current().getWidth();
            }
            final Length length = super.origin.getParent().width;
            return length != super.origin.width ? length.get() : Window.current().getWidth();
        }
    }

    public static class Vertical extends Length {
        public Vertical(Style origin) {
            super(origin);
        }

        @Override
        int proportion() {
            if (super.origin.getParent() == null) {
                return Window.current().getHeight();
            }
            final Length length = super.origin.getParent().height;
            return length != super.origin.height ? length.get() : Window.current().getHeight();
        }
    }

}
