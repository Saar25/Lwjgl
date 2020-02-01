package game.showcase;

import engine.components.*;
import engine.components.physics.GravityComponent;
import engine.components.physics.TerrainCollisionComponent;
import engine.effects.Shadows;
import engine.effects.WaterReflection;
import engine.effects.WaterRefraction;
import engine.gameengine.GameEngine;
import engine.gameengine.SimpleApplication;
import engine.light.DirectionalLight;
import engine.rendering.RenderManager;
import engine.rendering.background.BackgroundColour;
import engine.rendering.camera.ThirdPersonCamera;
import engine.shape.Cube;
import engine.terrain.Terrain;
import engine.terrain.generation.ColourGenerator;
import engine.terrain.generation.HeightGenerator;
import engine.terrain.generation.NormalColourGenerator;
import engine.terrain.generation.PerlinNoise;
import engine.terrain.lowpoly.LPTerrainConfigs;
import engine.terrain.lowpoly.LowPolyTerrain;
import engine.util.node.Group;
import engine.water.Water;
import engine.water.lowpoly.LowpolyWavyWater;
import glfw.input.Keyboard;
import glfw.input.Mouse;
import glfw.window.Window;
import maths.joml.Vector3f;
import maths.utils.Vector3;

import java.util.Random;

public class CubeGameShowcase extends SimpleApplication {

    public static void main(String[] args) {
        try {
            SimpleApplication game = new CubeGameShowcase();
            GameEngine gameEngine = new GameEngine("CubeGame", 1200, 741, game);
            gameEngine.init();
            gameEngine.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInit(Window window, RenderManager renderer, Keyboard keyboard, Mouse mouse) {
        keyboard.onKeyPress('T').perform(e -> mouse.setShown(!mouse.isShown()));

        renderer.setBackground(new BackgroundColour(0.5f, 1.0f, 1.0f));

        final DirectionalLight sun = new DirectionalLight();
        sun.setDirection(Vector3.of(-7, 5, -3));
        sun.setColour(Vector3.of(1, 1, 1));
        sun.setIntensity(1f);
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

        final Water water = new LowpolyWavyWater(terrain, 256);
        scene.attachChild(water);

        final Cube cube = new Cube();
        cube.getTransform().setScale(10);
        cube.getComponents().add(new MovementComponent());
        cube.getComponents().add(new ActiveMovementComponent(60, 40, 60, 10));
        cube.getComponents().add(new KeyboardInputComponent(keyboard));
        cube.getComponents().add(new TerrainCollisionComponent(terrain));
        cube.getComponents().add(new BackFaceComponent(camera.getTransform()));
        cube.getComponents().add(new GravityComponent());
        cube.getComponents().add(new FloatingComponent(0));
        scene.attachChild(cube);

        camera.setController(new ThirdPersonCamera(mouse, cube));

        final Group waterEffects = new Group();
        waterEffects.attachChild(terrain);
        waterEffects.attachChild(cube);
        waterEffects.attachChild(sun);
        scene.addEffect(new WaterReflection(waterEffects));
        scene.addEffect(new WaterRefraction(waterEffects));

        final Group shadowEffects = new Group();
        shadowEffects.attachChild(cube);
        shadowEffects.attachChild(terrain);
        scene.addEffect(new Shadows(shadowEffects, sun));
    }

    @Override
    public void onUpdate(Keyboard keyboard, Mouse mouse) {
        scene.update();
    }

    @Override
    public void onRender(RenderManager renderer) {
        renderer.render(scene);
    }
}
