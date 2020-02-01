package engine.water.flat;

import engine.water.Water;
import engine.water.WaterGenerator;
import opengl.textures.Texture2D;

public class FlatWater extends Water {

    private Texture2D dudvMap;
    private Texture2D normalsMap;

    private float distortionOffset;

    public FlatWater() {
        super(WaterGenerator.generateModel(64));
    }

    public Texture2D getDudvMap() {
        return dudvMap;
    }

    public Texture2D getNormalsMap() {
        return normalsMap;
    }

    public void setDudvMap(Texture2D dudvMap) {
        this.dudvMap = dudvMap;
    }

    public void setNormalsMap(Texture2D normalsMap) {
        this.normalsMap = normalsMap;
    }

    public float getDistortionOffset() {
        return distortionOffset;
    }

    @Override
    public void update() {
        distortionOffset += .01f;
    }

    @Override
    public void process() {
        FlatWaterRenderer.getInstance().process(this);
    }
}
