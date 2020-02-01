package engine.components;

import engine.components.physics.GravityComponent;
import engine.componentsystem.GameComponent;
import engine.gameengine.Time;

public class FloatingComponent extends GameComponent {

    private final float waterHeight;
    private GravityComponent gravity;
    private MovementComponent movement;

    public FloatingComponent(float waterHeight) {
        this.waterHeight = waterHeight;
    }

    @Override
    public void start() {
        gravity = getComponent(GravityComponent.class);
        movement = getComponent(MovementComponent.class);
    }

    @Override
    public void update() {
        if (getTransform().getPosition().y < waterHeight) {
            movement.getVelocity().y += 30 * Time.getDelta();
            movement.getVelocity().y = Math.min(movement.getVelocity().y, 15);
            gravity.setEnabled(false);
        } else {
            gravity.setEnabled(true);
        }
    }
}
