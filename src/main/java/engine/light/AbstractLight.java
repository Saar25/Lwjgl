package engine.light;

import maths.joml.Vector3f;
import maths.joml.Vector3fc;
import maths.utils.Vector3;

public abstract class AbstractLight extends Light implements ILight {

    protected final Vector3f position = Vector3.create();
    protected final Vector3f colour = Vector3.create();
    protected final Vector3f attenuation = Vector3.create();
    protected float intensity = 1;

    @Override
    public Vector3fc getPosition() {
        return position;
    }

    @Override
    public Vector3fc getColour() {
        return colour;
    }

    @Override
    public Vector3fc getAttenuation() {
        return attenuation;
    }

    @Override
    public float getIntensity() {
        return intensity;
    }
}
