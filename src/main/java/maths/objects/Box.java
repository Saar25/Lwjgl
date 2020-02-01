package maths.objects;

import maths.utils.Maths;
import maths.utils.Vector3;
import maths.joml.Vector3fc;

public class Box {

    public static final Box NONE = Box.of(0, 0, 0);
    public static final Box BASIC = Box.of(1, 1, 1);

    private final Vector3fc min;
    private final Vector3fc max;

    public Box(Vector3fc min, Vector3fc max) {
        this.min = min;
        this.max = max;
    }

    public static Box of(float w, float h, float d) {
        final Vector3fc min = Vector3.of(0, 0, 0);
        final Vector3fc max = Vector3.of(w, h, d);
        return new Box(min, max);
    }

    public static Box of(Vector3fc min, Vector3fc max) {
        return new Box(min, max);
    }

    public static Box of(float nx, float ny, float nz, float mx, float my, float mz) {
        final Vector3fc min = Vector3.of(nx, ny, nz);
        final Vector3fc max = Vector3.of(mx, my, mz);
        return new Box(min, max);
    }

    public float getWidth() {
        return getMax().x() - getMin().x();
    }

    public float getHeight() {
        return getMax().y() - getMin().y();
    }

    public float getDepth() {
        return getMax().z() - getMin().z();
    }

    public float w() {
        return getMax().x() - getMin().x();
    }

    public float h() {
        return getMax().y() - getMin().y();
    }

    public float d() {
        return getMax().z() - getMin().z();
    }

    public Vector3fc getMin() {
        return min;
    }

    public Vector3fc getMax() {
        return max;
    }

    public boolean intersects(Vector3fc point) {
        return intersects(Vector3.ZERO, point);
    }

    public boolean intersects(Vector3fc position, Vector3fc point) {
        return  Maths.isBetween(point.x(), position.x() - getMin().x(), position.x() + getMax().x()) &&
                Maths.isBetween(point.y(), position.y() - getMin().y(), position.y() + getMax().y()) &&
                Maths.isBetween(point.z(), position.z() - getMin().z(), position.z() + getMax().z());
    }
}
