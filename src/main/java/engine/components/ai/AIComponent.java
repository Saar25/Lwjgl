package engine.components.ai;

import engine.components.ActiveMovementComponent;
import engine.componentsystem.GameComponent;
import engine.gameengine.Timer;

public class AIComponent extends GameComponent {

    private Timer timer = Timer.createRandomTimer(0.001f);

    private ActiveMovementComponent movement;

    private float destination;

    @Override
    public void start() {
        movement = getComponent(ActiveMovementComponent.class);
    }

    @Override
    public void update() {
        if (timer.check()) {
            destination = (float) (80 + 100 * Math.random());
        }
        if (destination < 1) {
            destination = 0;
        } else {
            destination -= 1;
            movement.walk(true);
            movement.rotate(true);
        }
    }
}
