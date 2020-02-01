package engine.rendering.camera;

import engine.rendering.Camera;
import maths.joml.Matrix4f;
import maths.utils.Matrix4;

public class OrthographicProjection implements CameraProjection {

    private final Matrix4f matrix = Matrix4.create();

    private final float left;
    private final float right;

    private final float bottom;
    private final float top;

    private final float near;
    private final float far;

    public OrthographicProjection(float left, float right, float bottom, float top, float near, float far) {
        this.left = left;
        this.right = right;
        this.bottom = bottom;
        this.top = top;
        this.near = near;
        this.far = far;
    }

    @Override
    public Matrix4f getProjectionMatrix(Camera camera) {
        return Matrix4.ofProjection(left, right, bottom, top, near, far, matrix);
    }
}
