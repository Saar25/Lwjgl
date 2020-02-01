package engine.postprocessing;

import engine.rendering.RenderOutputData;
import glfw.window.Window;
import opengl.constants.DataType;
import opengl.constants.FormatType;
import opengl.constants.RenderMode;
import opengl.fbos.Fbo;
import opengl.fbos.FboTarget;
import opengl.fbos.IFbo;
import opengl.fbos.attachment.TextureAttachment;
import opengl.shaders.RenderState;
import opengl.shaders.ShadersProgram;
import opengl.textures.Texture;
import opengl.textures.TextureConfigs;
import opengl.utils.GlBuffer;
import opengl.utils.GlRendering;
import opengl.utils.GlUtils;

public abstract class AbstractPostProcessor implements PostProcessor {

    private final ShadersProgram<RenderOutputData> shadersProgram;
    private Fbo fbo;

    public AbstractPostProcessor(String fragFile) throws Exception {
        this("/engine/postprocessing/simpleVertex.glsl", fragFile);
    }

    public AbstractPostProcessor(String vertFile, String fragFile) throws Exception {
        this.shadersProgram = ShadersProgram.create(vertFile, fragFile);
    }

    /**
     * Create an fbo with the given width and height values
     *
     * @param width  the width of the fbo
     * @param height the height of the fbo
     * @return the created fbo
     */
    protected Fbo createFbo(int width, int height) {
        final Fbo fbo = Fbo.create(width, height);
        fbo.addAttachment(TextureAttachment.ofColour(0, new TextureConfigs(
                FormatType.RGB8, FormatType.RGB, DataType.U_BYTE)));
        return fbo;
    }

    protected void beforeProcess(RenderOutputData renderOutputData) {

    }

    /**
     * Returns the fbo of the post processor
     *
     * @return the fbo
     */
    protected final Fbo getFbo() {
        return fbo == null ? (fbo = createFbo(Window.current().getWidth(), Window.current().getHeight())) : fbo;
    }

    /**
     * Returns the shaders program of the post processor
     *
     * @return the shaders program
     */
    protected final ShadersProgram<RenderOutputData> getShadersProgram() {
        return shadersProgram;
    }

    protected void draw() {
        GlRendering.drawArrays(RenderMode.TRIANGLE_STRIP, 0, 4);
    }

    @Override
    public void process(RenderOutputData renderOutputData) {
        beforeProcess(renderOutputData);

        fbo.bind(FboTarget.DRAW_FRAMEBUFFER);
        GlUtils.clear(GlBuffer.COLOUR);
        getShadersProgram().bind();

        getShadersProgram().updatePerRenderUniforms(new RenderState<>(null, renderOutputData, null));
        getShadersProgram().updatePerInstanceUniforms(new RenderState<>(null, renderOutputData, null));

        draw();

        getShadersProgram().unbind();
    }

    @Override
    public final void resize(int width, int height) {
        if (getFbo() == null) {
            fbo = createFbo(width, height);
        } else if (!getFbo().isSized(width, height)) {
            getFbo().delete();
            fbo = createFbo(width, height);
        }
    }

    @Override
    public final Texture getTexture() {
        return getFbo().getAttachments().get(0).getTexture();
    }

    @Override
    public final void blitToFbo(IFbo fbo) {
        getFbo().blitFbo(fbo);
    }

    @Override
    public final void blitToScreen() {
        getFbo().blitToScreen();
    }

    @Override
    public final void cleanUp() {
        getFbo().delete();
        getShadersProgram().delete();
    }
}
