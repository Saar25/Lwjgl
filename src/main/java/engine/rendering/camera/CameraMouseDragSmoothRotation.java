package engine.rendering.camera;

import engine.rendering.Camera;
import glfw.input.Mouse;
import glfw.input.MouseButton;

public class CameraMouseDragSmoothRotation implements CameraController {

    private final Mouse mouse;
    private final Mouse.Observer observer;

    private float sensitivity = .01f;

    private float xVelocity = 0;
    private float yVelocity = 0;

    public CameraMouseDragSmoothRotation(Mouse mouse) {
        this.mouse = mouse;
        this.observer = mouse.createObserver();
    }

    @Override
    public void control(Camera camera) {
        if (mouse.isButtonDown(MouseButton.PRIMARY)) {
            xVelocity += -(float) observer.getDeltaY() * sensitivity;
            yVelocity += -(float) observer.getDeltaX() * sensitivity;
        }
        camera.getTransform().addRotation(xVelocity, yVelocity, 0);
        xVelocity = xVelocity * .95f;
        yVelocity = yVelocity * .95f;
        observer.update();
    }

    public void setSensitivity(float sensitivity) {
        this.sensitivity = sensitivity;
    }
}
