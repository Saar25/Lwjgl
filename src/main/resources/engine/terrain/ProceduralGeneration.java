package engine.terrain;

import engine.rendering.Camera;
import maths.joml.Vector3f;
import maths.utils.Vector3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ProceduralGeneration {

    public static ProceduralGeneration NONE = new ProceduralGeneration();

    private final int sightRadius;
    private final Camera camera;

    private final Vector3f lastPosition = Vector3.create();

    public ProceduralGeneration(int sightRadius, Camera camera) {
        this.sightRadius = sightRadius;
        this.camera = camera;

        lastPosition.set(camera.getTransform().getPosition());
    }

    private ProceduralGeneration() {
        this.sightRadius = 0;
        this.camera = null;
    }

    private boolean shouldCheck(World world) {
        if (sightRadius == 0 || camera == null) return false;
        final Vector3f cameraPosition = camera.getTransform().getPosition();
        final float distance2 = lastPosition.distanceSquared(cameraPosition);
        return distance2 > world.getTerrainSize() / 2;
    }

    public List<Vector3f> getNewPositions(World world) {
        if (!shouldCheck(world)) {
            return Collections.emptyList();
        }
        final List<Vector3f> positions = new ArrayList<>();
        for (int x = -sightRadius; x < sightRadius; x++) {
            for (int z = -sightRadius; z < sightRadius; z++) {
                final Vector3f position = Vector3.of(camera.getTransform().getPosition());
                position.add(x * world.getTerrainSize(), 0, z * world.getTerrainSize());
                if (!world.isInside(position.x, position.z)) {
                    positions.add(position);
                }
            }
        }
        lastPosition.set(camera.getTransform().getPosition());
        System.out.println(positions);
        return positions;
    }
}
