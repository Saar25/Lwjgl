package game.showcase;

import engine.gameengine.Application;
import engine.gameengine.GameEngine;
import engine.gameengine.SimpleApplication;
import engine.light.DirectionalLight;
import engine.rendering.RenderManager;
import engine.rendering.camera.CameraControllers;
import engine.rendering.camera.CameraKeyboardMovement;
import engine.rendering.camera.CameraLookAtRotation;
import engine.terrain.lowpoly.LowPolyPlanet;
import glfw.input.Keyboard;
import glfw.input.Mouse;
import glfw.window.Window;
import maths.utils.Vector3;

public class PlanetShowcase2 extends SimpleApplication {

    public static void main(String[] args) {
        try {
            Application game = new PlanetShowcase2();
            GameEngine gameEngine = new GameEngine("PlanetShowcase2", 1200, 741, game);
            gameEngine.init();
            gameEngine.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInit(Window window, RenderManager renderer, Keyboard keyboard, Mouse mouse) throws Exception {
        // Initialize the game

        final DirectionalLight sun = new DirectionalLight();
        sun.setDirection(Vector3.of(2f, 2f, 2f));
        sun.setColour(Vector3.of(1f, 1f, 1f));
        sun.setIntensity(1f);
        scene.attachChild(sun);

        final LowPolyPlanet planet = new LowPolyPlanet(256, 100);
        scene.attachChild(planet);

        camera.setController(new CameraControllers(
                new CameraKeyboardMovement(keyboard),
                new CameraLookAtRotation(planet)
        ));
        camera.getTransform().setPosition(200, 200, 200);
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