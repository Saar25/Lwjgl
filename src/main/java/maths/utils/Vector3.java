package maths.utils;

import maths.objects.CVector3f;
import maths.joml.Vector2fc;
import maths.joml.Vector3f;
import maths.joml.Vector3fc;
import maths.joml.Vector4fc;

public final class Vector3 {

    public static final ObjectPool<Vector3f> pool = new ObjectPool<>(Vector3::create);

    public static final Vector3fc UP = Vector3.constOf(0, 1, 0);
    public static final Vector3fc RIGHT = Vector3.constOf(1, 0, 0);
    public static final Vector3fc FORWARD = Vector3.constOf(0, 0, 1);

    public static final Vector3fc DOWN = Vector3.constOf(0, -1, 0);
    public static final Vector3fc LEFT = Vector3.constOf(-1, 0, 0);
    public static final Vector3fc BACKWARD = Vector3.constOf(0, 0, -1);

    public static final Vector3fc ONE = Vector3.constOf(1, 1, 1);
    public static final Vector3fc ZERO = Vector3.constOf(0, 0, 0);

    private Vector3() {

    }

    /**
     * Creates a new zero vector
     *
     * @return a new zero Vector3f
     */
    public static Vector3f create() {
        return new Vector3f();
    }

    /**
     * Creates a new Vector3f and initialize it with the given values
     *
     * @param x the x component
     * @param y the y component
     * @param z the z component
     * @return a new Vector3f
     */
    public static Vector3f of(float x, float y, float z) {
        return new Vector3f(x, y, z);
    }

    /**
     * Creates a new Vector3f and initialize it with the given value
     *
     * @param d the value of all three components
     * @return a new Vector3f
     */
    public static Vector3f of(float d) {
        return Vector3.of(d, d, d);
    }

    /**
     * Creates a new Vector3f with the xyz values of v
     *
     * @param v the Vector4fc to copy the xyz values from
     * @return a new Vector3f
     */
    public static Vector3f of(Vector4fc v) {
        return Vector3.of(v.x(), v.y(), v.z());
    }

    /**
     * Creates a new Vector3f with the same values as v
     *
     * @param v the Vector3fc to copy the values from
     * @return a new Vector3f
     */
    public static Vector3f of(Vector3fc v) {
        return Vector3.of(v.x(), v.y(), v.z());
    }

    /**
     * Creates a new Vector3f with the same values as v and z
     *
     * @param v the Vector2fc to copy the xy values from
     * @param z the z value of the new Vector3f
     * @return a new Vector3f
     */
    public static Vector3f of(Vector2fc v, float z) {
        return Vector3.of(v.x(), v.y(), z);
    }

    /**
     * Creates a new unmodifiable vector initialized with the given values
     *
     * @param x the x component
     * @param y the y component
     * @param z the z component
     * @return a new CVector3f
     */
    public static CVector3f constOf(float x, float y, float z) {
        return new CVector3f(x, y, z);
    }

    public static Vector3f upward() {
        return Vector3.of(UP.x(), UP.y(), UP.z());
    }

    public static Vector3f right() {
        return Vector3.of(RIGHT.x(), RIGHT.y(), RIGHT.z());
    }

    public static Vector3f forward() {
        return Vector3.of(FORWARD.x(), FORWARD.y(), FORWARD.z());
    }

    public static Vector3f downward() {
        return Vector3.of(DOWN.x(), DOWN.y(), DOWN.z());
    }

    public static Vector3f left() {
        return Vector3.of(LEFT.x(), LEFT.y(), LEFT.z());
    }

    public static Vector3f backward() {
        return Vector3.of(BACKWARD.x(), BACKWARD.y(), BACKWARD.z());
    }

    public static Vector3f one() {
        return Vector3.of(ONE.x(), ONE.y(), ONE.z());
    }

    public static Vector3f zero() {
        return Vector3.of(ZERO.x(), ZERO.y(), ZERO.z());
    }

    /*
     *
     * Methods for static operations on vectors
     *
     */

    public static Vector3f add(Vector3fc v1, Vector3fc v2) {
        return Vector3.of(v1).add(v2);
    }

    public static Vector3f sub(Vector3fc v1, Vector3fc v2) {
        return Vector3.of(v1).sub(v2);
    }

    public static Vector3f mul(Vector3fc v1, Vector3fc v2) {
        return Vector3.of(v1).mul(v2);
    }

    public static Vector3f div(Vector3fc v1, Vector3fc v2) {
        return Vector3.of(v1).div(v2);
    }

    public static Vector3f mul(Vector3fc v, float scalar) {
        return Vector3.of(v).mul(scalar);
    }

    public static Vector3f div(Vector3fc v, float scalar) {
        return Vector3.of(v).div(scalar);
    }

    public static Vector3f cross(Vector3fc v1, Vector3fc v2) {
        return Vector3.of(v1).cross(v2);
    }

    public static Vector3f normalize(float x, float y, float z) {
        return Vector3.of(x, y, z).normalize();
    }

    public static Vector3f normalize(Vector3fc vector) {
        return Vector3.of(vector).normalize();
    }

    public static float length(float x, float y, float z) {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public static float length(Vector3fc vector) {
        return vector.length();
    }

}