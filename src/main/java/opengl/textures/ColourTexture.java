package opengl.textures;

import opengl.utils.MemoryUtils;

public class ColourTexture implements ITexture {

    private final Texture2D texture;
    private final int r;
    private final int g;
    private final int b;

    public ColourTexture(int r, int g, int b) {
        this.texture = new Texture2D(1, 1);
        final byte[] bytes = new byte[]{(byte) r, (byte) g, (byte) b};
        getTexture().load(MemoryUtils.loadToByteBuffer(bytes));

        this.r = r;
        this.g = g;
        this.b = b;
    }

    public int getRed() {
        return r;
    }

    public int getGreen() {
        return g;
    }

    public int getBlue() {
        return b;
    }

    private Texture2D getTexture() {
        return texture;
    }

    @Override
    public void bind(int unit) {
        getTexture().bind(unit);
    }

    @Override
    public void bind() {
        getTexture().bind();
    }

    @Override
    public void unbind() {
        getTexture().unbind();
    }

    @Override
    public void delete() {
        getTexture().delete();
    }
}
