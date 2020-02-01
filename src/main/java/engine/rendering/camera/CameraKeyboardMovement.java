package engine.rendering.camera;

import engine.gameengine.Time;
import engine.rendering.Camera;
import glfw.input.Keyboard;
import maths.joml.Vector3f;
import maths.utils.Vector3;
import org.lwjgl.glfw.GLFW;

public class CameraKeyboardMovement implements CameraController {

    private final Keyboard keyboard;

    private final Vector3f speeds = Vector3.of(100);

    public CameraKeyboardMovement(Keyboard keyboard) {
        this.keyboard = keyboard;
    }

    @Override
    public void control(Camera camera) {
        final Vector3f deltaPosition = Vector3.create();
        if (keyboard.isKeyPressed(GLFW.GLFW_KEY_W)) {
            deltaPosition.add(0, 0, -speeds.z);
        }
        if (keyboard.isKeyPressed(GLFW.GLFW_KEY_A)) {
            deltaPosition.add(-speeds.x, 0, 0);
        }
        if (keyboard.isKeyPressed(GLFW.GLFW_KEY_S)) {
            deltaPosition.add(0, 0, +speeds.z);
        }
        if (keyboard.isKeyPressed(GLFW.GLFW_KEY_D)) {
            deltaPosition.add(+speeds.x, 0, 0);
        }
        deltaPosition.rotate(camera.getTransform().getRotation());
        if (keyboard.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
            deltaPosition.add(0, +speeds.y, 0);
        }
        if (keyboard.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            deltaPosition.add(0, -speeds.y, 0);
        }
        deltaPosition.mul(Time.getDelta());
        camera.getTransform().getPosition().add(deltaPosition);
    }

    public void setSpeed(float speed) {
        this.speeds.set(speed);
    }

    public void setSpeeds(float xAxis, float yAxis, float zAxis) {
        this.speeds.set(xAxis, yAxis, zAxis);
    }
}
