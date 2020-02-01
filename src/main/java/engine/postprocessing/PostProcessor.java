package engine.postprocessing;

import engine.rendering.RenderOutputData;
import opengl.fbos.IFbo;
import opengl.textures.Texture;

public interface PostProcessor {

    /**
     * Process the given data and return the new data
     *
     * @param renderOutputData the data
     */
    void process(RenderOutputData renderOutputData);

    /**
     * Returns the processed colour texture
     *
     * @return the processed colour texture
     */
    Texture getTexture();

    /**
     * Resize the fbo, should be called every
     * time the size of the window changed
     *
     * @param width  the width
     * @param height the height
     */
    void resize(int width, int height);

    /**
     * Blit the processed texture into the given fbo
     */
    void blitToFbo(IFbo fbo);

    /**
     * Blit the processed texture into the screen
     */
    void blitToScreen();

    /**
     * Clean up the post processor
     */
    void cleanUp();
}
