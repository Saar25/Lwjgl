package engine.rendering;

import engine.gameengine.GameState;
import engine.rendering.camera.CameraController;
import engine.rendering.camera.CameraProjection;
import engine.rendering.camera.PerspectiveProjection;
import engine.util.property.FloatProperty;
import maths.joml.Matrix4f;
import maths.joml.Matrix4fc;
import maths.objects.Transform;
import maths.utils.Matrix4;

public class Camera {

    private final Matrix4f projectionViewMatrix = Matrix4.create();
    private final Matrix4f projectionMatrix = Matrix4.create();
    private final Matrix4f viewMatrix = Matrix4.create();

    private FloatProperty nearPlaneProperty = new FloatProperty(2f);
    private FloatProperty farPlaneProperty = new FloatProperty(10000.0f);
    private FloatProperty fovProperty = new FloatProperty((float) Math.toRadians(60f));

    private CameraController controller = CameraController.NONE;
    private CameraProjection projection = new PerspectiveProjection();

    private Transform transform;

    private boolean enabled = true;

    public Camera() {
        this.transform = new Transform();
        getTransform().setScale(1f);
    }

    public void reflect(float h) {
        final float height = getTransform().getPosition().y - h;
        getTransform().getPosition().y -= height * 2;
        getTransform().getRotation().x *= -1;
        getTransform().getRotation().z *= -1;
    }

    public void reflect() {
        getTransform().getPosition().y *= -1;
        getTransform().getRotation().x *= -1;
        getTransform().getRotation().z *= -1;
    }

    public void update() {
        if (GameState.getCurrent() == GameState.GAME) {
            controller.control(this);
        }
    }

    public void setController(CameraController controller) {
        this.controller = controller == null ? CameraController.NONE : controller;
    }

    public void setProjection(CameraProjection projection) {
        this.projection = projection;
        updateProjectionMatrix();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /* ========= TRANSFORM ========= */

    public Transform getTransform() {
        return transform;
    }


    /* ========= PLANES and FOV ========= */

    /**
     * Returns the near plane property of the camera
     *
     * @return the near plane property of the camera
     */
    public FloatProperty nearPlaneProperty() {
        return nearPlaneProperty;
    }

    public float getNearPlane() {
        return nearPlaneProperty().getValue();
    }

    public void setNearPlane(float nearPlane) {
        nearPlaneProperty().setValue(nearPlane);
    }

    /**
     * Returns the far plane property of the camera
     *
     * @return the far plane property of the camera
     */
    public FloatProperty farPlaneProperty() {
        return farPlaneProperty;
    }

    public float getFarPlane() {
        return farPlaneProperty.getValue();
    }

    public void setFarPlane(float farPlane) {
        farPlaneProperty().setValue(farPlane);
    }

    /**
     * Returns the field of view property of the camera
     *
     * @return the field of view property of the camera
     */
    public FloatProperty fovProperty() {
        return fovProperty;
    }

    public float getFov() {
        return fovProperty().getValue();
    }

    public void setFov(float fov) {
        fovProperty().setValue(fov);
    }

    /* ========= MATRICES ========= */

    public void updateViewMatrix() {
        Matrix4.ofView(transform.getPosition(), transform.getRotation(), viewMatrix);
    }

    public void updateProjectionMatrix() {
        final Matrix4f matrix = projection.getProjectionMatrix(this);
        projectionMatrix.set(matrix);
    }

    public void updateProjectionViewMatrix() {
        getProjectionMatrix().mul(getViewMatrix(), projectionViewMatrix);
    }

    public Matrix4fc getViewMatrix() {
        return enabled ? viewMatrix : Matrix4.pool.poolAndGive().identity();
    }

    public Matrix4fc getProjectionMatrix() {
        return projectionMatrix;
    }

    public Matrix4fc getProjectionViewMatrix() {
        return projectionViewMatrix;
    }
}
