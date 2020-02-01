package tegui.event;

import glfw.input.MouseButton;
import glfw.input.event.ClickEvent;
import glfw.input.event.MoveEvent;
import glfw.window.Window;

public class MouseEvent {

    private final MouseButton button;
    private final Modifiers modifiers;

    private final int x;
    private final int y;
    private final int xBefore;
    private final int yBefore;
    private final int xOnScreen;
    private final int yOnScreen;

    public MouseEvent(MouseButton button, Modifiers modifiers, int x, int y, int xBefore, int yBefore, int xOnScreen, int yOnScreen) {
        this.button = button;
        this.modifiers = modifiers;
        this.x = x;
        this.y = y;
        this.xBefore = xBefore;
        this.yBefore = yBefore;
        this.xOnScreen = xOnScreen;
        this.yOnScreen = yOnScreen;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static MouseEvent create(ClickEvent event) {
        return MouseEvent.builder()
                .setX(event.getMouse().getXPos())
                .setY(event.getMouse().getYPos())
                .setxBefore(event.getMouse().getXPos())
                .setyBefore(event.getMouse().getYPos())
                .setButton(event.getButton())
                .create();
    }

    public static MouseEvent create(MoveEvent event) {
        return MouseEvent.builder()
                .setX((int) event.getX())
                .setY((int) event.getY())
                .setButton(event.getMouse().isButtonDown(MouseButton.PRIMARY) ? MouseButton.PRIMARY : MouseButton.NONE)
                .create();
    }

    public MouseButton getButton() {
        return button;
    }

    public Modifiers getModifiers() {
        return modifiers;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getxBefore() {
        return xBefore;
    }

    public int getyBefore() {
        return yBefore;
    }

    public int getDeltaX() {
        return x - xBefore;
    }

    public int getDeltaY() {
        return y - yBefore;
    }

    public int getxOnScreen() {
        return xOnScreen;
    }

    public int getyOnScreen() {
        return yOnScreen;
    }

    public static class Builder {

        private MouseButton button = MouseButton.NONE;
        private Modifiers modifiers = new Modifiers();

        private int x;
        private int y;
        private int xBefore;
        private int yBefore;

        public Builder setButton(MouseButton button) {
            this.button = button;
            return this;
        }

        public Builder setModifiers(Modifiers modifiers) {
            this.modifiers = modifiers;
            return this;
        }

        public Builder setX(int x) {
            this.x = x;
            return this;
        }

        public Builder setY(int y) {
            this.y = y;
            return this;
        }

        public Builder setxBefore(int xBefore) {
            this.xBefore = xBefore;
            return this;
        }

        public Builder setyBefore(int yBefore) {
            this.yBefore = yBefore;
            return this;
        }

        public MouseEvent create() {
            return new MouseEvent(button, modifiers, x, y, xBefore, yBefore, x + Window.current().getX(), y + Window.current().getY());
        }
    }

}
