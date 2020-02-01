package engine.rendering.camera;

import engine.rendering.Camera;
import glfw.input.Mouse;

public class CameraMouseRotation implements CameraController {

    protected final Mouse mouse;
    protected final Mouse.Observer observer;

    private float sensitivity = .3f;

    public CameraMouseRotation(Mouse mouse) {
        this.mouse = mouse;
        this.observer = mouse.createObserver();
    }

    @Override
    public void control(Camera camera) {
        final float dx = -(float) observer.getDeltaX() * sensitivity;
        final float dy = -(float) observer.getDeltaY() * sensitivity;
        camera.getTransform().addRotation(dy, dx, 0);
        observer.update();
    }

    public void setSensitivity(float sensitivity) {
        this.sensitivity = sensitivity;
    }
}
