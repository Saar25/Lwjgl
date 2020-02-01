package engine.water;

import engine.gameengine.Time;
import engine.models.Model;
import engine.rendering.Renderable;
import engine.terrain.Terrain;
import engine.util.Lazy;
import opengl.textures.ITexture;

public class WaterTile extends Water implements Renderable {

    public static ITexture defaultDudvMap = null;
    public static ITexture defaultNormalsMap = null;

    private static final Lazy<Model> MODEL = new Lazy<>(() -> WaterGenerator.generateModel(32));
    private static final float QUAD_SIZE = 8;

    private ITexture dudvMap = defaultDudvMap;
    private ITexture normalsMap = defaultNormalsMap;

    private float distortionOffset = 0;

    public WaterTile() {
        super(MODEL.get());
    }

    public WaterTile(Model model) {
        super(model);
    }

    public WaterTile(Terrain terrain) {
        super(WaterGenerator.generateModel(terrain,
                (int) (terrain.getSize() / QUAD_SIZE) + 1));
        getTransform().set(terrain.getTransform());
    }

    public static void deleteModel() {
        MODEL.ifAssigned(Model::delete);
    }

    public float getDistortionOffset() {
        return distortionOffset;
    }

    public ITexture getDudvMap() {
        return dudvMap;
    }

    public void setDudvMap(ITexture dudvMap) {
        this.dudvMap = dudvMap;
    }

    public ITexture getNormalsMap() {
        return normalsMap;
    }

    public void setNormalsMap(ITexture normalsMap) {
        this.normalsMap = normalsMap;
    }

    @Override
    public void process() {
        WaterTileRenderer.getInstance().process(this);
    }

    @Override
    public void update() {
        this.distortionOffset += Time.getDelta() * 0.05f;
        this.distortionOffset %= 1;
    }

    @Override
    public void delete() {
        super.delete();
        if (dudvMap != null) {
            dudvMap.delete();
        }
        if (normalsMap != null) {
            normalsMap.delete();
        }
    }
}
