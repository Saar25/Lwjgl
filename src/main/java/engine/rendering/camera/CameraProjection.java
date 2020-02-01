package engine.rendering.camera;

import engine.rendering.Camera;
import maths.joml.Matrix4f;

public interface CameraProjection {

    /**
     * Return the projection matrix of the given camera
     *
     * @param camera the camera
     * @return the projection matrix
     */
    Matrix4f getProjectionMatrix(Camera camera);

}
