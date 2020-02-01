package engine.rendering.camera;

import engine.rendering.Camera;
import glfw.input.Mouse;
import glfw.input.MouseButton;

public class CameraMouseDragRotation extends CameraMouseRotation implements CameraController {

    public CameraMouseDragRotation(Mouse mouse) {
        super(mouse);
    }

    @Override
    public void control(Camera camera) {
        if (mouse.isButtonDown(MouseButton.PRIMARY)) {
            super.control(camera);
        } else {
            super.observer.update();
        }
    }
}
