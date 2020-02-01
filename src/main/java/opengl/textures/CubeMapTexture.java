package opengl.textures;

import opengl.constants.DataType;
import opengl.constants.FormatType;

public class CubeMapTexture implements ITexture {

    private final Texture texture;

    private CubeMapTexture(Texture texture) {
        this.texture = texture;
        this.bind();
    }

    public static CubeMapTexture create() {
        Texture texture = Texture.create(TextureTarget.TEXTURE_CUBE_MAP);
        return new CubeMapTexture(texture);
    }

    public static CubeMapTexture of(TextureInfo px, TextureInfo py, TextureInfo pz, TextureInfo nx, TextureInfo ny, TextureInfo nz) {
        final Texture texture = Texture.create(TextureTarget.TEXTURE_CUBE_MAP);
        allocate(texture, TextureTarget.TEXTURE_CUBE_MAP_POSITIVE_X, px);
        allocate(texture, TextureTarget.TEXTURE_CUBE_MAP_NEGATIVE_X, nx);
        allocate(texture, TextureTarget.TEXTURE_CUBE_MAP_POSITIVE_Y, py);
        allocate(texture, TextureTarget.TEXTURE_CUBE_MAP_NEGATIVE_Y, ny);
        allocate(texture, TextureTarget.TEXTURE_CUBE_MAP_POSITIVE_Z, pz);
        allocate(texture, TextureTarget.TEXTURE_CUBE_MAP_NEGATIVE_Z, nz);
        return new CubeMapTexture(texture);
    }

    private static void allocate(Texture texture, TextureTarget target, TextureInfo info) {
        if (info != null) {
            texture.allocate(target, 0, FormatType.RGBA8, info.getWidth(),
                    info.getHeight(), 0, FormatType.RGBA, DataType.U_BYTE, info.getData());
        }
    }

    public static CubeMapTextureBuilder builder() {
        return new CubeMapTextureBuilder();
    }

    @Override
    public void bind(int unit) {
        texture.bind(unit);
    }

    @Override
    public void bind() {
        texture.bind();
    }

    @Override
    public void unbind() {
        texture.unbind();
    }

    @Override
    public void delete() {
        texture.delete();
    }
}
