package tegui.style.property;

import maths.joml.Matrix4f;
import maths.joml.Vector4i;
import maths.utils.Matrix4;
import tegui.style.Style;
import tegui.style.AbstractStyleProperty;

public class CornersColours extends AbstractStyleProperty {

    private final Matrix4f matrix = Matrix4.create();

    public final Colour topLeft = new Colour();
    public final Colour topRight = new Colour();
    public final Colour bottomRight = new Colour();
    public final Colour bottomLeft = new Colour();

    public CornersColours() {

    }

    public CornersColours(Colour all) {
        this(all, all, all, all);
    }

    public CornersColours(IColour topLeft, IColour topRight, IColour bottomRight, IColour bottomLeft) {
        this.set(topLeft, topRight, bottomRight, bottomLeft);
    }

    public CornersColours(Style origin) {
        super(origin);
    }

    public void set(IColour topLeft, IColour topRight, IColour bottomRight, IColour bottomLeft) {
        super.setInherited(false);
        this.topLeft.set(topLeft);
        this.topRight.set(topRight);
        this.bottomRight.set(bottomRight);
        this.bottomLeft.set(bottomLeft);
    }

    public void set(IColour topLeft, IColour sides, IColour bottomRight) {
        this.set(topLeft, sides, bottomRight, sides);
    }

    public void set(IColour topLeftBottomRight, IColour topRightBottomLeft) {
        this.set(topLeftBottomRight, topRightBottomLeft, topLeftBottomRight, topRightBottomLeft);
    }

    public void set(IColour a, IColour b, Orientation orientation) {
        switch (orientation) {
            case VERTICAL:
                this.set(a, a, b, b);
                break;
            case HORIZONTAL:
                this.set(a, b, b, a);
                break;
        }
    }

    public void set(IColour all) {
        this.set(all, all, all, all);
    }

    public void set(CornersColours corners) {
        this.set(corners.topLeft, corners.topRight, corners.bottomRight, corners.bottomLeft);
    }

    public Colour get() {
        return topRight;
    }

    public Colour getTopLeft() {
        return topLeft;
    }

    public Colour getTopRight() {
        return topRight;
    }

    public Colour getBottomRight() {
        return bottomRight;
    }

    public Colour getBottomLeft() {
        return bottomLeft;
    }

    public void setRGB(int rgb) {
        float r = ((rgb >> 16) & 255) / 255f;
        float g = ((rgb >> 8) & 255) / 255f;
        float b = ((rgb) & 255) / 255f;
        this.setNormalized(r, g, b, 1);
    }

    public void setRGBA(int rgba) {
        float r = ((rgba >> 24) & 255) / 255f;
        float g = ((rgba >> 16) & 255) / 255f;
        float b = ((rgba >> 8) & 255) / 255f;
        float a = ((rgba) & 255) / 255f;
        this.setNormalized(r, g, b, a);
    }

    public void set(float r, float g, float b, float a, boolean normalized) {
        if (normalized) {
            this.setNormalized(r, g, b, a);
        } else {
            this.set((int) r, (int) g, (int) b, (int) a);
        }
    }

    public void set(float r, float g, float b, boolean normalized) {
        if (normalized) {
            this.setNormalized(r, g, b);
        } else {
            this.set((int) r, (int) g, (int) b);
        }
    }

    public void set(int r, int g, int b, int a) {
        super.setInherited(false);
        this.topLeft.set(r, g, b, a);
        this.topRight.set(r, g, b, a);
        this.bottomRight.set(r, g, b, a);
        this.bottomLeft.set(r, g, b, a);
    }

    public void set(int r, int g, int b) {
        this.set(r, g, b, 255);
    }

    public void setNormalized(float r, float g, float b, float a) {
        super.setInherited(false);
        this.topLeft.setNormalized(r, g, b, a);
        this.topRight.setNormalized(r, g, b, a);
        this.bottomRight.setNormalized(r, g, b, a);
        this.bottomLeft.setNormalized(r, g, b, a);
    }

    public void setNormalized(float r, float g, float b) {
        this.setNormalized(r, g, b, 1);
    }

    public Matrix4f asMatrix() {
        return matrix.set(bottomLeft.rgbaVector(), topLeft.rgbaVector(),
                bottomRight.rgbaVector(), topRight.rgbaVector());
    }

    public Vector4i asIVec4() {
        return new Vector4i(bottomLeft.asInt(), topLeft.asInt(),
                bottomRight.asInt(), topRight.asInt());
    }

    @Override
    public String toString() {
        return String.format("[Corners: topLeft=%s, topRight=%s, bottomRight=%s, bottomLeft=%s]",
                topLeft, topRight, bottomRight, bottomLeft);
    }

}
