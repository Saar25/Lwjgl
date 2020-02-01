package tegui;

import engine.rendering.Spatial;
import maths.objects.Rectangle;
import opengl.textures.Texture2D;
import tegui.render.GuiRenderer;
import tegui.style.Style;
import tegui.style.Styleable;
import tegui.style.property.Radiuses;

/**
 * This class represent an object inside a GuiComponent.
 * Every GuiComponent is composed by at least one GuiObject which
 * will be rendered by the renderer.
 *
 * @author Saar ----
 * @version 1.2
 * @since 18.2.2018
 */
public class GuiObject extends Spatial implements Styleable {

    private final Style style;
    private final Texture2D texture;

    public GuiObject() {
        this(null);
    }

    public GuiObject(Texture2D texture) {
        this.texture = texture;
        this.style = new Style();
    }

    public boolean inTouch(float x, float y) {
        final Radiuses radiuses = style.borderRadiusAttribute.get();

        if (radiuses.isZero()) {
            return style.bounds.contains(x, y);
        }

        float xBounds = style.bounds.getX();
        float yBounds = style.bounds.getY();
        float wBounds = style.bounds.getW();
        float hBounds = style.bounds.getH();

        float radius = radiuses.get(x > xBounds + wBounds / 2, y < yBounds + hBounds / 2);
        radius = Math.min(radius, wBounds / 2);
        radius = Math.min(radius, hBounds / 2);

        Rectangle bordersV = new Rectangle(xBounds + radius, yBounds, wBounds - radius * 2, hBounds);
        Rectangle bordersH = new Rectangle(xBounds, yBounds + radius, wBounds, hBounds - radius * 2);
        if (bordersV.contains(x, y) || bordersH.contains(x, y)) {
            return true;
        }

        float cx = style.bounds.xCenter();
        float cy = style.bounds.yCenter();
        cx = x < cx ? xBounds + radius : xBounds + wBounds - radius;
        cy = y < cy ? yBounds + radius : yBounds + hBounds - radius;
        float dx = (cx - x);
        float dy = (cy - y);

        return radius * radius > dx * dx + dy * dy;
    }

    public boolean hasTexture() {
        return texture != null;
    }

    public Texture2D getTexture() {
        return texture;
    }

    @Override
    public Style getStyle() {
        return style;
    }

    @Override
    public void process() {
        GuiRenderer.getInstance().process(this);
    }

    @Override
    public void delete() {
        if (hasTexture()) texture.delete();
    }
}
