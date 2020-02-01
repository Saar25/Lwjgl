package game.showcase;

import engine.gameengine.Application;
import engine.gameengine.GameEngine;
import engine.gameengine.SimpleApplication;
import engine.light.DirectionalLight;
import engine.rendering.RenderManager;
import engine.rendering.camera.FlyCamera;
import engine.shape.Cube;
import engine.terrain.generation.SimplexNoise;
import glfw.input.Keyboard;
import glfw.input.Mouse;
import glfw.window.Window;
import maths.noise.Noise2f;
import maths.utils.Vector3;
import tegui.style.property.Colour;

public class Minecraft extends SimpleApplication {

    public static void main(String[] args) {
        try {
            Application game = new Minecraft();
            GameEngine gameEngine = new GameEngine("Minecraft", 1200, 741, game);
            gameEngine.init();
            gameEngine.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInit(Window window, RenderManager renderer, Keyboard keyboard, Mouse mouse) {
        // Initialize the game

        final DirectionalLight sun = new DirectionalLight();
        sun.setDirection(Vector3.of(0, 1, 0));
        sun.setColour(Vector3.of(.0f, .7f, 1));
        sun.setIntensity(1);
        scene.attachChild(sun);

        keyboard.onKeyPress('T').perform(e -> mouse.setShown(!mouse.isShown()));


        final Noise2f noise = new SimplexNoise(10, 30);

        for (int x = 0; x < 200; x++) {
            for (int z = 0; z < 200; z++) {
                final int height = (int) noise.noise(x, z);
                if (height <= 0) {
                    final Cube sea = new Cube();
                    sea.getColour().set(Colour.BLUE);
                    sea.getTransform().setPosition(x, 0, z);
                    scene.attachChild(sea);
                } else {

                    final Cube grass = new Cube();
                    grass.getColour().set(Colour.GREEN);
                    grass.getTransform().setPosition(x, height, z);
                    scene.attachChild(grass);

                    for (int y = 1; y < 3; y++) {
                        final Cube dirt = new Cube();
                        dirt.getColour().set(Colour.BROWN);
                        dirt.getTransform().setPosition(x, height - y, z);
                        scene.attachChild(dirt);
                    }
                }
            }
        }

        camera.setController(new FlyCamera(keyboard, mouse));
    }

    @Override
    public void onUpdate(Keyboard keyboard, Mouse mouse) {
        // Update game, get the needed input as well

        scene.update();
    }

    @Override
    public void onRender(RenderManager renderer) {
        // Render the scene

        renderer.render(scene);
    }

}