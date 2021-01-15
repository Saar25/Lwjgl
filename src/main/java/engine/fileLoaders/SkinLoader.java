package engine.fileLoaders;

import engine.models.Skin;
import opengl.textures.ITexture;
import opengl.textures.Texture2D;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public final class SkinLoader {

    private static final Map<String, ITexture> textures = new HashMap<>();
    private static final byte minTransparency = 127;

    private SkinLoader() {

    }

    public static Skin load(String textureFile) throws Exception {
        ITexture cachedTexture = textures.get(textureFile);
        if (cachedTexture != null) {
            return Skin.of(cachedTexture);
        }

        Info info = loadInfo(textureFile);
        Texture2D texture = Texture2D.of(info.width, info.height);
        texture.load(info.data);
        textures.put(textureFile, texture);

        final Skin skin = Skin.of(textureFile);
        skin.setTransparent(isTransparent(info.data));
        return skin;
    }

    private static boolean isTransparent(ByteBuffer data) {
        for (int i = 3; i < data.limit(); i += 4) {
            if (data.get(i) < minTransparency) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the file's format
     *
     * @param file the file
     * @return the format
     */
    private static String getFormat(String file) {
        return file.substring(file.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * Loads a texture file into width, height and data
     * and return the values as a {@link Info} object
     *
     * @param file the texture file path
     * @return the texture info
     * @throws Exception if could not load the texture
     */
    private static Info loadInfo(String file) throws Exception {
        String fileFormat = getFormat(file);
        switch (fileFormat) {
            case "png":
                return decodePng(file);
            default:
                throw new Exception("File format " + fileFormat + " is not supported");
        }
    }

    /**
     * Decodes a PNG format image into a width, height, and byte buffer
     *
     * @param textureFile the path of the image, must be a PNG
     * @return {@link Info} necessary to init the texture
     * @throws Exception if could not load the image
     */
    private static Info decodePng(String textureFile) throws Exception {
        PNGDecoder decoder = new PNGDecoder(SkinLoader.class.getResourceAsStream(textureFile));

        ByteBuffer textureBuffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
        decoder.decode(textureBuffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
        textureBuffer.flip();

        return new Info(decoder.getWidth(), decoder.getHeight(), textureBuffer);
    }

    private static class Info {
        private final int width;
        private final int height;
        private final ByteBuffer data;

        private Info(int width, int height, ByteBuffer data) {
            this.width = width;
            this.height = height;
            this.data = data;
        }
    }
}
