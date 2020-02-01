package opengl.fbos;

import opengl.textures.parameters.MagFilterParameter;
import opengl.utils.GlBuffer;

public interface IFbo {

    /**
     * Blit the fbo to the given fbo
     */
    default void blitFbo(IFbo fbo) {
        blitFbo(fbo, MagFilterParameter.NEAREST, GlBuffer.COLOUR, GlBuffer.DEPTH);
    }

    void blitFbo(IFbo fbo, MagFilterParameter filter, GlBuffer... buffers);

    /**
     * Returns the width of the fbo
     *
     * @return the width
     */
    int getWidth();

    /**
     * Returns the height of the fbo
     *
     * @return the height
     */
    int getHeight();

    /**
     * Bind the fbo
     */
    default void bind() {
        bind(FboTarget.FRAMEBUFFER);
    }

    /**
     * Bind the fbo to the given target
     *
     * @param target the fbo target
     */
    void bind(FboTarget target);

    /**
     * Unbind the fbo
     */
    default void unbind() {
        unbind(FboTarget.FRAMEBUFFER);
    }

    /**
     * Unbind the fbo from the given target
     *
     * @param target the fbo target
     */
    void unbind(FboTarget target);

    /**
     * Delete the fbo
     */
    void delete();

}
