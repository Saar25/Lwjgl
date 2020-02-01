package game;

import engine.entity.Entity;
import engine.gameengine.GameEngine;
import engine.gameengine.SimpleApplication;
import engine.light.DirectionalLight;
import engine.models.Model;
import engine.models.Skin;
import engine.postprocessing.contrast.Contrast;
import engine.rendering.RenderManager;
import engine.shape.Cube;
import engine.util.node.Node;
import glfw.input.Keyboard;
import glfw.input.Mouse;
import glfw.window.Window;
import maths.joml.Vector3f;
import maths.utils.Vector3;

public class SimpleGame extends SimpleApplication {

    private final Node boxes = new Node();

    public static void main(String[] args) {
        try {
            SimpleApplication game = new SimpleGame();
            GameEngine gameEngine = new GameEngine("Saar engine preview", 1200, 800, game);
            gameEngine.init();
            gameEngine.start();
        } catch (Exception e) {
            System.err.println("Error running the game");
            e.printStackTrace();
        }
    }

    @Override
    public void onInit(Window window, RenderManager renderer, Keyboard keyboard, Mouse mouse) throws Exception {

        // Apply contrast post processor
        renderer.getPostProcessing().add(new Contrast(1.8f));

        // Add tree 1
        Skin tree1Skin = loader.loadSkin("/textures/lowPolyTree.png");      // Load the skin
        Model tree1Model = loader.loadModel("/objModels/lowPolyTree.obj");  // Load the model
        Entity tree1 = new Entity(tree1Model, tree1Skin);                   // Create the entity
        tree1.getTransform().setPosition(10, 0, 0);                         // Transform the entity
        scene.attachChild(tree1);                                           // Add the entity to the scene

        // Add tree 2
        Skin tree2Skin = loader.loadSkin("/textures/tree.png");
        Model tree2Model = loader.loadModel("/objModels/tree.obj");
        Entity tree2 = new Entity(tree2Model, tree2Skin);
        tree2.getTransform().setPosition(-10, 0, 0);
        tree2.getTransform().setScale(10);
        scene.attachChild(tree2);

        // Add sun light
        float sunIntensity = 1;
        Vector3f sunColour = Vector3.of(1, 1, 1); // White
        Vector3f sunPosition = Vector3.of(-700, 1500, -300);
        DirectionalLight sun = new DirectionalLight(sunPosition, sunColour, sunIntensity);
        scene.attachChild(sun);

        // Center the camera
        Vector3f a = tree1.getTransform().getPosition();
        Vector3f b = tree2.getTransform().getPosition();
        Vector3f center = Vector3.add(a, b).div(2).add(0, 10, 0);
        camera.getTransform().setPosition(10, 10, 40);
        camera.getTransform().lookAt(center);

        // Add some boxes
        final Cube centerCube = new Cube();
        centerCube.getTransform().scaleBy(5);
        boxes.attachChild(centerCube);

        for (int x = -2; x < 2; x++) {
            for (int y = -1; y < 3; y++) {
                final Cube cube = new Cube();
                cube.getTransform().setPosition(x * 15, y * 15, 35);
                cube.getTransform().scaleBy(10);
                boxes.attachChild(cube);
            }
        }
        scene.attachChild(boxes);
    }

    @Override
    public void onUpdate(Keyboard keyboard, Mouse mouse) {
        scene.update();

        float r = .05f;
        boxes.getTransform().setPosition(0, 10, 0);
        if (keyboard.isKeyPressed('W')) {
            boxes.getTransform().addRotation(-r, 0, 0);
        }
        if (keyboard.isKeyPressed('A')) {
            boxes.getTransform().addRotation(0, r, 0);
        }
        if (keyboard.isKeyPressed('S')) {
            boxes.getTransform().addRotation(r, 0, 0);
        }
        if (keyboard.isKeyPressed('D')) {
            boxes.getTransform().addRotation(0, -r, 0);
        }
    }

    @Override
    public void onRender(RenderManager renderer) {
        renderer.render(scene);
    }

}