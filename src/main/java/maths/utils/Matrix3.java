package maths.utils;

import maths.joml.Matrix3f;
import maths.joml.Vector2fc;

public final class Matrix3 {

    public static final ObjectPool<Matrix3f> pool = new ObjectPool<>(Matrix3::create);

    private static final Matrix3f matrix = Matrix3.create();

    private Matrix3() {

    }

    /**
     * Creates a new Matrix3f
     *
     * @return new Matrix3f
     */
    public static Matrix3f create() {
        return new Matrix3f();
    }

    /**
     * Returns a transformation matrix based on the given values
     *
     * @param position the position
     * @param rotation the rotation
     * @param scaling  the scaling
     * @param dest     the value destination
     * @return the transformation matrix
     */
    public static Matrix3f ofTransformation(Vector2fc position, Vector2fc scaling, float rotation, Matrix3f dest) {
        float sin = Maths.sinf(rotation);
        float cos = Maths.cosf(rotation);
        dest.identity()
                // Translate
                .m02(position.x()).m12(position.y())
                // Rotate
                .m00(cos).m01(-sin)
                .m10(sin).m11(cos)
                // Scale
                .scale(scaling.x(), scaling.y(), 0);
        return dest;
    }

    public static Matrix3f ofTransformation(Vector2fc position, Vector2fc scaling, float rotation) {
        return ofTransformation(position, scaling, rotation, matrix);
    }

    public static Matrix3f createTransformation(Vector2fc position, Vector2fc scaling, float rotation) {
        return ofTransformation(position, scaling, rotation, Matrix3.create());
    }

    /**
     * Returns an identity matrix
     *
     * @param dest the value destination
     * @return the identity matrix
     */
    public static Matrix3f ofIdentity(Matrix3f dest) {
        dest.identity();
        return dest;
    }

    public static Matrix3f ofIdentity() {
        matrix.identity();
        return matrix;
    }

    public static Matrix3f createIdentity() {
        return ofIdentity(Matrix3.create());
    }
}
