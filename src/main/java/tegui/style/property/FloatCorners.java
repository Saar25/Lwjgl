package tegui.style.property;

import maths.joml.Vector4f;
import tegui.style.StyleProperty;

public interface FloatCorners extends StyleProperty {

    float get(boolean right, boolean top);

    float getTopLeft();

    float getTopRight();

    float getBottomRight();

    float getBottomLeft();

    Vector4f asVector();

}
