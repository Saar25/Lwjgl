package opengl.shaders;

import opengl.textures.MultiTexture;
import opengl.textures.Texture;
import org.lwjgl.opengl.GL20;

public abstract class UniformMultiTextureProperty<T> implements UniformProperty<T> {

    private final String blendMap;
    private final String dTexture;
    private final String rTexture;
    private final String gTexture;
    private final String bTexture;
    private final int unit;

    public UniformMultiTextureProperty(String blendMap, String dTexture, String rTexture,
                                       String gTexture, String bTexture, int unit) {
        this.blendMap = blendMap;
        this.dTexture = dTexture;
        this.rTexture = rTexture;
        this.gTexture = gTexture;
        this.bTexture = bTexture;
        this.unit = unit;
    }

    @Override
    public void load(RenderState<T> state) {
        if (valueAvailable()) {
            Texture.bind(getUniformValue(state), unit);
        }
    }

    @Override
    public void initialize(ShadersProgram<T> shadersProgram) {
        GL20.glUniform1i(shadersProgram.getUniformLocation(blendMap), unit);
        GL20.glUniform1i(shadersProgram.getUniformLocation(dTexture), unit + 1);
        GL20.glUniform1i(shadersProgram.getUniformLocation(rTexture), unit + 2);
        GL20.glUniform1i(shadersProgram.getUniformLocation(gTexture), unit + 3);
        GL20.glUniform1i(shadersProgram.getUniformLocation(bTexture), unit + 4);
    }

    protected abstract MultiTexture getUniformValue(RenderState<T> renderState);
}
