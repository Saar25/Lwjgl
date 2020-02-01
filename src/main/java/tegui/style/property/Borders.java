package tegui.style.property;

import maths.joml.Vector4f;
import maths.utils.Vector4;
import tegui.style.Style;
import tegui.style.AbstractStyleProperty;

public class Borders extends AbstractStyleProperty {

    private final Vector4f vector = Vector4.create();

    public float top = 0;
    public float right = 0;
    public float bottom = 0;
    public float left = 0;

    public Borders() {

    }

    public Borders(float all) {
        this(all, all, all, all);
    }

    public Borders(float top, float right, float bottom, float left) {
        this.set(top, right, bottom, left);
    }

    public Borders(Style origin) {
        super(origin);
    }

    public Borders(Style origin, float top, float right, float bottom, float left) {
        super(origin);
        this.set(top, right, bottom, left);
    }

    public void set(float top, float right, float bottom, float left) {
        super.setInherited(false);
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    public void set(float top, float rightLeft, float bottom) {
        this.set(top, rightLeft, bottom, rightLeft);
    }

    public void set(float topBottom, float rightLeft) {
        this.set(topBottom, rightLeft, topBottom, rightLeft);
    }

    public void set(float all) {
        this.set(all, all, all, all);
    }

    public void set(Borders borders) {
        this.set(borders.top, borders.right, borders.bottom, borders.left);
    }

    public Vector4f asVector() {
        return vector.set(left, top, right, bottom);
    }

    @Override
    public String toString() {
        return String.format("[Borders: top=%f, right=%f, bottom=%f, left=%f]", top, right, bottom, left);
    }
}
