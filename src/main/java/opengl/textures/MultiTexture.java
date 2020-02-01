package opengl.textures;

import java.util.function.Consumer;

public class MultiTexture implements ITexture {

    private final ITexture blendMap; // Blend Map Texture
    private final ITexture dTexture; // Default   Texture
    private final ITexture rTexture; // Red       Texture
    private final ITexture gTexture; // Green     Texture
    private final ITexture bTexture; // Blue      Texture

    public MultiTexture(ITexture blendMap, ITexture dTexture, ITexture rTexture, ITexture gTexture, ITexture bTexture) {
        this.blendMap = blendMap;
        this.dTexture = dTexture;
        this.rTexture = rTexture;
        this.gTexture = gTexture;
        this.bTexture = bTexture;
    }

    @Override
    public void bind(int unit) {
        blendMap.bind(unit);
        dTexture.bind(unit + 1);
        rTexture.bind(unit + 2);
        gTexture.bind(unit + 3);
        bTexture.bind(unit + 4);
    }

    @Override
    public void bind() {
        this.bind(0);
    }

    @Override
    public void unbind() {
        this.forEach(ITexture::unbind);
    }

    @Override
    public void delete() {
        this.forEach(ITexture::delete);
    }

    private void forEach(Consumer<ITexture> consumer) {
        consumer.accept(blendMap);
        consumer.accept(dTexture);
        consumer.accept(rTexture);
        consumer.accept(gTexture);
        consumer.accept(bTexture);
    }
}
