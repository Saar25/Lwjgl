package engine.terrain.multitexture;

import engine.terrain.Terrain;
import engine.terrain.World;
import engine.water.Water;
import engine.water.WaterTile;
import maths.joml.Vector3f;
import opengl.textures.ITexture;
import opengl.textures.MultiTexture;
import opengl.textures.Texture;

public class MultiTextureTerrainWorld extends World {

    private ITexture blendMap = Texture.NONE;
    private ITexture dTexture = Texture.NONE;
    private ITexture rTexture = Texture.NONE;
    private ITexture gTexture = Texture.NONE;
    private ITexture bTexture = Texture.NONE;

    public MultiTextureTerrainWorld(float terrainSize) {
        super(terrainSize);
    }

    public void setDTexture(ITexture dTexture) {
        this.dTexture = dTexture == null ? Texture.NONE : dTexture;
    }

    public void setRTexture(ITexture rTexture) {
        this.rTexture = rTexture == null ? Texture.NONE : rTexture;
    }

    public void setGTexture(ITexture gTexture) {
        this.gTexture = gTexture == null ? Texture.NONE : gTexture;
    }

    public void setBTexture(ITexture bTexture) {
        this.bTexture = bTexture == null ? Texture.NONE : bTexture;
    }

    public void setBlendMap(ITexture blendMap) {
        this.blendMap = blendMap == null ? Texture.NONE : blendMap;
    }

    @Override
    protected Terrain generateTerrain(Vector3f position) {
        return new MultiTextureTerrain(position, getTerrainSize(), new MultiTexture(
                blendMap, dTexture, rTexture, gTexture, bTexture
        ));
    }

    @Override
    protected Water generateWater(Terrain terrain) {
        return new WaterTile(terrain);
    }
}
