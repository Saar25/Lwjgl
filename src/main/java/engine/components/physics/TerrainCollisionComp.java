package engine.components.physics;

import engine.components.motion.ForceComponent;
import engine.componentsystem.GameComponent;
import engine.terrain.World;

public class TerrainCollisionComp extends GameComponent {

    private static final float BIAS = 0.5f;

    private final World terrainWorld;

    private ForceComponent force;

    private boolean onGround;

    public TerrainCollisionComp(World terrainWorld) {
        this.terrainWorld = terrainWorld;
    }

    @Override
    public void start() {
        force = getComponent(ForceComponent.class);
    }

    @Override
    public void update() {
        float groundHeight = terrainWorld.getHeight(getTransform().getPosition().x, getTransform().getPosition().z);

        if (getTransform().getPosition().y <= groundHeight + BIAS) {
            getTransform().getPosition().y = groundHeight + BIAS;
            force.addForce(0, -force.getY(), 0);
            onGround = true;
        } else {
            force.addForce(0, -9.8f, 0);
            onGround = false;
        }
    }

    public boolean isOnGround() {
        return onGround;
    }
}
