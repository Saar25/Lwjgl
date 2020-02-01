package tegui.style.property;

import tegui.style.Style;

public class Corners extends AbstractFloatCorners implements FloatCorners {

    public Corners() {

    }

    public Corners(float all) {
        this(all, all, all, all);
    }

    public Corners(float topLeft, float topRight, float bottomRight, float bottomLeft) {
        this.set(topLeft, topRight, bottomRight, bottomLeft);
    }

    public Corners(Style origin) {
        super(origin);
    }

}
