package engine.light;

import maths.joml.Vector3f;

public class AmbientLight extends AbstractLight implements ILight {

    public AmbientLight(Vector3f colour, float intensity) {
        this.position.set(0, 0, 0);
        this.colour.set(colour);
        this.attenuation.set(1, 0, 0);
        this.intensity = intensity;
    }

    @Override
    public boolean isDirectional() {
        return false;
    }
}
