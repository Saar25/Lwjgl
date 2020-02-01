package engine.rendering;

import java.util.LinkedList;
import java.util.List;

public abstract class Renderer<T> implements IRenderer {

    protected final List<T> renderList = new LinkedList<>();

    /**
     * Process a T that will be rendered once when calling {@link Renderer#render(RenderContext)}
     *
     * @param toProcess the T to render
     */
    public void process(T toProcess) {
        renderList.add(toProcess);
    }

    /**
     * Returns whether any objects have been processed to render
     *
     * @return true is the render list is not empty false otherwise
     */
    public boolean anyProcessed() {
        return renderList.size() > 0;
    }

    @Override
    public void finish() {
        this.renderList.clear();
    }

    /**
     * Clean up the renderer
     */
    public abstract void cleanUp();

    @Override
    public void delete() {
        cleanUp();
    }

    private static final RenderContext CONTEXT = new RenderContext();

    /**
     * Returns the context of the current render
     *
     * @return the context of the current render
     */
    public static RenderContext getContext() {
        return Renderer.CONTEXT;
    }
}
