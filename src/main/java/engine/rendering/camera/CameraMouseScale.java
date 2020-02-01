package engine.rendering.camera;

import engine.rendering.Camera;
import glfw.input.Mouse;

public class CameraMouseScale implements CameraController {

    private final Mouse.Observer observer;

    private float sensitivity = 3;

    public CameraMouseScale(Mouse mouse) {
        this.observer = mouse.createObserver();
    }

    @Override
    public void control(Camera camera) {
        final float change = -(float) (observer.getScrollChange() * sensitivity);
        camera.getTransform().addScale(change);
        observer.update();
    }

    public void setSensitivity(float sensitivity) {
        this.sensitivity = sensitivity;
    }
}
