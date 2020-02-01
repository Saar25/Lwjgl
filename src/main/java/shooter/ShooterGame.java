package shooter;

import engine.gameengine.Application;
import engine.gameengine.GameEngine;
import engine.gameengine.SimpleApplication;
import engine.light.DirectionalLight;
import engine.rendering.RenderManager;
import engine.rendering.camera.CameraControllers;
import engine.rendering.camera.CameraKeyboardMovement;
import engine.rendering.camera.CameraMouseDragRotation;
import engine.shape.Cube;
import glfw.input.Keyboard;
import glfw.input.Mouse;
import glfw.window.Window;
import maths.utils.Vector3;
import tegui.style.property.Colour;

public class ShooterGame extends SimpleApplication {

    public static void main(String[] args) {
        try {
            Application game = new ShooterGame();
            GameEngine gameEngine = new GameEngine("ShooterGame", 1200, 741, game);
            gameEngine.init();
            gameEngine.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInit(Window window, RenderManager renderer, Keyboard keyboard, Mouse mouse) throws Exception {
        // Initialize the game

        final DirectionalLight light = new DirectionalLight();
        light.setColour(Colour.WHITE.rgbVector());
        light.setDirection(Vector3.normalize(-.7f, -1.0f, .3f));
        light.setIntensity(1.0f);
        scene.attachChild(light);

        final Cube cube = new Cube();
        cube.getTransform().setScale(100);
        scene.attachChild(cube);

        camera.setController(new CameraControllers(
                new CameraKeyboardMovement(keyboard),
                new CameraMouseDragRotation(mouse)));
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