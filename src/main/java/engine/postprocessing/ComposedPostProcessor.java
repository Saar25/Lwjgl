package engine.postprocessing;

import engine.rendering.RenderOutputData;
import opengl.fbos.IFbo;
import opengl.textures.Texture;

import java.util.Arrays;
import java.util.List;

public abstract class ComposedPostProcessor implements PostProcessor {

    private final List<PostProcessor> postProcessors;

    public ComposedPostProcessor(PostProcessor... postProcessors) {
        this.postProcessors = Arrays.asList(postProcessors);
    }

    protected <T extends PostProcessor> T getPostProcessor(Class<T> postProcessorClass) {
        for (PostProcessor postProcessor : postProcessors) {
            if (postProcessorClass.isInstance(postProcessor)) {
                return postProcessorClass.cast(postProcessor);
            }
        }
        return null;
    }

    protected void beforeProcess(RenderOutputData renderOutputData) {

    }

    private PostProcessor getLast() {
        return postProcessors.get(postProcessors.size() - 1);
    }

    @Override
    public final void process(RenderOutputData renderOutputData) {
        beforeProcess(renderOutputData);
        for (PostProcessor postProcessor : postProcessors) {
            postProcessor.process(renderOutputData);
            renderOutputData = new RenderOutputData(postProcessor.getTexture(),
                    renderOutputData.getNormal(), renderOutputData.getDepth());
        }
    }

    @Override
    public Texture getTexture() {
        return getLast().getTexture();
    }

    @Override
    public void resize(int width, int height) {
        for (PostProcessor postProcessor : postProcessors) {
            postProcessor.resize(width, height);
        }
    }

    @Override
    public void blitToFbo(IFbo fbo) {
        getLast().blitToFbo(fbo);
    }

    @Override
    public void blitToScreen() {
        getLast().blitToScreen();
    }

    @Override
    public void cleanUp() {
        for (PostProcessor postProcessor : postProcessors) {
            postProcessor.cleanUp();
        }
    }
}
