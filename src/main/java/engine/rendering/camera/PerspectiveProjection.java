package engine.rendering.camera;

import engine.rendering.Camera;
import glfw.window.Window;
import maths.joml.Matrix4f;
import maths.utils.Matrix4;

public class PerspectiveProjection implements CameraProjection {

    private final Matrix4f matrix = Matrix4.create();

    @Override
    public Matrix4f getProjectionMatrix(Camera camera) {
        final int width = Window.current().getWidth();
        final int height = Window.current().getHeight();
        return Matrix4.ofProjection(camera.getFov(), width, height,
                camera.getNearPlane(), camera.getFarPlane(), matrix);
    }
}
