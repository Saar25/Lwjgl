package engine.rendering;

import opengl.shaders.RenderState;
import opengl.shaders.ShadersProgram;

public abstract class AbstractRenderer3D<T> extends Renderer3D<T> {

    private final ShadersProgram<T> shadersProgram;

    public AbstractRenderer3D(ShadersProgram<T> shadersProgram) {
        this.shadersProgram = shadersProgram;
    }

    public AbstractRenderer3D(String vertFile, String fragFile) throws Exception {
        this.shadersProgram = ShadersProgram.create(vertFile, fragFile);
    }

    @Override
    public void render(RenderContext context) {
        if (renderList.isEmpty()) {
            return;
        }

        getShadersProgram().bind();

        final RenderState<T> renderState = new RenderState<>(this, context.getCamera());
        getShadersProgram().updatePerRenderUniforms(renderState);
        onRenderStage(renderState);

        for (T instance : renderList) {
            final RenderState<T> instanceState = new RenderState<>(this, instance, context.getCamera());

            if (!canCullInstance(instanceState)) {
                getShadersProgram().updatePerInstanceUniforms(instanceState);

                onInstanceRender(instanceState);

                render(instance);
            }
        }

        getShadersProgram().unbind();
    }

    @Override
    public void cleanUp() {
        getShadersProgram().delete();
        onDelete();
    }

    /**
     * Returns the shaders program of the renderer
     *
     * @return the shaders program of the renderer
     */
    protected ShadersProgram<T> getShadersProgram() {
        return shadersProgram;
    }

    /**
     * Invoked when the renderer starts rendering all the processed instances
     *
     * @param renderState the render state of the renderer
     */
    protected void onRenderStage(RenderState<T> renderState) {

    }

    /**
     * Invoked before rendering an instance
     *
     * @param instanceState the instance rendering state
     */
    protected void onInstanceRender(RenderState<T> instanceState) {

    }

    /**
     * Decide if the instance can be culled from rendering
     *
     * @param instanceState the instance rendering state
     * @return true if the instance can be culled, false otherwise
     */
    protected boolean canCullInstance(RenderState<T> instanceState) {
        return false;
    }

    /**
     * Invoked when the renderer is deleted
     */
    protected void onDelete() {

    }

    /**
     * Render the given instance
     *
     * @param instance the instance to render
     */
    protected abstract void render(T instance);

}
