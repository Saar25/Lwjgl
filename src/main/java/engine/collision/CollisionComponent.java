package engine.collision;

import engine.components.MovementComponent;
import engine.componentsystem.GameComponent;

public class CollisionComponent extends GameComponent {

    private final boolean isStatic;
    private final Collider collider;

    private MovementComponent movement;

    public CollisionComponent(Collider collider, boolean isStatic) {
        this.isStatic = isStatic;
        this.collider = collider;
    }

    @Override
    public void start() {
        movement = getComponent(MovementComponent.class);
    }

    @Override
    public void update() {
        collider.update(getTransform().getPosition(), movement.getVelocity());
    }
}
