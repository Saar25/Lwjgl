package engine.rendering.background;

import engine.rendering.RenderContext;
import engine.util.lengths.Proportion;
import opengl.utils.GlUtils;
import tegui.GuiObject;
import tegui.render.GuiRenderer;
import tegui.style.property.CornersColours;

public class BackgroundGradientColour implements Background {

    private static final GuiObject quad = createQuad();
    private static final GuiRenderer renderer = new GuiRenderer();
    private static final RenderContext context = new RenderContext();

    private static GuiObject createQuad() {
        final GuiObject quad = new GuiObject();
        quad.getStyle().x.set(Proportion.of(0));
        quad.getStyle().y.set(Proportion.of(0));
        quad.getStyle().width.set(Proportion.of(1));
        quad.getStyle().height.set(Proportion.of(1));
        return quad;
    }

    @Override
    public void clear() {
        boolean lines = GlUtils.isPolygonLines();
        if (lines) {
            GlUtils.drawPolygonFill();
        }
        GlUtils.clearColourAndDepthBuffer();
        renderer.process(quad);
        renderer.render(context);
        renderer.finish();
        if (lines) {
            GlUtils.drawPolygonLine();
        }
    }

    @Override
    public void delete() {
        renderer.cleanUp();
    }

    public CornersColours getColours() {
        return quad.getStyle().backgroundColour;
    }
}
