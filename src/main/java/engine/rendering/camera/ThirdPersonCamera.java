package engine.rendering.camera;

import engine.rendering.Camera;
import glfw.input.Mouse;
import maths.joml.Vector3f;
import maths.objects.Transformable;
import maths.utils.Vector3;

public class ThirdPersonCamera implements CameraController {

    private final CameraController positionControl;
    private final CameraController rotationControl;
    private final CameraController scaleControl;
    private final Vector3f offset;

    public ThirdPersonCamera(Mouse mouse, Transformable center) {
        this.positionControl = new CameraFollowingMovement(center);
        this.rotationControl = new CameraMouseDragRotation(mouse);
        this.scaleControl = new CameraMouseScale(mouse);
        this.offset = Vector3.create();
    }

    public ThirdPersonCamera(Mouse mouse, Transformable center, Vector3f offset) {
        this.positionControl = new CameraFollowingMovement(center);
        this.rotationControl = new CameraMouseDragRotation(mouse);
        this.scaleControl = new CameraMouseScale(mouse);
        this.offset = offset;
    }

    @Override
    public void control(Camera camera) {
        rotationControl.control(camera);
        positionControl.control(camera);
        scaleControl.control(camera);
        camera.getTransform().addPosition(offset);
    }
}
