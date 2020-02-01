package engine.models;

import maths.joml.Vector2f;
import maths.utils.Vector2;
import opengl.textures.ITexture;
import opengl.textures.Texture;
import opengl.textures.Texture2D;

public class Skin {

    private static final Vector2f offset = Vector2.create();

    private final ITexture texture;

    private int textureIndex = 0;
    private int textureRows = 1;
    private boolean transparent = false;

    private Skin(ITexture texture) {
        this.texture = texture;
    }

    public static Skin create() {
        return new Skin(Texture.NONE);
    }

    public static Skin of(ITexture texture) {
        return new Skin(texture == null ? Texture.NONE : texture);
    }

    public static Skin of(String textureFile) throws Exception {
        ITexture texture = Texture2D.of(textureFile);
        return new Skin(texture);
    }

    /**
     * Returns the texture of the skin
     *
     * @return the texture of the skin
     */
    public ITexture getTexture() {
        return texture;
    }

    /**
     * Cleans up the skin, should be called before closing up the program
     */
    public void cleanUp() {
        if (texture != null) {
            texture.delete();
        }
    }

    /**
     * Returns whether the skin has texture
     *
     * @return true if the skin has texture, else false
     */
    public boolean hasTexture() {
        return texture != Texture.NONE;
    }

    public Vector2f getTextureOffset() {
        offset.x = (float) (textureIndex % textureRows) / textureRows;
        offset.y = (float) (textureIndex / textureRows) / textureRows;
        return offset;
    }

    public Skin setTextureIndex(int textureIndex) {
        this.textureIndex = textureIndex;
        return this;
    }

    public int getTextureRows() {
        return textureRows;
    }

    public Skin setTextureRows(int textureRows) {
        this.textureRows = textureRows;
        return this;
    }

    public boolean isTransparent() {
        return transparent;
    }

    public Skin setTransparent(boolean transparent) {
        this.transparent = transparent;
        return this;
    }
}
