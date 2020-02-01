package engine.rendering.camera;

import engine.rendering.Camera;
import glfw.input.Keyboard;
import glfw.input.Mouse;

public class FlyCamera implements CameraController {

    private final CameraController positionControl;
    private final CameraController rotationControl;

    public FlyCamera(Keyboard keyboard, Mouse mouse) {
        this.positionControl = new CameraKeyboardMovement(keyboard);
        this.rotationControl = new CameraMouseDragRotation(mouse);
    }

    @Override
    public void control(Camera camera) {
        rotationControl.control(camera);
        positionControl.control(camera);
    }
}
