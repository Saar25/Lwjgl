package maths.utils;

import maths.joml.*;

public final class Vector4 {

    public static final ObjectPool<Vector4f> pool = new ObjectPool<>(Vector4::create);

    private Vector4() {

    }

    /**
     * Creates a new zero vector
     *
     * @return a new zero Vector4f
     */
    public static Vector4f create() {
        return new Vector4f();
    }

    /**
     * Creates a new Vector4f with the given values
     *
     * @param x the x component
     * @param y the y component
     * @param z the z component
     * @param w the w component
     * @return a new Vector4f
     */
    public static Vector4f of(float x, float y, float z, float w) {
        return new Vector4f(x, y, z, w);
    }

    /**
     * Creates a new Vector4f and initialize it with the given value
     *
     * @param d the value of all four components
     * @return a new Vector4f
     */
    public static Vector4f of(float d) {
        return Vector4.of(d, d, d, d);
    }

    /**
     * Creates a new Vector4f with the same values as v
     *
     * @param v the Vector4fc to copy the values from
     * @return a new Vector4f
     */
    public static Vector4f of(Vector4fc v) {
        return Vector4.of(v.x(), v.y(), v.z(), v.w());
    }

    /**
     * Creates a new Vector4f with the same values as v and w
     *
     * @param v the Vector3fc to copy the xyz values from
     * @param w the w value of the new Vector4f
     * @return a new Vector4f
     */
    public static Vector4f of(Vector3fc v, float w) {
        return Vector4.of(v.x(), v.y(), v.z(), w);
    }

    /**
     * Creates a new Vector4f with the same values as v and z and w
     *
     * @param v the Vector2fc to copy the xy values from
     * @param z the z value of the new Vector4f
     * @param w the w value of the new Vector4f
     * @return a new Vector4f
     */
    public static Vector4f of(Vector2fc v, float z, float w) {
        return Vector4.of(v.x(), v.y(), z, w);
    }

    /**
     * Creates a new Vector4f with the same values as v1 and v2
     *
     * @param v1 the Vector2fc to copy the xy values from
     * @param v2 the Vector2fc to copy the zw values from
     * @return a new Vector4f
     */
    public static Vector4f of(Vector2fc v1, Vector2fc v2) {
        return Vector4.of(v1.x(), v1.y(), v2.x(), v2.y());
    }

}
