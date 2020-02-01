package maths.utils;

import maths.joml.Vector2fc;
import maths.joml.Vector3f;
import maths.joml.Vector3fc;

public final class Maths {

    public static final float PI = (float) Math.PI;

    /**
     * Don't let anyone instantiate this class
     */
    private Maths() {

    }

    /**
     * Calculates the height of the xz position on the 3 points of the 3D triangle
     *
     * @param p1 first point of the triangle
     * @param p2 second point of the triangle
     * @param p3 third point of the triangle
     * @param ps x and z position on the triangle
     * @return the y value of the position
     */
    public static float barryCentric(Vector3fc p1, Vector3fc p2, Vector3fc p3, Vector2fc ps) {
        float det = (p2.z() - p3.z()) * (p1.x() - p3.x()) + (p3.x() - p2.x()) * (p1.z() - p3.z());
        float l1 = ((p2.z() - p3.z()) * (ps.x() - p3.x()) + (p3.x() - p2.x()) * (ps.y() - p3.z())) / det;
        float l2 = ((p3.z() - p1.z()) * (ps.x() - p3.x()) + (p1.x() - p3.x()) * (ps.y() - p3.z())) / det;
        float l3 = 1.0f - l1 - l2;
        return l1 * p1.y() + l2 * p2.y() + l3 * p3.y();
    }

    /**
     * Calculates the normal of a 3D plane given 3 points on the plane that are not on the same line
     * The 3 points must be on clockwise order
     *
     * @param p1 1st point on the plane
     * @param p2 2nd point on the plane
     * @param p3 3rd point on the plane
     * @return the normal to the plane
     */
    public static Vector3f calculateNormal(Vector3fc p1, Vector3fc p2, Vector3fc p3) {
        Vector3f v1 = p2.sub(p1, Vector3.create());
        Vector3f v2 = p3.sub(p1, Vector3.create());
        return v1.cross(v2).normalize();
    }

    /**
     * Clamp a value between 2 other values
     *
     * @param a   the value to clamp
     * @param min the minimum value
     * @param max the maximum value
     * @return the clamped value
     */
    public static float clamp(float a, float min, float max) {
        if (a < min) return min;
        return Math.min(a, max);
    }

    /**
     * Clamp a value between 2 other values
     *
     * @param a   the value to clamp
     * @param min the minimum value
     * @param max the maximum value
     * @return the clamped value
     */
    public static int clamp(int a, int min, int max) {
        if (a < min) return min;
        if (a > max) return max;
        return a;
    }

    /**
     * Returns whether the given value is higher than min and lower than max
     *
     * @param a   the value to check
     * @param min the minimum value
     * @param max the maximum value
     * @return true if the value is between false otherwise
     */
    public static boolean isBetween(float a, float min, float max) {
        return a > min && a < max;
    }

    /**
     * Returns whether the given value is higher than min and lower than max or equals to them
     *
     * @param a   the value to check
     * @param min the minimum value
     * @param max the maximum value
     * @return true if the value is between or equals false otherwise
     */
    public static boolean isInside(float a, float min, float max) {
        return a >= min && a <= max;
    }

    /**
     * Mix two vectors using the scalar, for scalar = 0 v1 value will be returned, for scalar = 1 v2 will be returned.
     * Any other scalar value will interpolate between the two vectors
     *
     * @param vec1   the first vector
     * @param vec2   the second vector
     * @param scalar the scalar
     * @return The mixed vector
     */
    public static Vector3f mix(Vector3fc vec1, Vector3fc vec2, float scalar) {
        Vector3f v = Vector3.of(vec1);
        v.mul(1 - scalar);
        Vector3f vector3f = Vector3.create();
        vec2.mul(scalar, vector3f);
        v.add(vector3f);
        return v;
    }

    /**
     * Mix two floats using the scalar
     *
     * @param a      the first float
     * @param b      the second float
     * @param scalar the scalar
     * @return The mixed float
     */
    public static float mix(float a, float b, float scalar) {
        return a * scalar + b * (1 - scalar);
    }

    /**
     * Returns the trigonometric sine of an angle
     *
     * @param angle the angle in radians
     * @return trigonometric sine of an angle
     */
    public static float sinf(float angle) {
        return (float) Math.sin(angle);
    }

    public static float sinf(double angle) {
        return (float) Math.sin(angle);
    }

    /**
     * Returns the trigonometric cosine of an angle
     *
     * @param angle the angle in radians
     * @return trigonometric cosine of an angle
     */
    public static float cosf(float angle) {
        return (float) Math.cos(angle);
    }

    public static float cosf(double angle) {
        return (float) Math.cos(angle);
    }

    /**
     * Returns the trigonometric tangent of an angle
     *
     * @param angle the angle in radians
     * @return trigonometric tangent of an angle
     */
    public static float tanf(float angle) {
        return (float) Math.tan(angle);
    }

    public static float tanf(double angle) {
        return (float) Math.tan(angle);
    }

    /**
     * Returns the sqrt of the value
     *
     * @param a the the value
     * @return the sqrt of the value
     */
    public static float sqrt(float a) {
        return (float) Math.sqrt(a);
    }

}
