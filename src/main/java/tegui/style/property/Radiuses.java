package tegui.style.property;

import tegui.style.Style;

public class Radiuses extends AbstractFloatCorners implements FloatCorners {

    public Radiuses() {

    }

    public Radiuses(Style origin) {
        super(origin);
    }

    private float clamp(float radius) {
        if (origin != null) {
            final Dimensions d = origin.dimensions;
            return Math.min(Math.min(radius, d.getWidth() / 2f), d.getHeight() / 2f);
        }
        return radius;
    }

    @Override
    public float getTopLeft() {
        return clamp(super.getTopLeft());
    }

    @Override
    public float getTopRight() {
        return clamp(super.getTopRight());
    }

    @Override
    public float getBottomRight() {
        return clamp(super.getBottomRight());
    }

    @Override
    public float getBottomLeft() {
        return clamp(super.getBottomLeft());
    }
}
