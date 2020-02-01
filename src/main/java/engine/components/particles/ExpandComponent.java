package engine.components.particles;

import engine.components.MovementComponent;
import engine.componentsystem.GameComponent;
import engine.gameengine.Time;
import maths.utils.Vector3;
import maths.joml.Vector3f;

public class ExpandComponent extends GameComponent {

    private final float speed;
    private MovementComponent movement;

    public ExpandComponent(float speed) {
        this.speed = speed;
    }

    @Override
    public void start() {
        this.movement = getComponent(MovementComponent.class);
    }

    @Override
    public void update() {
        Vector3f velocity = Vector3.of(getTransform().getPosition());
        velocity.normalize(Time.getDelta() * speed);
        movement.addVelocity(velocity);
    }

}
