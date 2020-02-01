package engine.effects;

import engine.rendering.Camera;
import engine.rendering.RenderManager;
import engine.util.node.GGroup;
import engine.water.WaterTileRenderer;
import opengl.textures.Texture;

public class FastWaterRefraction extends Effect {

    private Texture colour;
    private Texture depth;

    public FastWaterRefraction(GGroup<?> group) {
        super(group);
    }

    @Override
    public void process(RenderManager renderManager, Camera camera) {
        this.colour = renderManager.getOutput().getColour();
        this.depth = renderManager.getOutput().getDepth();
    }

    @Override
    protected void onEnable() {
        WaterTileRenderer.getInstance().setRefractionTexture(colour);
        WaterTileRenderer.getInstance().setDepthTexture(depth);
    }

    @Override
    protected void onDisable() {
        WaterTileRenderer.getInstance().setRefractionTexture(null);
        WaterTileRenderer.getInstance().setDepthTexture(null);
    }
}
