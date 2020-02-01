package engine.components.physics;

import engine.components.MovementComponent;
import engine.componentsystem.GameComponent;
import engine.gameengine.Time;

public class GravityComponent extends GameComponent {

    private static final float g = 9.8f;

    private MovementComponent movement;

    @Override
    public void start() {
        movement = getComponent(MovementComponent.class);
    }

    @Override
    public void update() {
        movement.getVelocity().y -= g * Time.getDelta() * 10;
    }

}
