package engine.components.animation;

import engine.components.MovementComponent;
import engine.componentsystem.GameComponent;

public class FallingAnimationComponent extends GameComponent {

    private MovementComponent movement;

    private float rotation;

    public FallingAnimationComponent() {

    }

    @Override
    public void start() {
        movement = getComponent(MovementComponent.class);
    }

    @Override
    public void update() {
        getTransform().getRotation().rotateX(-rotation);
        if (movement.getVelocity().y < -5) {
            rotation += rotation < Math.PI ? 0.01f : 0;
        } else {
            rotation = 0;
        }
        getTransform().getRotation().rotateX(+rotation);
    }
}
