package tegui.style.property;

import maths.joml.Vector4f;
import maths.utils.Vector4;
import tegui.style.AbstractStyleProperty;
import tegui.style.Style;

public abstract class AbstractFloatCorners extends AbstractStyleProperty implements FloatCorners {

    private final Vector4f vector = Vector4.create();

    public float topLeft = 0;
    public float topRight = 0;
    public float bottomRight = 0;
    public float bottomLeft = 0;

    protected AbstractFloatCorners() {

    }

    protected AbstractFloatCorners(Style origin) {
        super(origin);
    }

    public void set(float topLeft, float topRight, float bottomRight, float bottomLeft) {
        super.setInherited(false);
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomRight = bottomRight;
        this.bottomLeft = bottomLeft;
    }

    public void set(float topLeft, float sides, float bottomRight) {
        this.set(topLeft, sides, bottomRight, sides);
    }

    public void set(float topLeftBottomRight, float topRightBottomLeft) {
        this.set(topLeftBottomRight, topRightBottomLeft, topLeftBottomRight, topRightBottomLeft);
    }

    public void set(float all) {
        this.set(all, all, all, all);
    }

    public void set(FloatCorners corners) {
        this.set(corners.getTopLeft(), corners.getTopRight(),
                corners.getBottomRight(), corners.getBottomLeft());
    }

    public boolean isZero() {
        return getTopLeft() <= 0 && getTopRight() <= 0 &&
                getBottomRight() <= 0 && getBottomLeft() <= 0;
    }

    public float get() {
        return getTopRight();
    }

    @Override
    public float get(boolean right, boolean top) {
        if (top) {
            return right ? getTopRight() : getTopLeft();
        } else {
            return right ? getBottomRight() : getBottomLeft();
        }
    }

    @Override
    public float getTopLeft() {
        return topLeft;
    }

    @Override
    public float getTopRight() {
        return topRight;
    }

    @Override
    public float getBottomRight() {
        return bottomRight;
    }

    @Override
    public float getBottomLeft() {
        return bottomLeft;
    }

    @Override
    public Vector4f asVector() {
        return vector.set(getTopLeft(), getTopRight(), getBottomRight(), getBottomLeft());
    }

    @Override
    public String toString() {
        return String.format("[Corners: topLeft=%f, topRight=%f, bottomRight=%f, bottomLeft=%f]",
                getTopLeft(), getTopRight(), getBottomRight(), getBottomLeft());
    }
}
