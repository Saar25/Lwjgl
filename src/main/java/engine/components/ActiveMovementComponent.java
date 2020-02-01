package engine.components;

import engine.components.physics.TerrainCollisionComponent;
import engine.componentsystem.GameComponent;

public class ActiveMovementComponent extends GameComponent {

    private final float jumpPower;
    private final float walkSpeed;
    private final float sprintSpeed;
    private final float rotateSpeed;
    private final float sidewalkSpeed;

    private MovementComponent movement;
    private TerrainCollisionComponent terrainCollision;
    private float jump, walk, sprint, rotate, sidewalk;

    public ActiveMovementComponent(float jumpPower, float walkSpeed, float sprintSpeed, float rotateSpeed) {
        this.jumpPower = jumpPower;
        this.walkSpeed = walkSpeed;
        this.sprintSpeed = sprintSpeed;
        this.rotateSpeed = rotateSpeed;
        this.sidewalkSpeed = walkSpeed / 2;
    }

    public ActiveMovementComponent(float jumpPower, float walkSpeed, float sprintSpeed, float rotateSpeed, float sidewalkSpeed) {
        this.jumpPower = jumpPower;
        this.walkSpeed = walkSpeed;
        this.sprintSpeed = sprintSpeed;
        this.rotateSpeed = rotateSpeed;
        this.sidewalkSpeed = sidewalkSpeed;
    }

    @Override
    public void start() {
        movement = getComponent(MovementComponent.class);
        terrainCollision = getNullableComponent(TerrainCollisionComponent.class);
    }

    @Override
    public void update() {
        if (jump != 0) {
            movement.getVelocity().y = jump > 0
                    ? Math.max(movement.getVelocity().y, jump)
                    : Math.min(movement.getVelocity().y, jump);
            jump = 0;
        }
        if (walk != 0) {
            movement.getVelocity().z = walk > 0
                    ? Math.max(movement.getVelocity().z, walk)
                    : Math.min(movement.getVelocity().z, walk);
            walk = 0;
        }
        if (sprint != 0) {
            movement.getVelocity().z = sprint > 0
                    ? Math.max(movement.getVelocity().z, sprint)
                    : Math.min(movement.getVelocity().z, sprint);
            sprint = 0;
        }
        if (sidewalk != 0) {
            movement.getVelocity().x = sidewalk > 0
                    ? Math.max(movement.getVelocity().x, sidewalk)
                    : Math.min(movement.getVelocity().x, sidewalk);
            sidewalk = 0;
        }
        if (rotate != 0) {
            getTransform().addRotation(0, rotate, 0);
            rotate = 0;
        }
    }

    public void jump() {
        if (terrainCollision == null || terrainCollision.onGround()) {
            this.jump = jumpPower;
        }
    }

    public void walk(boolean forward) {
        this.walk = forward ? walkSpeed : -walkSpeed;
    }

    public void sidewalk(boolean left) {
        this.sidewalk = left ? sidewalkSpeed : -sidewalkSpeed;
    }

    public void sprint() {
        this.sprint = sprintSpeed;
    }

    public void rotate(boolean right) {
        this.rotate = right ? -rotateSpeed : rotateSpeed;
    }

}
