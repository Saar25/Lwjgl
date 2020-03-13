package engine.terrain;

import engine.util.node.Node;
import engine.util.node.Parent;
import engine.water.Water;
import maths.joml.Vector3f;
import maths.utils.Vector3;

import java.util.ArrayList;
import java.util.List;

public abstract class World extends Parent {

    private final List<Terrain> terrains = new ArrayList<>();

    private final float terrainSize;

    private ProceduralGeneration generation = ProceduralGeneration.NONE;

    private boolean generateWater;
    private Node waterHolder;

    public World(float terrainSize) {
        this.terrainSize = terrainSize;
    }

    public void setGeneration(ProceduralGeneration generation) {
        this.generation = generation;
        this.generate();
    }

    public void generateWater(Node waterHolder) {
        this.waterHolder = waterHolder;
        this.generateWater = true;
        for (Terrain terrain : getChildren()) {
            final Water water = generateWater(terrain);
            waterHolder.attachChild(water);
        }
    }

    public void addTerrain(Vector3f position) {
        int tx = (int) (position.x / getTerrainSize()) - (position.x < 0 ? 1 : 0);
        int tz = (int) (position.z / getTerrainSize()) - (position.z < 0 ? 1 : 0);
        final Terrain terrain = generateTerrain(Vector3.of(tx, 0, tz).mul(getTerrainSize()));
        getChildren().add(terrain);
        if (generateWater) {
            final Water water = generateWater(terrain);
            waterHolder.attachChild(water);
        }
    }

    public void addTerrain(int x, int y, int z) {
        final Vector3f position = Vector3.of(x, y, z).mul(getTerrainSize());
        final Terrain terrain = generateTerrain(position);
        getChildren().add(terrain);
        if (generateWater) {
            final Water water = generateWater(terrain);
            waterHolder.attachChild(water);
        }
    }

    public float getHeight(float x, float z) {
        for (Terrain child : getChildren()) {
            if (child.isInside(x, z)) {
                return child.getHeight(x, z);
            }
        }
        return 0;
    }

    public boolean isInside(float x, float z) {
        for (Terrain child : getChildren()) {
            if (child.isInside(x, z)) {
                return true;
            }
        }
        return false;
    }

    public final float getTerrainSize() {
        return terrainSize;
    }

    @Override
    public void update() {
        super.update();
        generate();
    }

    @Override
    public List<Terrain> getChildren() {
        return terrains;
    }

    private void generate() {
        generation.getNewPositions(this)
                .forEach(this::addTerrain);
    }

    /**
     * Generate a terrain in the given position
     *
     * @param position the position of the terrain
     * @return the generated terrain
     */
    protected abstract Terrain generateTerrain(Vector3f position);

    /**
     * Generate water above the given terrain
     *
     * @return the generated water
     */
    protected abstract Water generateWater(Terrain terrain);
}
