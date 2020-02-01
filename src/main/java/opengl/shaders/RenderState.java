package opengl.shaders;

import engine.rendering.Camera;
import engine.rendering.Renderer;

public class RenderState<T> {

    private final Renderer<T> renderer;
    private final T instance;
    private final Camera camera;

    public RenderState(Renderer<T> renderer, T instance, Camera camera) {
        this.renderer = renderer;
        this.instance = instance;
        this.camera = camera;
    }

    public RenderState(Renderer<T> renderer, Camera camera) {
        this.renderer = renderer;
        this.instance = null;
        this.camera = camera;
    }

    public Renderer<T> getRenderer() {
        return renderer;
    }

    public Camera getCamera() {
        return camera;
    }

    public T getInstance() {
        return instance;
    }

    public boolean hasInstance() {
        return instance != null;
    }
}
