package engine.light;

import maths.joml.Vector3f;
import maths.joml.Vector3fc;
import maths.utils.Vector3;

public class DirectionalLight extends AbstractLight implements ILight {

    private final Vector3f position = Vector3.create();
    private final Vector3f direction = Vector3.create();

    public DirectionalLight() {
        this.attenuation.set(Vector3.of(1, 0, 0));
    }

    public DirectionalLight(Vector3f direction, Vector3f colour, float intensity) {
        this.direction.set(direction);
        this.colour.set(colour);
        this.attenuation.set(Vector3.of(1, 0, 0));
        this.intensity = intensity;
    }

    @Override
    public Vector3fc getPosition() {
        // TODO position should be the direction but reversed (mul -1)
        return position.set(direction).mul(1);
    }

    public Vector3f getDirection() {
        return direction;
    }

    public void setDirection(Vector3f direction) {
        getDirection().set(direction).normalize();
        this.position.set(direction).mul(-1);
    }

    @Override
    public Vector3f getColour() {
        return colour;
    }

    public void setColour(Vector3fc colour) {
        getColour().set(colour);
    }

    @Override
    public float getIntensity() {
        return this.intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    @Override
    public boolean isDirectional() {
        return true;
    }

}
