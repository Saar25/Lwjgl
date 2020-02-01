package engine.components.ai;

import engine.components.MovementComponent;
import engine.componentsystem.GameComponent;
import maths.objects.Transform;
import maths.objects.Transformable;

public class ZombieAIComponent extends GameComponent {

    private final Transform toFollow;
    private final float speed;

    private MovementComponent movement;

    public ZombieAIComponent(Transformable toFollow) {
        this.toFollow = toFollow.getTransform();
        speed = (float) Math.random() * 10 + 5;
    }

    @Override
    public void start() {
        movement = getComponent(MovementComponent.class);
    }

    @Override
    public void update() {
        float temp = movement.getVelocity().y;
        movement.getVelocity().set(toFollow.getPosition());
        movement.getVelocity().sub(getTransform().getPosition());

        if (movement.getVelocity().lengthSquared() > 0.1f) {
            movement.getVelocity().normalize(speed);
        } else {
            movement.getVelocity().set(0);
        }
        movement.getVelocity().y = temp;
    }

}
