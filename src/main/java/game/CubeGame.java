package game;

import engine.components.ActiveMovementComponent;
import engine.components.FloatingComponent;
import engine.components.MovementComponent;
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
import engine.rendering.camera.FlyCamera;
import engine.shape.Cube;
import engine.terrain.ProceduralGeneration;
import engine.terrain.World;
import engine.terrain.generation.HeightColourGenerator;
import engine.terrain.generation.SimplexNoise;
import engine.terrain.lowpoly.LPTerrainConfigs;
import engine.terrain.lowpoly.LowPolyWorld;
import engine.util.node.Group;
import engine.util.node.Node;
import glfw.input.Keyboard;
import glfw.input.Mouse;
import glfw.window.Window;
import maths.utils.Vector3;
import opengl.utils.GlUtils;

public class CubeGame extends SimpleApplication {

    public static void main(String[] args) {
        try {
            SimpleApplication game = new CubeGame();
            GameEngine gameEngine = new GameEngine("CubeGame", 1200, 741, game);
            gameEngine.init();
            gameEngine.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInit(Window window, RenderManager renderer, Keyboard keyboard, Mouse mouse) {
        renderer.setBackground(new BackgroundColour(0.5f, 1.0f, 1.0f));

        final DirectionalLight sun = new DirectionalLight();
        sun.setDirection(Vector3.of(-700, 1500, -300));
        sun.setColour(Vector3.of(1, 1, 1));
        sun.setIntensity(1f);
        scene.attachChild(sun);

        final World world;

        if (false)
            world = new LowPolyWorld(new LPTerrainConfigs()
                    .setSize(800).setVertices(128).setLevels(0, 1, 2, 3, 5, 8, 13, 21, 34, 55, -1)
                    .setColourGenerator(new HeightColourGenerator()
                            .withColour(-5.0f, Vector3.of(.76f, .69f, .50f))
                            .withColour(50.0f, Vector3.of(0.5f, 0.6f, 0.0f))
                            .withColour(100.f, Vector3.of(1.0f, 1.0f, 1.0f)))
                    .setHeightGenerator(new SimplexNoise(500, 200, 500, 3))
            );
        else world = new LowPolyWorld(800);

        world.setGeneration(new ProceduralGeneration(3, camera));
        scene.attachChild(world);

        final Node waterTiles = new Node();
        world.generateWater(waterTiles);
        scene.attachChild(waterTiles);

        final Cube cube = new Cube();
        cube.getComponents().add(new MovementComponent());
        cube.getComponents().add(new TerrainCollisionComponent(world));
        cube.getComponents().add(new ActiveMovementComponent(40, 10, 60, .1f));
        //box.getComponents()().add(new BackFaceComponent(camera.getTransform()));
        //box.getComponents()().add(new KeyboardInputComponent(keyboard));
        cube.getComponents().add(new GravityComponent());
        cube.getComponents().add(new FloatingComponent(0));
        cube.getTransform().setScale(10);
        scene.attachChild(cube);

        camera.setController(new FlyCamera(keyboard, mouse));

        final Group waterEffects = new Group();
        waterEffects.attachChild(world);
        waterEffects.attachChild(cube);
        waterEffects.attachChild(sun);
        scene.addEffect(new WaterReflection(waterEffects));
        scene.addEffect(new WaterRefraction(waterEffects));

        final Group shadowEffects = new Group();
        shadowEffects.attachChild(cube);
        shadowEffects.attachChild(world);
        scene.addEffect(new Shadows(shadowEffects, sun));

        camera.getTransform().setScale(30);
    }

    @Override
    public void onUpdate(Keyboard keyboard, Mouse mouse) {
        scene.update();

        if (keyboard.isKeyPressed(Configs.DRAW_LINES_KEY)) {
            GlUtils.drawPolygonLine();
        } else {
            GlUtils.drawPolygonFill();
        }
    }

    @Override
    public void onRender(RenderManager renderer) {
        renderer.render(scene);
    }
}
