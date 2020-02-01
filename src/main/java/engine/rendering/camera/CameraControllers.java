package engine.rendering.camera;

import engine.rendering.Camera;

import java.util.Arrays;
import java.util.List;

public class CameraControllers implements CameraController {

    private final List<CameraController> controllers;

    public CameraControllers(CameraController... controllers) {
        this.controllers = Arrays.asList(controllers);
    }

    @Override
    public void control(Camera camera) {
        for (CameraController controller : controllers) {
            controller.control(camera);
        }
    }
}
