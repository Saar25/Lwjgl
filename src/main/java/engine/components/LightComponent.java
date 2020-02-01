package engine.components;

import engine.componentsystem.GameComponent;
import engine.light.Light;
import engine.light.PointLight;
import maths.joml.Vector3f;
import maths.utils.Vector3;

public class LightComponent extends GameComponent {

    private final PointLight light;

    public LightComponent(Vector3f colour, float intensity) {
        this.light = new PointLight(Vector3.create(), colour, Vector3.of(.3f, 0.0f, 0.001f), intensity);
    }

    public LightComponent(PointLight light) {
        this.light = light;
    }

    @Override
    public void update() {
        light.setPosition(getTransform().getPosition());
    }

    public Light getLight() {
        return light;
    }
}
