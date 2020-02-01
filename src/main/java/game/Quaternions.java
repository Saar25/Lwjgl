package game;

import engine.gameengine.GameEngine;
import engine.gameengine.SimpleApplication;
import engine.light.DirectionalLight;
import engine.rendering.RenderManager;
import engine.shape.Shape;
import engine.shape.Sphere;
import engine.terrain.Terrain;
import engine.terrain.generation.HeightColourGenerator;
import engine.terrain.generation.SimplexNoise;
import engine.terrain.lowpoly.LPTerrainConfigs;
import engine.terrain.lowpoly.LowPolyTerrain;
import glfw.input.Keyboard;
import glfw.input.Mouse;
import glfw.window.Window;
import maths.joml.Quaternionf;
import maths.utils.Quaternion;
import maths.utils.Vector3;
import tegui.TContainer;
import tegui.control.TSlider;
import tegui.style.Style;

public class Quaternions extends SimpleApplication {

    public static void main(String[] args) {
        try {
            SimpleApplication game = new Quaternions();
            GameEngine gameEngine = new GameEngine("Quaternions", 1200, 741, game, true);
            gameEngine.init();
            gameEngine.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Quaternionf quaternion = Quaternion.create();
    private Shape shape;

    @Override
    public void onInit(Window window, RenderManager renderer, Keyboard keyboard, Mouse mouse) throws Exception {
        final Terrain terrain = new LowPolyTerrain(new LPTerrainConfigs()
                .setHeightGenerator(new SimplexNoise(500, 20, 100))
                .setColourGenerator(new HeightColourGenerator()
                        .withColour(-10, Vector3.of(.76f, .69f, .50f))
                        .withColour(+10, Vector3.of(0.5f, 0.8f, 0.0f)))
                .setPosition(Vector3.of(0, -100, -500))
                .setVertices(50).setSize(1000));
        scene.attachChild(terrain);

        shape = new Sphere();
        shape.getTransform().setScale(2);
        scene.attachChild(shape);

        camera.getTransform().setPosition(0, 0, 10);
        camera.getTransform().lookAt(shape.getTransform().getPosition());

        final DirectionalLight sun = new DirectionalLight(
                Vector3.of(2, 5, 2), Vector3.of(1, 1, 1), 1);
        scene.attachChild(sun);

        final Style style = new Style();
        style.backgroundColour.set(0, 0, 0, 255);
        style.dimensions.set(200, 20);
        style.radiuses.set(5);
        style.borders.set(3);
        style.borderColour.set(255, 255, 255, 255);

        final TContainer panel = new TContainer();

        final TSlider xSlider = new TSlider(50, 50);
        xSlider.getStyle().setParent(style);
        xSlider.setBounds(-1, 1);
        xSlider.dynamicValueProperty().addListener((o, old, value) -> {
            quaternion.x = value;
        });
        panel.attachChild(xSlider);

        final TSlider ySlider = new TSlider(50, 150);
        ySlider.getStyle().setParent(style);
        ySlider.setBounds(-1, 1);
        ySlider.dynamicValueProperty().addListener((o, old, value) -> {
            quaternion.y = value;
        });
        panel.attachChild(ySlider);

        final TSlider zSlider = new TSlider(50, 250);
        zSlider.getStyle().setParent(style);
        zSlider.setBounds(-1, 1);
        zSlider.dynamicValueProperty().addListener((o, old, value) -> {
            quaternion.z = value;
        });
        panel.attachChild(zSlider);

        final TSlider wSlider = new TSlider(50, 350);
        wSlider.getStyle().setParent(style);
        wSlider.setBounds(-1, 1);
        wSlider.dynamicValueProperty().addListener((o, old, value) -> {
            quaternion.w = value;
        });
        wSlider.setValue(1);
        panel.attachChild(wSlider);

        scene.attachChild(panel);
    }

    @Override
    public void onUpdate(Keyboard keyboard, Mouse mouse) {
        scene.update();
        shape.getTransform().getRotation().set(quaternion).normalize();
    }

    @Override
    public void onRender(RenderManager renderer) {
        renderer.render(scene);
    }

}