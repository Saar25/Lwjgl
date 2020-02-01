package engine.components;

import engine.componentsystem.GameComponent;
import maths.objects.Transform;
import maths.objects.Transformable;
import maths.utils.Vector3;
import maths.joml.Vector3f;

public class BackFaceComponent extends GameComponent {

    private static final float MIN_VELOCITY = .5f;
    private static final Vector3f vector = Vector3.create();

    private final Transform backFaced;
    private MovementComponent movement;

    public BackFaceComponent(Transformable backFaced) {
        this.backFaced = backFaced.getTransform();
    }

    @Override
    public void start() {
        movement = getComponent(MovementComponent.class);
    }

    @Override
    public void update() {
        final float x = Math.abs(movement.getVelocity().x);
        final float y = Math.abs(movement.getVelocity().y);
        final float z = Math.abs(movement.getVelocity().z);
        if (x > MIN_VELOCITY || y > MIN_VELOCITY || z > MIN_VELOCITY) {
            Vector3f direction = vector.set(getTransform().getPosition());
            direction.sub(backFaced.getPosition()).mul(-1, 0, -1);
            getTransform().lookAlong(direction);
        }
    }
}
