package engine.components.motion;

import engine.componentsystem.GameComponent;
import maths.utils.Vector3;
import maths.joml.Vector3f;
import maths.joml.Vector3fc;

public class ForceComponent extends GameComponent {

    private final float weight;
    private final Vector3f force;
    private VelocityComponent velocity;

    public ForceComponent() {
        this(100);
    }

    public ForceComponent(float weight) {
        this.weight = weight;
        this.force = Vector3.create();
    }

    @Override
    public void start() {
        velocity = getComponent(VelocityComponent.class);
    }

    @Override
    public void update() {
        velocity.addVelocity(force.x / weight, force.y / weight, force.z / weight);
    }

    public void addForce(float x, float y, float z) {
        force.add(x, y, z);
    }

    public void addForce(Vector3fc f) {
        force.add(f);
    }

    public void setForce(float x, float y, float z) {
        force.set(x, y, z);
    }

    public void setForce(Vector3fc f) {
        force.set(f);
    }

    public float getX() {
        return force.x;
    }

    public float getY() {
        return force.y;
    }

    public float getZ() {
        return force.z;
    }

    public Vector3f get() {
        return force;
    }
}
