package tegui.objects;

import opengl.textures.Texture2D;
import tegui.GuiObject;
import tegui.style.Style;

public class TImage extends GuiObject {

    public TImage(Texture2D texture) {
        super(texture);

        final Style style = new Style();
        style.backgroundColour.set(0, 0, 0, 0);
        getStyle().setParent(style);
    }
}
