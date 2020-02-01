package game.showcase;

import engine.effects.WaterReflection;
import engine.effects.WaterRefraction;
import engine.gameengine.GameEngine;
import engine.gameengine.SimpleApplication;
import engine.light.DirectionalLight;
import engine.rendering.RenderManager;
import engine.rendering.background.BackgroundGradientColour;
import engine.rendering.camera.CameraControllers;
import engine.rendering.camera.CameraKeyboardSmoothMovement;
import engine.rendering.camera.CameraMouseDragSmoothRotation;
import engine.terrain.Terrain;
import engine.terrain.generation.ColourGenerator;
import engine.terrain.generation.HeightGenerator;
import engine.terrain.generation.NormalColourGenerator;
import engine.terrain.generation.PerlinNoise;
import engine.terrain.lowpoly.LPTerrainConfigs;
import engine.terrain.lowpoly.LowPolyTerrain;
import engine.util.node.Group;
import engine.water.lowpoly.LowpolyWavyWater;
import glfw.input.Keyboard;
import glfw.input.Mouse;
import glfw.window.Window;
import maths.joml.Vector3f;
import maths.utils.Vector3;
import tegui.style.property.Colour;
import tegui.style.property.Orientation;

import java.util.Random;

public class WaterTerrainsShowcase extends SimpleApplication {

    public static void main(String[] args) {
        try {
            SimpleApplication game = new WaterTerrainsShowcase();
            GameEngine gameEngine = new GameEngine("WaterTerrains", 1200, 741, game);
            gameEngine.init();
            gameEngine.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInit(Window window, RenderManager renderer, Keyboard keyboard, Mouse mouse) {
        final DirectionalLight sun = new DirectionalLight(Vector3.of(2, 7, 5), Vector3.of(1f, 1f, 1f), 1f);
        scene.attachChild(sun);

        final HeightGenerator heightGenerator = new PerlinNoise(new Random().nextLong(), .03f, 800, 3, 50);

        final ColourGenerator normalColourGenerator = new NormalColourGenerator(Vector3.upward())
                .withColour(0.8f, Vector3.of(.41f, .41f, .41f))
                .withColour(1.0f, Vector3.of(0.5f, 0.6f, 0.0f));
        final Vector3f sandColour = Vector3.of(.76f, .69f, .50f);
        final ColourGenerator colourGenerator = vertex -> vertex.getPosition().y < 20
                ? sandColour : normalColourGenerator.generateColour(vertex);

        final Terrain terrain = new LowPolyTerrain(new LPTerrainConfigs()
                .setHeightGenerator(heightGenerator)
                .setColourGenerator(colourGenerator)
                .setPosition(Vector3.of(0, 0, 0))
                .setSize(4000).setVertices(32 * 8)
        );
        scene.attachChild(terrain);

        final LowpolyWavyWater water = new LowpolyWavyWater(terrain, 256);
        water.setAmplitude(.5f);
        scene.attachChild(water);

        final Group waterEffects = new Group();
        waterEffects.attachChild(sun);
        waterEffects.attachChild(terrain);
        scene.addEffect(new WaterReflection(waterEffects, 2560, 1440));
        scene.addEffect(new WaterRefraction(waterEffects));

        scene.getEffect(WaterReflection.class).setEnabled(false);
        scene.getEffect(WaterRefraction.class).setEnabled(false);

        camera.setController(new CameraControllers(
                new CameraKeyboardSmoothMovement(keyboard),
                new CameraMouseDragSmoothRotation(mouse)
        ));

        final BackgroundGradientColour background = new BackgroundGradientColour();
        background.getColours().set(Colour.CYAN, new Colour(0, .8f, 1), Orientation.VERTICAL);
        renderer.setBackground(background);
    }

    @Override
    public void onUpdate(Keyboard keyboard, Mouse mouse) {
        scene.update();

        scene.getEffect(WaterReflection.class).setEnabled(keyboard.isKeyPressed('E'));
        scene.getEffect(WaterRefraction.class).setEnabled(keyboard.isKeyPressed('R'));
    }

    @Override
    public void onRender(RenderManager renderer) {
        renderer.render(scene);
    }

}