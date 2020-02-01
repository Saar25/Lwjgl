package game.showcase;

import engine.effects.WaterReflection;
import engine.effects.WaterRefraction;
import engine.gameengine.Application;
import engine.gameengine.GameEngine;
import engine.gameengine.SimpleApplication;
import engine.light.DirectionalLight;
import engine.rendering.RenderManager;
import engine.rendering.camera.CameraControllers;
import engine.rendering.camera.CameraKeyboardSmoothMovement;
import engine.rendering.camera.CameraMouseDragSmoothRotation;
import engine.skybox.SkyBox;
import engine.terrain.StaticWorld;
import engine.terrain.World;
import engine.terrain.multitexture.MultiTextureTerrain;
import engine.util.node.Group;
import engine.util.node.Node;
import engine.water.wavy.WavyWater;
import game.Configs;
import glfw.input.Keyboard;
import glfw.input.Mouse;
import glfw.window.Window;
import maths.joml.Vector3f;
import maths.utils.Vector3;
import opengl.textures.CubeMapTexture;
import opengl.textures.MultiTexture;
import opengl.textures.Texture2D;
import org.lwjgl.glfw.GLFW;

public class WaterTerrain2 extends SimpleApplication {

    public static void main(String[] args) {
        try {
            Application game = new WaterTerrain2();
            GameEngine gameEngine = new GameEngine("WaterTerrain2", 1200, 741, game);
            gameEngine.init();
            gameEngine.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInit(Window window, RenderManager renderer, Keyboard keyboard, Mouse mouse) throws Exception {
        //renderer.getPostProcessing().add(new Contrast(1.4f));

        final DirectionalLight sun = new DirectionalLight();
        sun.setDirection(Vector3.of(1, .8f, 1));
        sun.setColour(Vector3.of(1, 1, 1));
        sun.setIntensity(1);
        scene.attachChild(sun);

        final CubeMapTexture texture = CubeMapTexture.builder()
                .positiveX("/skyBoxes/right.png").negativeX("/skyBoxes/left.png")
                .positiveY("/skyBoxes/top.png").negativeY("/skyBoxes/bottom.png")
                .positiveZ("/skyBoxes/front.png").negativeZ("/skyBoxes/back.png")
                .create();
        final SkyBox skyBox = new SkyBox(texture);
        scene.attachChild(skyBox);

        final Node terrainWater = new Node();
        final MultiTexture multiTexture = new MultiTexture(
                Texture2D.of("/terrains/blendMap2.png"),
                Texture2D.of("/terrains/grass.png"),
                Texture2D.of("/terrains/mud.png"),
                Texture2D.of("/terrains/grassFlowers.png"),
                Texture2D.of("/terrains/path.png"));

        final World world = new StaticWorld();

        final int terrainCount = 5;

        for (int x = -terrainCount / 2; x <= terrainCount / 2; x++) {
            for (int z = -terrainCount / 2; z <= terrainCount / 2; z++) {
                final Vector3f position = Vector3.of(x, 0, z).mul(Configs.TERRAIN_SIZE);
                final MultiTextureTerrain terrain = new MultiTextureTerrain(
                        position, Configs.TERRAIN_SIZE, multiTexture);
                world.getChildren().add(terrain);

                terrainWater.attachChild(new WavyWater(terrain));
            }
        }

        terrainWater.attachChild(world);
        scene.attachChild(terrainWater);

        camera.setController(new CameraControllers(
                new CameraKeyboardSmoothMovement(keyboard),
                new CameraMouseDragSmoothRotation(mouse)
        ));
        mouse.setShown(false);
        keyboard.onKeyPress(GLFW.GLFW_KEY_ESCAPE).perform(
                e -> mouse.setShown(!mouse.isShown()));

        final Group waterEffects = new Group();
        waterEffects.attachChild(sun);
        waterEffects.attachChild(world);
        waterEffects.attachChild(skyBox);
        scene.addEffect(new WaterReflection(waterEffects));
        scene.addEffect(new WaterRefraction(waterEffects));
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