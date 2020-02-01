package engine.light;

import maths.joml.Vector3f;
import maths.joml.Vector3fc;

public abstract class ModifiableLight extends AbstractLight implements ILight {

    protected ModifiableLight() {

    }

    @Override
    public Vector3f getPosition() {
        return this.position;
    }

    @Override
    public Vector3f getColour() {
        return this.colour;
    }

    @Override
    public Vector3f getAttenuation() {
        return this.attenuation;
    }

    public void setPosition(Vector3fc position) {
        this.position.set(position);
    }

    public void setColour(Vector3fc colour) {
        this.colour.set(colour);
    }

    public void setAttenuation(Vector3fc attenuation) {
        this.attenuation.set(attenuation);
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }
}
