package engine.components.particles;

import engine.components.MovementComponent;
import engine.componentsystem.GameComponent;
import maths.joml.Vector3f;
import maths.utils.Vector3;

public class ExplosionComponent extends GameComponent {

    private final float height;
    private final float radius;

    public ExplosionComponent(float height, float radius) {
        this.height = height;
        this.radius = radius;
    }

    @Override
    public void start() {
        final Vector3f velocity = Vector3.create();
        velocity.x = (float) Math.random() - .5f;
        velocity.z = (float) Math.random() - .5f;
        velocity.normalize(radius).add(0, height, 0);
        getComponent(MovementComponent.class).addVelocity(velocity);
    }
}
