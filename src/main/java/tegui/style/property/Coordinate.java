package tegui.style.property;

import engine.util.lengths.ILength;
import engine.util.lengths.Pixels;
import engine.util.lengths.Proportion;
import glfw.window.Window;
import tegui.style.Style;

public abstract class Coordinate {

    private final Style origin;

    private ILength length = Proportion.ZERO;

    private Coordinate(Style origin) {
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

    public void set(Coordinate coordinate) {
        set(length -> coordinate.get());
    }

    public void set(ILength length) {
        this.length = length;
    }

    public int get() {
        return length.proportionTo(proportion()) + parentCoordinates();
    }

    abstract int proportion();

    abstract int parentCoordinates();

    public static class Horizontal extends Coordinate {
        public Horizontal(Style origin) {
            super(origin);
        }

        @Override
        int proportion() {
            if (super.origin.getParent() == null) {
                return Window.current().getWidth();
            }
            return super.origin.getParent().width.get();
        }

        @Override
        int parentCoordinates() {
            return super.origin.getParent() == null ? 0
                    : super.origin.getParent().x.get();
        }
    }

    public static class Vertical extends Coordinate {
        public Vertical(Style origin) {
            super(origin);
        }

        @Override
        int proportion() {
            if (super.origin.getParent() == null) {
                return Window.current().getHeight();
            }
            return super.origin.getParent().height.get();
        }

        @Override
        int parentCoordinates() {
            return super.origin.getParent() == null ? 0
                    : super.origin.getParent().y.get();
        }
    }
}
