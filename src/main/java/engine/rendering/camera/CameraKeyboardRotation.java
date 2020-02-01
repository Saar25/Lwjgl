package engine.rendering.camera;

import engine.rendering.Camera;
import glfw.input.Keyboard;

public class CameraKeyboardRotation implements CameraController {

    private final Keyboard keyboard;

    private int leftKey = 'G';
    private int rightKey = 'J';
    private int upKey = 'Y';
    private int downKey = 'N';

    private float speed = .3f;

    public CameraKeyboardRotation(Keyboard keyboard) {
        this.keyboard = keyboard;
    }

    @Override
    public void control(Camera camera) {
        if (keyboard.isKeyPressed(leftKey)) {
            camera.getTransform().addRotation(0, -speed, 0);
        }
        if (keyboard.isKeyPressed(rightKey)) {
            camera.getTransform().addRotation(0, +speed, 0);
        }
        if (keyboard.isKeyPressed(upKey)) {
            camera.getTransform().addRotation(-speed, 0, 0);
        }
        if (keyboard.isKeyPressed(downKey)) {
            camera.getTransform().addRotation(+speed, 0, 0);
        }
    }

    public void setLeftKey(int leftKey) {
        this.leftKey = leftKey;
    }

    public void setRightKey(int rightKey) {
        this.rightKey = rightKey;
    }

    public void setUpKey(int upKey) {
        this.upKey = upKey;
    }

    public void setDownKey(int downKey) {
        this.downKey = downKey;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
