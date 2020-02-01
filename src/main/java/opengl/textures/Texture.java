package opengl.textures;

import opengl.constants.DataType;
import opengl.constants.FormatType;
import opengl.utils.GlConfigs;
import opengl.utils.MemoryUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Texture implements ITexture {

    private static int activeTexture = 0;
    private static int[] boundTextures = new int[32];

    public static final Texture NONE = new Texture(0, TextureTarget.TEXTURE_2D);

    private final int id;
    private final int target;

    private final TextureFunctions functions;

    private boolean deleted = false;

    private Texture(int id, TextureTarget target) {
        this.id = id;
        this.target = target.get();
        this.functions = new TextureFunctions(this, target);
        this.bind();
    }

    public static Texture create() {
        return Texture.create(TextureTarget.TEXTURE_2D);
    }

    public static TextureBuilder builder() {
        return new TextureBuilder();
    }

    public static Texture create(TextureTarget target) {
        final int id = GL11.glGenTextures();
        return new Texture(id, target);
    }

    /**
     * Binds the given texture to the given texture unit
     * this method takes care of null check so it is the same as
     * if (texture != null) texture.bind(unit);
     *
     * @param texture the texture to bind
     * @param unit    the unit to bind to
     */
    public static void bind(ITexture texture, int unit) {
        if (texture != null) {
            texture.bind(unit);
        }
    }

    /**
     * Unbinds the texture on the given unit
     *
     * @param unit the unit to unbind
     */
    public static void unbind(int unit) {
        NONE.bind(unit);
    }

    /**
     * Returns the functions of the texture
     *
     * @return the functions
     */
    public TextureFunctions getFunctions() {
        return functions;
    }

    /**
     * Attaches the texture to the current bound fbo
     *
     * @param attachment the attachment of the texture
     * @param level      the mip map level of texture to attach
     */
    public void attachToFbo(int attachment, int level) {
        GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, attachment, id, level);
    }

    public void allocate(TextureTarget target, int level, FormatType internalFormat, int width,
                         int height, int border, FormatType format, DataType type, ByteBuffer data) {
        bind();
        GL11.glTexImage2D(target.get(), level, internalFormat.get(), width,
                height, border, format.get(), type.get(), data);
    }

    public void allocate(TextureTarget target, int level, FormatType internalFormat, int width,
                         int height, int border, FormatType format, DataType type, IntBuffer data) {
        bind();
        GL11.glTexImage2D(target.get(), level, internalFormat.get(), width,
                height, border, format.get(), type.get(), data);
    }

    public void allocate(TextureTarget target, int level, FormatType internalFormat, int width,
                         int height, int border, FormatType format, DataType type, FloatBuffer data) {
        bind();
        GL11.glTexImage2D(target.get(), level, internalFormat.get(), width,
                height, border, format.get(), type.get(), data);
    }

    public void allocateMultisample(int samples, FormatType iFormat, int width, int height) {
        bind();
        GL32.glTexImage2DMultisample(target, samples, iFormat.get(), width, height, true);
    }

    public void load(TextureTarget target, int level, int xOffset, int yOffset, int width,
                     int height, FormatType format, DataType type, ByteBuffer data) {
        bind();
        GL11.glTexSubImage2D(target.get(), level, xOffset, yOffset, width,
                height, format.get(), type.get(), data);
    }

    public void load(TextureTarget target, int level, int xOffset, int yOffset, int width,
                     int height, FormatType format, DataType type, IntBuffer data) {
        bind();
        GL11.glTexSubImage2D(target.get(), level, xOffset, yOffset, width,
                height, format.get(), type.get(), data);
    }

    public int getWidth() {
        bind0();
        return GL11.glGetTexLevelParameteri(target, 0, GL11.GL_TEXTURE_WIDTH);
    }

    public int getHeight() {
        bind0();
        return GL11.glGetTexLevelParameteri(target, 0, GL11.GL_TEXTURE_HEIGHT);
    }

    public ByteBuffer getPixelsBuffer() {
        final ByteBuffer byteBuffer = MemoryUtils.allocByte(4 * getWidth() * getHeight());
        GL11.glGetTexImage(target, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, byteBuffer);
        return byteBuffer;
    }

    @Override
    public void bind(int unit) {
        if (GlConfigs.CACHE_STATE || activeTexture != unit) {
            activeTexture = unit;
            GL13.glActiveTexture(GL13.GL_TEXTURE0 + unit);
        }
        bind();
    }

    @Override
    public void bind() {
        if (GlConfigs.CACHE_STATE || boundTextures[activeTexture] != id) {
            bind0();
        }
    }

    private void bind0() {
        boundTextures[activeTexture] = id;
        GL11.glBindTexture(target, id);
    }

    @Override
    public void unbind() {
        NONE.bind();
    }

    @Override
    public void delete() {
        if (!deleted) {
            GL11.glDeleteTextures(id);
            deleted = true;
        }
    }
}
