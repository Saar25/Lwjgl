package tegui.style.property;

import maths.joml.Vector2f;
import maths.utils.Vector2;
import tegui.style.Style;

public class Dimensions {

    private final Vector2f vector = Vector2.create();

    private final Length width;
    private final Length height;

    public Dimensions(Style origin) {
        this.width = origin.width;
        this.height = origin.height;
    }

    public void add(int width, int height) {
        this.width.add(width);
        this.height.add(height);
    }

    public void setWidth(int width) {
        this.width.set(width);
    }

    public void setHeight(int height) {
        this.height.set(height);
    }

    public void set(int size) {
        this.width.set(size);
        this.height.set(size);
    }

    public void set(int width, int height) {
        this.width.set(width);
        this.height.set(height);
    }

    public void set(Dimensions dimensions) {
        this.width.set(dimensions.width.get());
        this.height.set(dimensions.height.get());
    }

    public int getWidth() {
        return this.width.get();
    }

    public int getHeight() {
        return this.height.get();
    }

    public Vector2f asVector() {
        return vector.set(getWidth(), getHeight());
    }
}
