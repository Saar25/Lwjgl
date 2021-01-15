package opengl.textures;

import engine.fileLoaders.PNGDecoder;
import opengl.utils.MemoryUtils;

import java.nio.ByteBuffer;

public final class TextureLoader {

    private TextureLoader() {

    }

    /**
     * Loads a texture file into width, height and data
     * and return the values as a {@link TextureInfo} object
     *
     * @param file the texture file path
     * @return the texture info
     * @throws Exception if could not load the texture
     */
    public static TextureInfo load(String file) throws Exception {
        String fileFormat = file.substring(file.lastIndexOf(".") + 1).toLowerCase();
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
     * @return {@link TextureInfo} necessary to init the texture
     * @throws Exception if could not load the image
     */
    private static TextureInfo decodePng(String textureFile) throws Exception {
        PNGDecoder decoder = new PNGDecoder(TextureLoader.class.getResourceAsStream(textureFile));

        ByteBuffer textureBuffer = MemoryUtils.allocByte(4 * decoder.getWidth() * decoder.getHeight());
        decoder.decode(textureBuffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
        textureBuffer.flip();

        return new TextureInfo(decoder.getWidth(), decoder.getHeight(), textureBuffer);
    }
}
