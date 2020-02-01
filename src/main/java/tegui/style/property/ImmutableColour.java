package tegui.style.property;

import maths.joml.Vector3f;
import maths.joml.Vector4f;
import maths.utils.Vector3;
import maths.utils.Vector4;

public class ImmutableColour implements IColour {

    private final Vector3f rgbColour = Vector3.create();
    private final Vector4f rgbaColour = Vector4.create();

    public final float r;
    public final float g;
    public final float b;
    public final float a;

    public ImmutableColour(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    @Override
    public Vector3f rgbVector() {
        return rgbColour.set(r, g, b);
    }

    @Override
    public Vector4f rgbaVector() {
        return rgbaColour.set(r, g, b, a);
    }

    @Override
    public float getRed() {
        return r;
    }

    @Override
    public float getGreen() {
        return g;
    }

    @Override
    public float getBlue() {
        return b;
    }

    @Override
    public float getAlpha() {
        return a;
    }

    @Override
    public String toString() {
        return string();
    }
}