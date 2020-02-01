package engine.terrain;

public class TerrainNeighbors {
    
    private final Terrain[][] terrains = new Terrain[3][3];
    
    public void set(int x, int z, Terrain terrain) {
        terrains[x + 1][z + 1] = terrain;
    }
    
    public Terrain get(int x, int z) {
        return terrains[x + 1][z + 1];
    }
}
