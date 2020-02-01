package game.showcase;

import engine.gameengine.GameEngine;
import engine.gameengine.SimpleApplication;
import engine.light.DirectionalLight;
import engine.rendering.RenderManager;
import engine.rendering.Spatial;
import engine.rendering.camera.FlyCamera;
import engine.terrain.Terrain;
import engine.terrain.generation.HeightColourGenerator;
import engine.terrain.generation.PerlinNoise;
import engine.terrain.lowpoly.LPTerrainConfigs;
import engine.terrain.lowpoly.LowPolyTerrain;
import glfw.input.Keyboard;
import glfw.input.Mouse;
import glfw.window.Window;
import maths.utils.Vector3;

public class Terrains extends SimpleApplication {

    public static void main(String[] args) {
        try {
            SimpleApplication game = new Terrains();
            GameEngine gameEngine = new GameEngine("Terrains", 1200, 741, game);
            gameEngine.init();
            gameEngine.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int seed = 0;

    @Override
    public void onInit(Window window, RenderManager renderer, Keyboard keyboard, Mouse mouse) throws Exception {
        scene.attachChild(new DirectionalLight(Vector3.of(7, 50, 5), Vector3.one(), 1));

        scene.attachChild(createTerrain());

        camera.setController(new FlyCamera(keyboard, mouse));
    }

    private Terrain createTerrain() {
        final HeightColourGenerator colourGenerator = new HeightColourGenerator()
                .withColour(-5.0f, Vector3.of(.76f, .69f, .50f))
                .withColour(30.0f, Vector3.of(0.5f, 0.6f, 0.0f))
                .withColour(80.0f, Vector3.of(0.5f, 0.6f, 0.0f))
                .withColour(100.f, Vector3.of(1.0f, 1.0f, 1.0f));
        return new LowPolyTerrain(new LPTerrainConfigs()
                .setHeightGenerator(new PerlinNoise(seed += 10, 800, 10))
                .setSize(1000).setVertices(64)
                .setColourGenerator(colourGenerator)
        );
    }

    @Override
    public void onUpdate(Keyboard keyboard, Mouse mouse) {
        // Update game, get the needed input as well

        scene.update();

        if (keyboard.isKeyPressed('R')) {
            Spatial terrain = scene.getChildren().get(1);
            scene.replaceChild(terrain, createTerrain());
            terrain.delete();
        }
    }

    @Override
    public void onRender(RenderManager renderer) {
        // Render the scene

        renderer.render(scene);
    }

}