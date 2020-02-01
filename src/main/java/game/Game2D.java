package game;

import engine.gameengine.SimpleApplication;
import engine.gameengine.GameEngine;
import glfw.input.Keyboard;
import glfw.input.Mouse;
import engine.rendering.RenderManager;
import glfw.window.Window;
import maths.utils.Maths;
import tegui.graphics.Graphics;
import tegui.objects.TGraphical;
import tegui.style.property.Colour;

public class Game2D extends SimpleApplication {

    private Bird bird;
    private float xVelocity = 0;
    private float yVelocity = 0;

    public static void main(String[] args) {
        try {
            SimpleApplication game = new Game2D();
            GameEngine gameEngine = new GameEngine("Game 2D", 1200, 741, game);
            gameEngine.init();
            gameEngine.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInit(Window window, RenderManager renderer, Keyboard keyboard, Mouse mouse) {
        scene.attachChild(new View(1200, 741));

        bird = new Bird();
    }

    @Override
    public void onUpdate(Keyboard keyboard, Mouse mouse) {
        scene.update();
        bird.y += yVelocity;
        if (bird.y == 520 && keyboard.isKeyPressed('W')) {
            yVelocity = -7;
        } else if (bird.y <= 520) {
            yVelocity += .07f;
        } else {
            yVelocity = 0;
            bird.y = 520;
            xVelocity *= 0.9f;
        }

        if (keyboard.isKeyPressed('A')) {
            xVelocity = -2.5f;
        }
        if (keyboard.isKeyPressed('D')) {
            xVelocity = 2.5f;
        }
        bird.x = Maths.clamp(bird.x + (int) xVelocity, 0, 1100);
    }

    @Override
    public void onRender(RenderManager renderer) {
        renderer.render(scene);
    }

    private static class Bird {
        private int x, y;

        private void paint(Graphics g) {
            g.setColour(Colour.RED);
            g.fillRectangle(x, y, 100, 100);
        }
    }

    private class View extends TGraphical {

        private final Colour brown = new Colour(222, 184, 135);

        private View(int width, int height) {
            super(width, height);
            setBackground(Colour.CYAN);
        }

        @Override
        public void paint(Graphics g) {
            g.setColour(brown);
            g.fillRectangle(0, 650, 1200, 91);
            g.setColour(Colour.GREEN);
            g.fillRectangle(0, 620, 1200, 30);
            bird.paint(g);
        }
    }
}
