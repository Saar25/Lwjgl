package engine.effects;

import engine.util.node.GGroup;
import opengl.fbos.Fbo;
import opengl.textures.Texture;

public abstract class ExtraRenderEffect extends Effect {

    protected final Fbo fbo;

    protected final int width;
    protected final int height;

    protected ExtraRenderEffect(GGroup<?> group, int width, int height) {
        super(group);
        this.width = width;
        this.height = height;
        this.fbo = createFbo();
        setEnabled(true);
    }

    protected abstract Fbo createFbo();

    public Texture getTexture() {
        return fbo.getAttachments().get(0).getTexture();
    }

    @Override
    public void onDelete() {
        this.fbo.delete();
    }
}
