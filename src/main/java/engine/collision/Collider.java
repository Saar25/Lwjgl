package engine.collision;

import maths.objects.Ellipsoid;
import maths.utils.Vector3;
import maths.joml.Vector3f;

public class Collider {

    private final CollisionModule collisionModule;
    private final Ellipsoid body;

    private final Vector3f position = Vector3.create();
    private final Vector3f velocity = Vector3.create();

    public Collider(CollisionModule collisionModule, Ellipsoid body) {
        this.collisionModule = collisionModule;
        this.body = body;
    }

    /**
     * Updates the position and the velocity values
     *
     * @param position the current position of the collider
     * @param velocity the velocity the collider will move until next update
     */
    public void update(Vector3f position, Vector3f velocity) {
        this.position.set(position);
        this.velocity.set(velocity);
    }

}
