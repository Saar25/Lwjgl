package engine.rendering;

public interface IRenderer {

    /**
     * Render to the scene using the render context
     *
     * @param context the render context
     */
    void render(RenderContext context);

    /**
     * Finish the rendering process
     * Invoked after finished rendering the scene
     */
    void finish();

    /**
     * Delete the renderer
     * Invoked when the program closes
     */
    void delete();

}
