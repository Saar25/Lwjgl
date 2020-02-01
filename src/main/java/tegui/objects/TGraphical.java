package tegui.objects;

import opengl.constants.DataType;
import opengl.constants.FormatType;
import opengl.textures.Texture2D;
import opengl.textures.TextureConfigs;
import tegui.GuiObject;
import tegui.graphics.BufferedGraphics;
import tegui.graphics.Graphics;
import tegui.style.property.Colour;
import tegui.style.property.IColour;

public abstract class TGraphical extends GuiObject {

    private final Graphics graphics;

    private IColour background = null;

    public TGraphical(int width, int height) {
        super(new Texture2D(width, height, configs()));
        this.graphics = new BufferedGraphics(getTexture());
        getStyle().dimensions.set(width, height);
    }

    private static TextureConfigs configs() {
        return new TextureConfigs(FormatType.RGBA8, FormatType.RGBA, DataType.U_BYTE);
    }

    public void setBackground(IColour background) {
        this.background = background;
    }

    @Override
    public void process() {
        graphics.clear(background);
        graphics.setColour(Colour.BLACK);
        paint(graphics);
        graphics.process();
        super.process();
    }

    public abstract void paint(Graphics g);

    @Override
    public void delete() {
        super.delete();
        graphics.delete();
    }
}
