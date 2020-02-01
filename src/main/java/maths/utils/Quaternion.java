package maths.utils;

import maths.joml.Quaternionf;
import maths.joml.Quaternionfc;
import maths.joml.Vector3fc;

public class Quaternion {

    private Quaternion() {

    }

    /**
     * Creates a new Quaternionf
     *
     * @return a new Quaternionf
     */
    public static Quaternionf create() {
        return new Quaternionf();
    }

    /**
     * Creates a new Quaternionf and initialize it with the given values
     *
     * @param x the x component
     * @param y the y component
     * @param z the z component
     * @param w the w component
     * @return a new Quaternionf
     */
    public static Quaternionf of(float x, float y, float z, float w) {
        return new Quaternionf(x, y, z, w);
    }

    /**
     * Creates a new Quaternion with the same values as q
     *
     * @param q the quaternion to copy the values from
     * @return a new Quaternionf
     */
    public static Quaternionf of(Quaternionfc q) {
        return Quaternion.of(q.x(), q.y(), q.z(), q.w());
    }

    /**
     * Creates a new Quaternionf that rotated by the euler angles given
     *
     * @param angles the euler angles
     * @return a new Quaternion
     */
    public static Quaternionf ofEulerAngles(Vector3fc angles) {
        return Quaternion.ofEulerAngles(angles.x(), angles.y(), angles.z());
    }

    public static Quaternionf ofEulerAngles(float x, float y, float z) {
        Quaternionf quaternion = Quaternion.create();
        quaternion.identity();
        quaternion.rotateXYZ(x, y, z);
        return quaternion;
    }

    /**
     * Creates a new Quaternionf that rotated toward the direction given
     *
     * @param direction the direction
     * @return a new Quaternion
     */
    public static Quaternionf ofDirection(Vector3fc direction, Quaternionf dest) {
        dest.identity();
        dest.lookAlong(direction, Vector3.UP);
        return dest;
    }

    public static Quaternionf createDirection(Vector3fc direction) {
        return ofDirection(direction, Quaternion.create());
    }

    /*
     *
     * Methods for static operations on vectors
     *
     */

    public static Quaternionf add(Quaternionf a, Quaternionf b) {
        return Quaternion.of(a).add(b);
    }

    public static Quaternionf mul(Quaternionf a, Quaternionf b) {
        return Quaternion.of(a).mul(b);
    }

}
