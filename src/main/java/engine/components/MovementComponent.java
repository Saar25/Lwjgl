package engine.components;

import engine.componentsystem.GameComponent;
import engine.gameengine.Time;
import maths.utils.Vector3;
import maths.joml.Vector3f;
import maths.joml.Vector3fc;

public class MovementComponent extends GameComponent {

    private static final Vector3f rotatedVelocity = Vector3.create();

    private final Vector3f velocity = Vector3.create();

    @Override
    public void update() {
        rotatedVelocity.set(velocity).mul(Time.getDelta());
        rotatedVelocity.rotate(getTransform().getRotation());
        getTransform().addPosition(rotatedVelocity);
    }

    public Vector3f predictPosition(float interval) {
        return Vector3.of(velocity).mul(interval).add(getTransform().getPosition());
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    public void setVelocity(float x, float y, float z) {
        velocity.set(x, y, z);
    }

    public void addVelocity(Vector3fc v) {
        velocity.add(v);
    }

    public void addVelocity(float x, float y, float z) {
        velocity.add(x, y, z);
    }

    public void stop() {
        velocity.set(0);
    }
}
