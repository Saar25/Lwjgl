package tegui.style;

import glfw.window.Window;

public class DefaultStyle extends Style {

    private static final DefaultStyle instance = new DefaultStyle();

    public DefaultStyle() {
        width.set(length -> Window.current().getWidth());
        height.set(length -> Window.current().getHeight());
    }

    public static DefaultStyle getInstance() {
        return instance;
    }

    @Override
    public Style getParent() {
        return null;
    }
}
