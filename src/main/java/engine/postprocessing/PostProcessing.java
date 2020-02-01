package engine.postprocessing;

import engine.rendering.RenderOutputData;
import engine.util.ModifiableList;
import opengl.fbos.Fbo;
import opengl.fbos.IFbo;
import opengl.objects.Vao;
import opengl.textures.Texture;
import opengl.utils.GlUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Class the in charge of dealing with the post
 * processing pipeline, from binding the needed fbo
 * to process the texture by the given post processors
 * and rendering the final image into the screen.
 *
 * @author Saar ----
 * @version 1.0
 * @since 2018-11-4
 */
public class PostProcessing {

    private final List<PostProcessor> postProcessors;
    private final ModifiableList<PostProcessor> modifiablePostProcessors;

    public PostProcessing() {
        this.postProcessors = new ArrayList<>();
        this.modifiablePostProcessors = new ModifiableList<>(postProcessors);
    }

    /**
     * Resize all of the post processors, should be
     * called whenever the size of the window changed
     *
     * @param width  the width
     * @param height the height
     */
    public void resize(int width, int height) {
        postProcessors.forEach(p -> p.resize(width, height));
    }

    public void add(PostProcessor postProcessor) {
        postProcessors.add(postProcessor);
    }

    public void remove(PostProcessor postProcessor) {
        postProcessors.remove(postProcessor);
    }

    /**
     * Processes a texture with all the post processors
     * received earlier. The processed texture can be
     * accessed with getTexture().
     *
     * @param draw the fbo to draw to
     * @param read the fbo to read from
     */
    public void processToFbo(IFbo draw, Fbo read) {
        if (postProcessors.size() == 0) {
            read.blitFbo(draw);
        } else {
            processToFbo(draw, read.getAttachments().get(0).getTexture(),
                    read.getDepthAttachment().getTexture());
        }
    }

    public void processToFbo(IFbo draw, IFbo read, RenderOutputData renderOutputData) {
        if (postProcessors.size() == 0) {
            read.blitFbo(draw);
        } else {
            processToFbo(draw, renderOutputData);
        }
    }

    /**
     * Processes a texture with all the post processors
     * received earlier. The processed texture can be
     * accessed with getTexture().
     *
     * @param fbo          the fbo to process to
     * @param texture      the texture to process
     * @param depthTexture the depth texture use
     */
    public void processToFbo(IFbo fbo, Texture texture, Texture depthTexture) {
        process(texture, depthTexture);
        if (postProcessors.size() > 0) {
            postProcessors.get(postProcessors.size() - 1).blitToFbo(fbo);
        }
    }

    public void processToFbo(IFbo fbo, RenderOutputData renderOutputData) {
        process(renderOutputData);
        if (postProcessors.size() > 0) {
            postProcessors.get(postProcessors.size() - 1).blitToFbo(fbo);
        }
    }

    /**
     * Processes a texture with all the post processors
     * received earlier and displays the result to the screen
     *
     * @param fbo the fbo that contains the textures to process
     */
    public void processToScreen(Fbo fbo) {
        if (postProcessors.size() == 0) {
            fbo.blitToScreen();
        } else {
            processToScreen(fbo.getAttachments().get(0).getTexture(),
                    fbo.getDepthAttachment().getTexture());
        }
    }


    /**
     * Processes a texture with all the post processors
     * received earlier and displays the result to the screen
     *
     * @param texture      the colour texture to process
     * @param depthTexture the depth texture to process
     */
    public void processToScreen(Texture texture, Texture depthTexture) {
        process(texture, depthTexture);
        if (postProcessors.size() > 0) {
            postProcessors.get(postProcessors.size() - 1).blitToScreen();
        }
    }

    public void process(Texture texture, Texture depthTexture) {
        boolean line = GlUtils.isPolygonLines();
        GlUtils.disableDepthTest(); // No depth test is required
        GlUtils.drawPolygonFill();  // The quad should be filled
        GlUtils.disableBlending();  // No blending is required
        GlUtils.disableCulling();   // No culling is required
        Vao.bindIfNone();           // The quad should be drawn without a model
        // and vao cannot be bound to zero
        for (PostProcessor postProcessor : postProcessors) {
            postProcessor.process(new RenderOutputData(texture, Texture.NONE, depthTexture));
            texture = postProcessor.getTexture();
        }

        if (line) GlUtils.drawPolygonLine();
    }

    public void process(RenderOutputData renderOutputData) {
        boolean line = GlUtils.isPolygonLines();
        GlUtils.disableDepthTest(); // No depth test is required
        GlUtils.drawPolygonFill();  // The quad should be filled
        GlUtils.disableBlending();  // No blending is required
        GlUtils.disableCulling();   // No culling is required
        Vao.bindIfNone();           // The quad should be drawn without a model
        // and vao cannot be bound to zero
        Texture texture = renderOutputData.getColour();
        for (PostProcessor postProcessor : postProcessors) {
            postProcessor.process(new RenderOutputData(texture,
                    renderOutputData.getNormal(), renderOutputData.getDepth()));
            texture = postProcessor.getTexture();
        }

        if (line) GlUtils.drawPolygonLine();
    }

    public Texture getOutput() {
        if (postProcessors.size() > 0) {
            return postProcessors.get(postProcessors.size() - 1).getTexture();
        } else {
            return Texture.NONE;
        }
    }

    /**
     * Cleans the renderer, should be called when closing up the program
     */
    public void cleanUp() {
        for (PostProcessor processor : postProcessors) {
            processor.cleanUp();
        }
    }

}
