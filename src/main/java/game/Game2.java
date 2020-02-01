package game;

import engine.components.ActiveMovementComponent;
import engine.components.BackFaceComponent;
import engine.components.KeyboardInputComponent;
import engine.components.MovementComponent;
import engine.components.physics.TerrainCollisionComponent;
import engine.effects.Shadows;
import engine.effects.WaterReflection;
import engine.effects.WaterRefraction;
import engine.engineObjects.Fog;
import engine.entity.Entity;
import engine.gameengine.GameEngine;
import engine.gameengine.SimpleApplication;
import engine.light.DirectionalLight;
import engine.models.Model;
import engine.models.Skin;
import engine.particles.ParticleSystem;
import engine.particles.placers.CircleParticlePlacer;
import engine.postprocessing.contrast.Contrast;
import engine.postprocessing.deferredfog.DeferredFog;
import engine.rendering.RenderManager;
import engine.rendering.background.BackgroundColour;
import engine.rendering.camera.FlyCamera;
import engine.skybox.SkyBox;
import engine.terrain.FlatTerrain;
import engine.terrain.StaticWorld;
import engine.terrain.Terrain;
import engine.terrain.TerrainRenderer;
import engine.terrain.generation.RandomColourGenerator;
import engine.terrain.generation.SimplexNoise;
import engine.terrain.lowpoly.LPTerrainConfigs;
import engine.terrain.lowpoly.LowPolyTerrain;
import engine.terrain.triangle.TerrainNode;
import engine.util.lengths.Proportion;
import engine.util.node.Group;
import engine.util.node.Node;
import engine.water.WaterTile;
import glfw.input.Keyboard;
import glfw.input.Mouse;
import glfw.window.Window;
import maths.utils.Vector3;
import opengl.textures.CubeMapTexture;
import opengl.textures.MultiTexture;
import opengl.textures.Texture2D;
import opengl.utils.GlUtils;
import tegui.TContainer;
import tegui.control.TButton;
import tegui.control.TSlider;
import tegui.control.TToggleButton;
import tegui.objects.TImage;

public class Game2 extends SimpleApplication {

    private final Node mainScreen = new Node();
    private final Node menuScreen = new Node();

    private final Fog fog = new Fog(500, 600, Vector3.of(.7f, .7f, 1));

    private static final int levels = 10;

    public static void main(String[] args) {
        long sum = 0;
        for (int i = 0; i < levels; i++) {
            sum += Math.pow(4, i);
        }
        System.out.println("Potential Triangles: " + sum);

        try {
            SimpleApplication game = new Game2();
            GameEngine gameEngine = new GameEngine("Tarnegol engine preview", 1200, 800, game);
            gameEngine.init();
            gameEngine.start();
        } catch (Exception e) {
            System.err.println("Error running the game");
            e.printStackTrace();
        }
    }

    @Override
    public void onInit(Window window, RenderManager renderer, Keyboard keyboard, Mouse mouse) throws Exception {
        keyboard.keyPressEventListeners().add(e -> {
            if (e.getKeyCode() == Configs.DETACH_MOUSE_KEY && !mouse.isShown()) {
                mouse.setShown(true);
            }
        });
        keyboard.keyPressEventListeners().add(e -> {
            if (e.getKeyCode() == 'H') {
                TerrainRenderer.setLodLevel(TerrainRenderer.getLodLevel() - 1);
            } else if (e.getKeyCode() == 'J') {
                TerrainRenderer.setLodLevel(TerrainRenderer.getLodLevel() + 1);
            }
        });

        //setSensitivity(Configs.MOUSE_SENSITIVITY);

        renderer.setBackground(new BackgroundColour(0, 0, 0));
        renderer.getPostProcessing().add(new Contrast(1.2f));
        renderer.getPostProcessing().add(new DeferredFog(fog, camera));

        final DirectionalLight sun = new DirectionalLight();
        sun.setDirection(Configs.SUN_POSITION);
        sun.setColour(Vector3.of(1, 1, 1));
        sun.setIntensity(0.5f);

        /*
            Load sky box
        */
        CubeMapTexture texture = CubeMapTexture.builder()
                .positiveX("/skyBoxes/right.png")
                .negativeX("/skyBoxes/left.png")
                .positiveY("/skyBoxes/top.png")
                .negativeY("/skyBoxes/bottom.png")
                .positiveZ("/skyBoxes/front.png")
                .negativeZ("/skyBoxes/back.png")
                .create();
        SkyBox skyBox = new SkyBox(texture);
        skyBox.setFogColour(fog.getColour());
        SkyBox menuSkyBox = new SkyBox(texture);

        /*
            Load terrains
        */

        MultiTexture terrainTexture = new MultiTexture(
                Texture2D.of("/terrains/blendMap.png"),
                Texture2D.of("/terrains/grass.png"),
                Texture2D.of("/terrains/grassFlowers.png"),
                Texture2D.of("/terrains/mud.png"),
                Texture2D.of("/terrains/path.png")
        );
        long terrainSeed = 0;
        final Node terrains = new Node();

        final TerrainNode terrain = new TerrainNode(Vector3.zero(), 100, levels);
        terrain.setCamera(camera);
        terrains.attachChild(terrain);

        final Terrain flatTerrain = new FlatTerrain(Vector3.of(-100, 0, 100), 100);
        terrains.attachChild(flatTerrain);
        terrain.getTransform().getPosition().y += 1f;

        final Terrain lowPolyTerrain = new LowPolyTerrain(new LPTerrainConfigs()
                .setHeightGenerator(new SimplexNoise(100, 50, 100, 5))
                .setColourGenerator(new RandomColourGenerator())
                .setPosition(Vector3.of(50, 0, 50))
                .setSize(1000)
        );
        terrains.attachChild(lowPolyTerrain);


        /*
            Load water tiles
        */
        final Node waterTiles = new Node();
        for (int i = 0; i < 9; i++) {
            final WaterTile waterTile = new WaterTile();
            waterTile.setDudvMap(Texture2D.of("/maps/waterDudvMap.png"));
            waterTile.setNormalsMap(Texture2D.of("/maps/waterNormalMap.png"));
            waterTile.getTransform().setPosition((i / 3) * 400, 0, (i % 3) * 400);
            waterTile.getTransform().setScale(400);
            waterTiles.attachChild(waterTile);
        }

        /*
            Load particles
        */
        final Skin skin = loader.loadSkin("/textures/particleFire.png").setTextureRows(4);
        final ParticleSystem particles = new ParticleSystem(new CircleParticlePlacer(10), skin);

        /*
            Load entities
        */
        final Node entities = new Node();

        final Skin playerSkin = loader.loadSkin("/textures/player.png");
        final Model playerModel = loader.loadModel("/objModels/player.obj");
        final Entity player = new Entity(playerModel, playerSkin);
        player.getComponents().add(new MovementComponent());
        player.getComponents().add(new KeyboardInputComponent(keyboard));
        player.getComponents().add(new ActiveMovementComponent(30, 10, 200, 2));
        player.getComponents().add(new BackFaceComponent(camera.getTransform()));
        player.getComponents().add(new TerrainCollisionComponent(new StaticWorld()));
        entities.attachChild(player);

        final Skin tree1Skin = loader.loadSkin("/textures/lowPolyTree.png");
        final Model tree1Model = loader.loadModel("/objModels/lowPolyTree.obj");
        final Entity tree1 = new Entity(tree1Model, tree1Skin);
        tree1.getTransform().setPosition(0, 20, 0);
        entities.attachChild(tree1);

        final Skin horseSkin = loader.loadSkin("/textures/horse.png");
        final Model horseModel = loader.loadModel("/objModels/horse.obj");
        final Entity horse = new Entity(horseModel, horseSkin);
        horse.getTransform().setPosition(10, 20, 0);
        entities.attachChild(horse);

        final Skin tree2Skin = loader.loadSkin("/textures/tree.png");
        final Model tree2Model = loader.loadModel("/objModels/tree.obj");
        final Entity tree2 = new Entity(tree2Model, tree2Skin);
        tree2.getTransform().setPosition(20, 20, 0);
        tree2.getTransform().setScale(10);
        entities.attachChild(tree2);

        final Skin barrelSkin = loader.loadSkin("/textures/barrel.png");
        final Model barrelModel = loader.loadModel("/objModels/barrel.obj");
        final Entity barrel = new Entity(barrelModel, barrelSkin);
        barrel.getTransform().setPosition(33, 30, 0);
        entities.attachChild(barrel);

        camera.setController(new FlyCamera(keyboard, mouse));

        // Shadow
        final Group shadows = new Group();
        shadows.attachChild(entities);
        scene.addEffect(new Shadows(shadows, sun));
        scene.getEffect(Shadows.class).setEnabled(false);

        // Water effects
        final Group waterEffects = new Group();
        waterEffects.attachChild(sun);
        waterEffects.attachChild(skyBox);
        waterEffects.attachChild(entities);
        waterEffects.attachChild(terrains);
        waterEffects.attachChild(particles);
        scene.addEffect(new WaterReflection(waterEffects));
        scene.addEffect(new WaterRefraction(waterEffects));
        scene.getEffect(WaterReflection.class).setEnabled(false);
        scene.getEffect(WaterRefraction.class).setEnabled(false);

        // World
        final Node world = new Node();
        world.attachChild(sun);
        world.attachChild(skyBox);
        world.attachChild(terrains);
        world.attachChild(entities);
        world.attachChild(particles);
        world.attachChild(waterTiles);
        mainScreen.attachChild(world);

        final TContainer inGameGui = new TContainer();

        final TButton fromGameToMenuBtn = new TButton();
        fromGameToMenuBtn.getStyle().position.set(900, 670);
        fromGameToMenuBtn.getStyle().dimensions.set(150, 120);
        fromGameToMenuBtn.setImage(new TImage(Texture2D.of("/textures/arrowRight.png")));

        inGameGui.attachChild(fromGameToMenuBtn);
        mainScreen.attachChild(inGameGui);
        fromGameToMenuBtn.setOnAction(e -> {
            scene.replaceChild(mainScreen, menuScreen);
        });

        // Menu Frame
        final TContainer panel = new TContainer();
        menuScreen.attachChild(panel);

        final TButton buttonPlay = new TButton();
        buttonPlay.getStyle().bounds.setMiddleX(Proportion.of(.5f));
        buttonPlay.getStyle().y.set(300);
        buttonPlay.getStyle().dimensions.set(200, 150);
        buttonPlay.setImage(new TImage(Texture2D.of("/textures/playButton.png")));
        panel.attachChild(buttonPlay);

        final TButton buttonSettings = new TButton();
        buttonSettings.getStyle().bounds.setMiddleX(Proportion.of(.5f));
        buttonSettings.getStyle().y.set(470);
        buttonSettings.getStyle().dimensions.set(200, 150);
        buttonSettings.setImage(new TImage(Texture2D.of("/textures/settingsButton.png")));
        panel.attachChild(buttonSettings);

        final TButton buttonQuitMain = new TButton();
        buttonQuitMain.getStyle().bounds.setMiddleX(Proportion.of(.5f));
        buttonQuitMain.getStyle().y.set(640);
        buttonQuitMain.getStyle().dimensions.set(200, 150);
        buttonQuitMain.setImage(new TImage(Texture2D.of("/textures/quitButton.png")));
        panel.attachChild(buttonQuitMain);

        final TSlider radiusSlider = new TSlider();
        radiusSlider.getStyle().backgroundColour.set(0, 0, 0, 255);
        radiusSlider.dynamicValueProperty().addListener((o, old, value) -> {
            buttonPlay.getStyle().radiuses.set(value);
            buttonSettings.getStyle().radiuses.set(value);
            buttonQuitMain.getStyle().radiuses.set(value);
        });
        radiusSlider.getStyle().x.set(Proportion.of(.1f));
        radiusSlider.getStyle().y.set(Proportion.of(.1f));
        radiusSlider.getStyle().width.set(Proportion.of(.4f));
        radiusSlider.getStyle().height.set(40);

        radiusSlider.getSlider().getStyle().backgroundColour.set(.3f, .3f, 1, 1, true);
        panel.attachChild(radiusSlider);

        // Settings Frame
        final Node settingsNode = new Node();
        final TContainer settingsPanel = new TContainer();
        settingsNode.attachChild(settingsPanel);

        final TButton buttonQuitSettings = new TButton();
        buttonQuitSettings.getStyle().bounds.setMiddleX(Proportion.of(.5f));
        buttonQuitSettings.getStyle().y.set(640);
        buttonQuitSettings.getStyle().dimensions.set(200, 150);
        buttonQuitSettings.setImage(new TImage(Texture2D.of("/textures/quitButton.png")));
        settingsPanel.attachChild(buttonQuitSettings);

        final TToggleButton toggleShadows = new TToggleButton(100, 100);
        settingsPanel.attachChild(toggleShadows);
        toggleShadows.valueProperty().addListener((o, old, current) -> {
            scene.getEffect(Shadows.class).setEnabled(current);
            System.out.println("\nShadows enabled");
        });
        final TImage toggleShadowsImage = new TImage(Texture2D.of("/textures/shadows.png"));
        toggleShadowsImage.getStyle().backgroundColour.set(.1f, .5f, .7f, 1, true);
        toggleShadowsImage.getStyle().position.set(700, 90);
        toggleShadowsImage.getStyle().dimensions.set(200, 150);
        settingsNode.attachChild(toggleShadowsImage);

        final TToggleButton toggleWaterEffects = new TToggleButton(100, 250);
        settingsPanel.attachChild(toggleWaterEffects);
        toggleWaterEffects.valueProperty().addListener((o, old, current) -> {
            scene.getEffect(WaterReflection.class).setEnabled(current);
            scene.getEffect(WaterRefraction.class).setEnabled(current);
            System.out.println("\nWater enabled");
        });
        final TImage toggleWaterEffectsImage = new TImage(Texture2D.of("/textures/waterEffects.png"));
        toggleWaterEffectsImage.getStyle().backgroundColour.set(.1f, .5f, .7f, 1, true);
        toggleWaterEffectsImage.getStyle().position.set(700, 250);
        toggleWaterEffectsImage.getStyle().dimensions.set(200, 100);
        settingsNode.attachChild(toggleWaterEffectsImage);

        final TButton buttonToMenu = new TButton();
        buttonToMenu.getStyle().position.set(900, 670);
        buttonToMenu.getStyle().dimensions.set(150, 120);
        buttonToMenu.setImage(new TImage(Texture2D.of("/textures/arrowRight.png")));

        settingsPanel.attachChild(buttonToMenu);
        buttonToMenu.setOnAction(e -> {
            menuScreen.replaceChild(settingsNode, panel);
            camera.setEnabled(false);
        });

        menuScreen.attachChild(menuSkyBox);
        menuSkyBox.getTransform().addRotation(-45, 0, 0);

        scene.attachChild(menuScreen);

        buttonPlay.setOnAction(e -> {
            scene.replaceChild(menuScreen, mainScreen);
            camera.setEnabled(true);
        });
        buttonSettings.setOnAction(e -> {
            menuScreen.replaceChild(panel, settingsNode);
        });
        buttonQuitMain.setOnAction(e -> {
            closeGame();
        });
        buttonQuitSettings.setOnAction(e -> {
            closeGame();
        });

        final TButton lodButton = new TButton();
        lodButton.getStyle().position.set(100, 50);
        lodButton.getStyle().dimensions.set(140, 100);
        inGameGui.attachChild(lodButton);

        camera.setEnabled(false);
        //SkyBoxRenderer.getInstance().setFog(new Fog(.001f, 20, Vector3.of(1, .5f, 0)));
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