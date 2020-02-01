package tegui.style.property;

import maths.joml.Vector3f;
import maths.joml.Vector4f;

public interface IColour {

    float getRed();

    float getGreen();

    float getBlue();

    float getAlpha();

    default int asInt() {
        final int r = (int) (this.getRed() * 255) << 24;
        final int g = (int) (this.getGreen() * 255) << 16;
        final int b = (int) (this.getBlue() * 255) << 8;
        final int a = (int) (this.getAlpha() * 255);
        return r + g + b + a;
    }

    Vector3f rgbVector();

    Vector4f rgbaVector();

    default String string() {
        return String.format("Colour: Red = %.3f, Green = %.3f, Blue = %.3f, Alpha = %.3f",
                getRed(), getGreen(), getBlue(), getAlpha());
    }
}
