package engine.components.motion;

import engine.componentsystem.GameComponent;
import maths.utils.Vector3;
import maths.joml.Vector3f;
import maths.joml.Vector3fc;

public class VelocityComponent extends GameComponent {

    private final Vector3f velocity;

    public VelocityComponent() {
        this.velocity = Vector3.create();
    }

    @Override
    public void update() {
        getTransform().addPosition(velocity);
    }

    public void addVelocity(float x, float y, float z) {
        velocity.add(x, y, z);
    }

    public void addVelocity(Vector3fc v) {
        velocity.add(v);
    }

    public void setVelocity(float x, float y, float z) {
        velocity.set(x, y, z);
    }

    public void setVelocity(Vector3fc v) {
        velocity.set(v);
    }

    public float getX() {
        return velocity.x;
    }

    public float getY() {
        return velocity.y;
    }

    public float getZ() {
        return velocity.z;
    }

    public Vector3f get() {
        return velocity;
    }
}
