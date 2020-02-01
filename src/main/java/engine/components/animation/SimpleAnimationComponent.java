package engine.components.animation;

import engine.components.MovementComponent;
import engine.componentsystem.GameComponent;
import engine.gameengine.Time;
import maths.joml.Vector3f;

public class SimpleAnimationComponent extends GameComponent {

    private MovementComponent movement;

    private float yRotation = 0;
    private float rotate = 0;

    private float time = 0;

    public SimpleAnimationComponent() {

    }

    @Override
    public void start() {
        movement = getComponent(MovementComponent.class);
    }

    @Override
    public void update() {
        final float interval = Time.getDelta();

        final Vector3f velocity = movement.getVelocity();
        if (velocity.x == 0 && velocity.z == 0) {
            rotate = Math.max(rotate - 20f * interval, 0);
        } else {
            rotate += 6 * interval;
            rotate %= 2 * Math.PI;
        }

        getTransform().getStaticRotation().rotateY(-yRotation);
        yRotation = (float) Math.sin(rotate) / 3;
        getTransform().getStaticRotation().rotateY(yRotation);
    }
}
