package engine.components;

import engine.componentsystem.GameComponent;

public class AutoJumpComponent extends GameComponent {

    private ActiveMovementComponent movement;

    @Override
    public void start() {
        movement = getComponent(ActiveMovementComponent.class);
    }

    @Override
    public void update() {
        movement.jump();
    }
}
