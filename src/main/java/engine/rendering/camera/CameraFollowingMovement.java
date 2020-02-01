package engine.rendering.camera;

import engine.rendering.Camera;
import maths.joml.Vector3f;
import maths.objects.Transform;
import maths.objects.Transformable;
import maths.utils.Vector3;

public class CameraFollowingMovement implements CameraController {

    private final Transform centerTransform;

    public CameraFollowingMovement(Transformable centerTransform) {
        this.centerTransform = centerTransform.getTransform();
    }

    @Override
    public void control(Camera camera) {
        Vector3f positionOffset2 = Vector3.forward();
        positionOffset2.rotate(camera.getTransform().getRotation());
        positionOffset2.mul(camera.getTransform().getScale());
        positionOffset2.add(centerTransform.getPosition());
        camera.getTransform().setPosition(positionOffset2);
    }
}
