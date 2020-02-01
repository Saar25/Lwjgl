package engine.rendering.camera;

import engine.rendering.Camera;

public interface CameraController {

    CameraController NONE = camera -> {};

    void control(Camera camera);

}
