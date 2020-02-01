package engine.light;

import maths.joml.Vector3f;
import maths.utils.Vector3;

public class PointLight extends ModifiableLight implements ILight {

    public PointLight() {

    }

    public PointLight(Vector3f position, Vector3f colour, Vector3f attenuation, float intensity) {
        this.position.set(position);
        this.colour.set(colour);
        this.attenuation.set(attenuation);
        this.intensity = intensity;
    }

    @Override
    public boolean isDirectional() {
        return false;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Vector3f position = Vector3.create();
        private Vector3f colour = Vector3.of(1, 1, 1);
        private Vector3f attenuation = Vector3.of(1, 0, 0);
        private float intensity = 1;

        public Builder setPosition(float x, float y, float z) {
            this.position.set(x, y, z);
            return this;
        }

        public Builder setPosition(Vector3f v) {
            this.position.set(v);
            return this;
        }

        public Builder setColour(float r, float g, float b) {
            this.colour.set(r, g, b);
            return this;
        }

        public Builder setColour(Vector3f v) {
            this.colour.set(v);
            return this;
        }

        public Builder setAttenuation(float c, float l, float e) {
            this.attenuation.set(c, l, e);
            return this;
        }

        public Builder setAttenuation(Vector3f v) {
            this.attenuation.set(v);
            return this;
        }

        public Builder setIntensity(float intensity) {
            this.intensity = intensity;
            return this;
        }

        public PointLight create() {
            return new PointLight(position, colour, attenuation, intensity);
        }

    }
}
