package engine.components.particles;

import engine.componentsystem.GameComponent;
import maths.utils.Vector3;
import maths.joml.Vector3f;

public class SpherePositionStarting extends GameComponent {

    private final float radius;

    public SpherePositionStarting(float radius) {
        this.radius = radius;
    }

    @Override
    public void start() {
        float x = (float) (Math.random() - 0.5);
        float y = (float) (Math.random() - 0.5);
        float z = (float) (Math.random() - 0.5);
        Vector3f v = Vector3.of(x, y, z).normalize(radius);
        getTransform().setPosition(v);
    }
}
