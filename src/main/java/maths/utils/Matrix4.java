package maths.utils;

import maths.joml.Matrix4f;
import maths.joml.Matrix4fc;
import maths.joml.Quaternionfc;
import maths.joml.Vector3fc;

public final class Matrix4 {

    public static final ObjectPool<Matrix4f> pool = new ObjectPool<>(Matrix4::create);

    private static final Matrix4f matrix = Matrix4.create();

    private Matrix4() {

    }

    /**
     * Creates a new Matrix4f
     *
     * @return new Matrix4f
     */
    public static Matrix4f create() {
        return new Matrix4f();
    }

    /**
     * Creates a new Matrix4f with the given matrix values
     *
     * @param matrix the matrix to copy
     * @return new Matrix4f
     */
    public static Matrix4f of(Matrix4fc matrix) {
        return new Matrix4f(matrix);
    }

    /**
     * Returns a perspective projection matrix
     *
     * @param fov    the field of view in radians
     * @param width  the width of the screen
     * @param height the height of the screen
     * @param zNear  the near plane
     * @param zFar   the far plane
     * @param dest   the value destination
     * @return the projection matrix
     */
    public static Matrix4f ofProjection(float fov, float width, float height, float zNear, float zFar, Matrix4f dest) {
        float aspectRatio = width / height;
        dest.identity();
        dest.perspective(fov, aspectRatio, zNear, zFar);
        return dest;
    }

    public static Matrix4f ofProjection(float fov, float width, float height, float zNear, float zFar) {
        return ofProjection(fov, width, height, zNear, zFar, matrix);
    }

    public static Matrix4f createProjection(float fov, float width, float height, float zNear, float zFar) {
        return ofProjection(fov, width, height, zNear, zFar, Matrix4.create());
    }

    /**
     * Returns a orthographic projection matrix
     *
     * @param left   the left frustum edge
     * @param right  the right frustum edge
     * @param bottom the bottom frustum edge
     * @param top    the top frustum edge
     * @param zNear  the near clipping plane distance
     * @param zFar   the far clipping plane distance
     * @param dest   the value destination
     * @return the projection matrix
     */
    public static Matrix4f ofProjection(float left, float right, float bottom, float top,
                                        float zNear, float zFar, Matrix4f dest) {
        dest.identity();
        dest.ortho(left, right, bottom, top, zNear, zFar);
        return dest;
    }

    public static Matrix4f ofProjection(float left, float right, float bottom, float top, float zNear, float zFar) {
        return ofProjection(left, right, bottom, top, zNear, zFar, matrix);
    }

    public static Matrix4f createProjection(float left, float right, float bottom, float top, float zNear, float zFar) {
        return ofProjection(left, right, bottom, top, zNear, zFar, Matrix4.create());
    }

    /**
     * Returns a view matrix based on the camera
     *
     * @param position the position camera
     * @param rotation the rotation camera
     * @param dest     the value destination
     * @return the view matrix
     */
    public static Matrix4f ofView(Vector3fc position, Vector3fc rotation, Matrix4f dest) {
        dest.identity().rotateXYZ(rotation.x(), rotation.y(), rotation.z())
                .translate(-position.x(), -position.y(), -position.z());
        return dest;
    }

    public static Matrix4f ofView(Vector3fc position, Vector3fc rotation) {
        return ofView(position, rotation, matrix);
    }

    public static Matrix4f createView(Vector3fc position, Vector3fc rotation) {
        return ofView(position, rotation, Matrix4.create());
    }

    /**
     * Returns a view matrix based on the camera
     *
     * @param position the position camera
     * @param rotation the rotation camera
     * @param dest     the value destination
     * @return the view matrix
     */
    public static Matrix4f ofView(Vector3fc position, Quaternionfc rotation, Matrix4f dest) {
        return dest.identity().translationRotateScaleInvert(position, rotation, 1);
        //return dest.identity().rotate(rotation.invert(Quaternion.create()))
        //        .translate(-position.x(), -position.y(), -position.z());
    }

    public static Matrix4f ofView(Vector3fc position, Quaternionfc rotation) {
        return ofView(position, rotation, matrix);
    }

    public static Matrix4f createView(Vector3fc position, Quaternionfc rotation) {
        return ofView(position, rotation, Matrix4.create());
    }

    /**
     * Returns a transformation matrix based on the given values
     *
     * @param position the position
     * @param rotation the rotation
     * @param scale    the scale
     * @param dest     the value destination
     * @return the transformation matrix
     */
    public static Matrix4f ofTransformation(Vector3fc position, Vector3fc rotation, Vector3fc scale, Matrix4f dest) {
        return dest.identity().translate(position)
                .rotateXYZ(rotation.x(), rotation.y(), rotation.z())
                .scale(scale);
    }

    public static Matrix4f ofTransformation(Vector3fc position, Vector3fc rotation, Vector3fc scale) {
        return ofTransformation(position, rotation, scale, matrix);
    }

    public static Matrix4f createTransformation(Vector3fc position, Vector3fc rotation, Vector3fc scale) {
        return ofTransformation(position, rotation, scale, Matrix4.create());
    }

    /**
     * Returns a transformation matrix based on the given values
     *
     * @param position the position
     * @param rotation the rotation
     * @param scale    the scale
     * @param dest     the value destination
     * @return the transformation matrix
     */
    public static Matrix4f ofTransformation(Vector3fc position, Quaternionfc rotation, Vector3fc scale, Matrix4f dest) {
        return dest.identity().translationRotateScale(position, rotation, scale);
        //return dest.identity().translate(position).rotate(rotation).scale(scale);
    }

    public static Matrix4f ofTransformation(Vector3fc position, Quaternionfc rotation, Vector3fc scale) {
        return ofTransformation(position, rotation, scale, matrix);
    }

    public static Matrix4f createTransformation(Vector3fc position, Quaternionfc rotation, Vector3fc scale) {
        return ofTransformation(position, rotation, scale, Matrix4.create());
    }

    /**
     * Returns an identity matrix
     *
     * @param dest the value destination
     * @return the identity matrix
     */
    public static Matrix4f ofIdentity(Matrix4f dest) {
        return dest.identity();
    }

    public static Matrix4f ofIdentity() {
        return matrix.identity();
    }

    public static Matrix4f createIdentity() {
        return ofIdentity(Matrix4.create());
    }

    public static Matrix4f mul(Matrix4f a, Matrix4f b) {
        return Matrix4.of(a).mul(b);
    }

}
