package engine.components;

import engine.componentsystem.GameComponent;
import glfw.input.Keyboard;
import org.lwjgl.glfw.GLFW;

public class KeyboardInputComponent extends GameComponent {

    private ActiveMovementComponent movement;
    private Keyboard keyboard;

    public KeyboardInputComponent(Keyboard keyboard) {
        this.keyboard = keyboard;
    }

    @Override
    public void start() {
        this.movement = getComponent(ActiveMovementComponent.class);
    }

    @Override
    public void update() {
        if (keyboard.allKeysPressed(GLFW.GLFW_KEY_W, GLFW.GLFW_KEY_LEFT_CONTROL)) {
            movement.sprint();
        } else if (keyboard.isKeyPressed(GLFW.GLFW_KEY_W)) {
            movement.walk(true);
        }
        if (keyboard.isKeyPressed(GLFW.GLFW_KEY_A)) {
            movement.sidewalk(true);
        }
        if (keyboard.isKeyPressed(GLFW.GLFW_KEY_S)) {
            movement.walk(false);
        }
        if (keyboard.isKeyPressed(GLFW.GLFW_KEY_D)) {
            movement.sidewalk(false);
        }
        if (keyboard.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
            movement.jump();
        }
    }
}
