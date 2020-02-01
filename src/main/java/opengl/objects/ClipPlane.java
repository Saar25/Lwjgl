package opengl.objects;

import maths.joml.Vector4f;

public class ClipPlane {

    public static final ClipPlane NONE = ClipPlane.of(0, 0, 0, 0);

    private final Vector4f value;

    private ClipPlane(Vector4f value) {
        this.value = value;
    }

    public static ClipPlane of(float a, float b, float c, float d) {
        Vector4f value = new Vector4f(a, b, c, d);
        return new ClipPlane(value);
    }

    public static ClipPlane ofAbove(float height) {
        Vector4f value = new Vector4f(0, +1, 0, -height);
        return new ClipPlane(value);
    }

    public static ClipPlane ofBelow(float height) {
        Vector4f value = new Vector4f(0, -1, 0, +height);
        return new ClipPlane(value);
    }

    public Vector4f getValue() {
        return value;
    }
}