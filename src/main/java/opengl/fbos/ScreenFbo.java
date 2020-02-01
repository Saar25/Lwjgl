package opengl.fbos;

import glfw.window.Window;
import opengl.textures.parameters.MagFilterParameter;
import opengl.utils.GlBuffer;
import opengl.utils.GlUtils;
import org.lwjgl.opengl.GL11;

public class ScreenFbo implements IFbo {

    private static final ScreenFbo instance = new ScreenFbo();

    private ScreenFbo() {

    }

    public static ScreenFbo getInstance() {
        return instance;
    }

    private Fbo getFbo() {
        return Fbo.NULL;
    }

    @Override
    public void blitFbo(IFbo fbo, MagFilterParameter filter, GlBuffer... buffers) {
        getFbo().blitFbo(fbo, filter, buffers);
    }

    @Override
    public int getWidth() {
        return Window.current().getWidth();
    }

    @Override
    public int getHeight() {
        return Window.current().getHeight();
    }

    @Override
    public void bind(FboTarget target) {
        getFbo().bind(target);
        GlUtils.setViewport(0, 0, getWidth(), getHeight());
        switch (target) {
            case DRAW_FRAMEBUFFER:
                GL11.glDrawBuffer(GL11.GL_BACK);
                break;
            case READ_FRAMEBUFFER:
                GL11.glReadBuffer(GL11.GL_NONE);
                break;
            default:
        }
    }

    @Override
    public void unbind(FboTarget target) {
        getFbo().unbind(target);
    }

    @Override
    public void delete() {
        // Cannot delete screen fbo
    }
}
