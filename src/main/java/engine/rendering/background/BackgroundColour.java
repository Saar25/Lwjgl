package engine.rendering.background;

import maths.joml.Vector3f;
import maths.joml.Vector3fc;
import maths.utils.Vector3;
import opengl.utils.GlUtils;
import tegui.style.property.IColour;

public class BackgroundColour implements Background {

    private final Vector3f colour;

    public BackgroundColour(IColour colour) {
        this.colour = Vector3.of(colour.getRed(), colour.getGreen(), colour.getBlue());
    }

    public BackgroundColour(Vector3fc colour) {
        this.colour = Vector3.of(colour);
    }

    public BackgroundColour(float r, float g, float b) {
        this.colour = Vector3.of(r, g, b);
    }

    @Override
    public void clear() {
        GlUtils.setClearColour(colour);
        GlUtils.clearColourAndDepthBuffer();
    }

    @Override
    public void delete() {
        // Not implemented
    }
}
