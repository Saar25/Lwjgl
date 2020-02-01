package engine.rendering.camera;

import engine.rendering.Camera;
import engine.rendering.Spatial;
import maths.joml.Vector3f;

public class CameraLookAtRotation implements CameraController {

    private final Vector3f center;

    public CameraLookAtRotation(Spatial center) {
        this.center = center.getTransform().getPosition();
    }

    public CameraLookAtRotation(Vector3f center) {
        this.center = center;
    }

    @Override
    public void control(Camera camera) {
        camera.getTransform().lookAt(center);
    }
}
