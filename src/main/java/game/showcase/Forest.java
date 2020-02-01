package game.showcase;

import engine.components.ActiveMovementComponent;
import engine.components.AutoJumpComponent;
import engine.components.MovementComponent;
import engine.components.ai.AIComponent;
import engine.components.animation.SimpleAnimationComponent;
import engine.components.physics.GravityComponent;
import engine.components.physics.TerrainCollisionComponent;
import engine.effects.Shadows;
import engine.effects.WaterReflection;
import engine.effects.WaterRefraction;
import engine.entity.Entity;
import engine.entity.EntityBlueprint;
import engine.gameengine.Application;
import engine.gameengine.GameEngine;
import engine.gameengine.SimpleApplication;
import engine.light.DirectionalLight;
import engine.models.Model;
import engine.models.Skin;
import engine.postprocessing.deferredshadows.DeferredShadows;
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
import tegui.style.property.Colour;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Forest extends SimpleApplication {

    public static void main(String[] args) {
        try {
            Application game = new Forest();
            GameEngine gameEngine = new GameEngine("Forest", 1200, 741, game);
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
                .positiveX("/skyBoxes/right.png")
                .negativeX("/skyBoxes/left.png")
                .positiveY("/skyBoxes/top.png")
                .negativeY("/skyBoxes/bottom.png")
                .positiveZ("/skyBoxes/front.png")
                .negativeZ("/skyBoxes/back.png")
                .create();
        final SkyBox skyBox = new SkyBox(texture);
        skyBox.setSkyColour(Colour.BLACK);
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

        final Node entities = new Node();

        Skin skin = loader.loadSkin("/textures/horse.png");
        Model model = loader.loadModel("/objModels/horse.obj");
        final EntityBlueprint horse = new EntityBlueprint(model, skin);

        skin = loader.loadSkin("/textures/lowPolyTree.png");
        model = loader.loadModel("/objModels/lowPolyTree.obj");
        final EntityBlueprint tree1 = new EntityBlueprint(model, skin);

        skin = loader.loadSkin("/textures/tree.png");
        model = loader.loadModel("/objModels/tree.obj");
        final EntityBlueprint tree2 = new EntityBlueprint(model, skin);

        skin = loader.loadSkin("/textures/barrel.png");
        model = loader.loadModel("/objModels/barrel.obj");
        final EntityBlueprint barrelBp = new EntityBlueprint(model, skin);

        model = loader.loadModel("/objModels/fern.obj");
        final List<EntityBlueprint> fernModels = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            skin = loader.loadSkin("/textures/fern.png");
            skin.setTextureRows(2);
            skin.setTextureIndex(i);
            skin.setTransparent(true);
            fernModels.add(new EntityBlueprint(model, skin));
        }

        final Random random = new Random();

        final float from = -Configs.TERRAIN_SIZE * terrainCount / 2;
        final float until = Configs.TERRAIN_SIZE * terrainCount / 2;
        int entityCount = 10000;
        while (entityCount > 0) {
            float x = random.nextFloat() * (until - from) + from;
            float z = random.nextFloat() * (until - from) + from;
            float h = world.getHeight(x, z);
            if (h >= 0) {
                double rand = Math.random();
                if (rand < .5f) {
                    Entity entity = tree1.createInstance();
                    entity.getTransform().setPosition(x, h, z);
                    entity.getTransform().addRotation(0, random.nextFloat() * 360, 0);
                    entities.attachChild(entity);
                } else {
                    Entity entity = tree2.createInstance();
                    entity.getTransform().setPosition(x, h, z);
                    entity.getTransform().addRotation(random.nextFloat() * 20 - 10, 0, random.nextFloat() * 20 - 10);
                    entity.getTransform().setScale(10);
                    entities.attachChild(entity);
                }
                entityCount--;
            }

            x = random.nextFloat() * (until - from) + from;
            z = random.nextFloat() * (until - from) + from;
            h = world.getHeight(x, z);

            if (h > 0) {
                final int fernIndex = random.nextInt(4);
                final Entity entity = fernModels.get(fernIndex).createInstance();
                entity.getTransform().setPosition(x, world.getHeight(x, z), z);
                entities.attachChild(entity);
                entityCount--;
            }
        }

        float h = world.getHeight(20, 20);
        Entity barrel = barrelBp.createInstance();
        barrel.getTransform().setPosition(20, h, 20);
        entities.attachChild(barrel);

        horse.getComponents().add(() -> new AIComponent());
        horse.getComponents().add(() -> new GravityComponent());
        horse.getComponents().add(() -> new MovementComponent());
        horse.getComponents().add(() -> new AutoJumpComponent());
        horse.getComponents().add(() -> new SimpleAnimationComponent());
        horse.getComponents().add(() -> new TerrainCollisionComponent(world));
        horse.getComponents().add(() -> new ActiveMovementComponent(30, 5, 10, 2));

        Entity h1 = horse.createInstance();
        h1.getTransform().setPosition(-5, 10, 20);

        Entity h2 = horse.createInstance();
        h2.getTransform().setPosition(+5, 10, 20);

        entities.attachChild(h1);
        entities.attachChild(h2);

        scene.attachChild(entities);

        final Group waterEffects = new Group();
        waterEffects.attachChild(sun);
        waterEffects.attachChild(world);
        waterEffects.attachChild(skyBox);
        waterEffects.attachChild(entities);
        scene.addEffect(new WaterReflection(waterEffects));
        scene.addEffect(new WaterRefraction(waterEffects));

        final Group shadowEffects = new Group();
        shadowEffects.attachChild(world);
        shadowEffects.attachChild(entities);
        scene.addEffect(new Shadows(shadowEffects, sun));

        renderer.getPostProcessing().add(new DeferredShadows(scene.getEffect(Shadows.class), camera));

        camera.setController(new CameraControllers(
                new CameraMouseDragSmoothRotation(mouse),
                new CameraKeyboardSmoothMovement(keyboard)));
        keyboard.onKeyPress('T').perform(e -> mouse.setShown(!mouse.isShown()));
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