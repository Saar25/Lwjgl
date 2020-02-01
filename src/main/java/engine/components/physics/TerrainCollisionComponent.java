package engine.components.physics;

import engine.components.MovementComponent;
import engine.componentsystem.GameComponent;
import engine.gameengine.Time;
import engine.terrain.StaticWorld;
import engine.terrain.Terrain;
import engine.terrain.World;
import maths.joml.Vector3f;

public class TerrainCollisionComponent extends GameComponent {

    private MovementComponent movement;
    private World terrains;
    private float ground;

    public TerrainCollisionComponent(World terrains) {
        this.terrains = terrains;
    }

    public TerrainCollisionComponent(Terrain... terrains) {
        this.terrains = new StaticWorld(terrains);
    }

    @Override
    public void start() {
        movement = getComponent(MovementComponent.class);
    }

    @Override
    public void update() {
        ground = terrains == null ? 0 : terrains.getHeight(getTransform().getPosition().x, getTransform().getPosition().z);
        Vector3f velocity = movement.getVelocity();

        // Terrain collision
        if (getTransform().getPosition().y <= ground) {
            getTransform().getPosition().y = ground;
            velocity.y = Math.max(velocity.y, 0);
        }

        // Ground Friction
        float friction = (getTransform().getPosition().y > ground ? 0.3f : 30f) * Time.getDelta();
        if (velocity.x > 0) {
            velocity.x = Math.max(0, velocity.x - friction);
        } else if (velocity.x < 0) {
            velocity.x = Math.min(0, velocity.x + friction);
        }
        if (velocity.z > 0) {
            velocity.z = Math.max(0, velocity.z - friction);
        } else if (velocity.z < 0) {
            velocity.z = Math.min(0, velocity.z + friction);
        }
    }

    public boolean onGround() {
        return getTransform().getPosition().y <= ground;
    }

}
