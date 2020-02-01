package game;

import engine.dayNightCycle.DayNightManager;
import engine.dayNightCycle.Hour;
import engine.effects.Shadows;
import engine.effects.WaterReflection;
import engine.effects.WaterRefraction;
import engine.engineObjects.Fog;
import engine.entity.Entity;
import engine.gameengine.GameEngine;
import engine.gameengine.SimpleApplication;
import engine.light.DirectionalLight;
import engine.light.PointLight;
import engine.postprocessing.contrast.Contrast;
import engine.postprocessing.deferredfog.DeferredFog;
import engine.postprocessing.sunshafts.SunShafts;
import engine.rendering.RenderManager;
import engine.rendering.background.BackgroundColour;
import engine.rendering.camera.ThirdPersonCamera;
import engine.terrain.ProceduralGeneration;
import engine.terrain.World;
import engine.util.lengths.Proportion;
import engine.util.node.Group;
import engine.util.node.Node;
import glfw.input.Keyboard;
import glfw.input.Mouse;
import glfw.window.Window;
import maths.utils.Maths;
import maths.utils.Vector3;
import opengl.textures.Texture2D;
import opengl.utils.GlUtils;
import tegui.control.TProgressBar;
import tegui.control.TSlider;
import tegui.objects.TImage;
import tegui.style.property.Colour;
import tegui.style.property.Orientation;

public class Game extends SimpleApplication {

    private DayNightManager dayNightManager;
    private DirectionalLight sun;
    private Fog fog;

    public static void main(String[] args) {
        try {
            SimpleApplication game = new Game();
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
        final TImage image = new TImage(Texture2D.of("/textures/SaarLogo.png"));
        image.getStyle().position.set(250, 50);
        image.getStyle().dimensions.set(700, 700);

        final TProgressBar progressBar = new TProgressBar();
        progressBar.getStyle().position.set(250, 650);
        progressBar.getStyle().dimensions.set(700, 50);

        final Node loadingScreen = new Node();
        loadingScreen.attachChild(image);
        loadingScreen.attachChild(progressBar);
        progressBar.setValue(0);

        progressBar.setValue(10);
        renderer.render(loadingScreen, camera);
        window.swapBuffers();

        fog = new Fog(800, 900, Vector3.create());

        renderer.getPostProcessing().add(new DeferredFog(fog, camera));
        renderer.getPostProcessing().add(new Contrast(1.4f));
        renderer.getPostProcessing().add(new SunShafts(camera, Vector3.of(100, 5, 10)));
        //renderer.getPostProcessing().add(new Bloom(.2126f, .7152f, .0722f));

        sun = new DirectionalLight();
        sun.setDirection(Configs.SUN_POSITION);
        sun.setColour(Vector3.of(1, 1, 1));
        sun.setIntensity(0.5f);

        final PointLight light = new PointLight();
        light.setColour(Configs.LIGHT_COLOUR);
        light.setPosition(Configs.LIGHT_POSITION);
        light.setAttenuation(Configs.LIGHT_ATTENUATION);
        light.setIntensity(0.07f);

        final Node lights = new Node();
        lights.attachChild(light);
        lights.attachChild(sun);

        // Day night cycle
        dayNightManager = new DayNightManager(Configs.DAY_NIGHT_SPM);
        dayNightManager.addKeyFrame(Configs.DAY_NIGHT_1100, Hour.of(11, 0));
        dayNightManager.addKeyFrame(Configs.DAY_NIGHT_0600, Hour.of(6, 0));
        dayNightManager.addKeyFrame(Configs.DAY_NIGHT_0000, Hour.of(0, 0));

        progressBar.setValue(20);
        renderer.render(loadingScreen, camera);
        window.swapBuffers();

        // Load scene
        final WorldLoader worldLoader = new WorldLoader(loader);
        final World terrainWorld = worldLoader.loadTerrains();
        terrainWorld.setGeneration(new ProceduralGeneration(2, camera));

        progressBar.setValue(30);
        renderer.render(loadingScreen, camera);
        window.swapBuffers();

        final Node terrains = new Node();
        terrains.attachChild(terrainWorld);
        final Node skyBoxes = worldLoader.loadSkyBoxes();
        final Node waterTiles = worldLoader.loadWaterTiles();
        terrainWorld.generateWater(waterTiles);

        progressBar.setValue(50);
        renderer.render(loadingScreen, camera);
        window.swapBuffers();

        final Node entities = new Node();
        final Entity player = worldLoader.loadPlayer(terrainWorld, keyboard, camera);
        entities.attachChild(worldLoader.loadZombies(terrainWorld, player, 0));
        entities.attachChild(worldLoader.loadEntities(terrainWorld));
        entities.attachChild(player);

        progressBar.setValue(70);
        renderer.render(loadingScreen, camera);
        window.swapBuffers();

        final Node particles = worldLoader.loadParticleSystems();
        final Node images = worldLoader.loadImages();

        //renderer.setFog(Fog.NONE);
        renderer.setBackground(new BackgroundColour(0, 0, 0));
        camera.setController(new ThirdPersonCamera(mouse, player));

        // Shadows
        final Group shadowCasters = new Group();
        shadowCasters.attachChild(entities);
        //shadowCasters.attachChild(terrainWorld);
        scene.addEffect(new Shadows(shadowCasters, sun));

        // Water reflection and refraction
        final Group waterEffects = new Group();
        waterEffects.attachChild(lights);
        waterEffects.attachChild(entities);
        waterEffects.attachChild(terrains);
        waterEffects.attachChild(skyBoxes);
        waterEffects.attachChild(particles);
        scene.addEffect(new WaterReflection(waterEffects));
        scene.addEffect(new WaterRefraction(waterEffects));

        Node mainScene = new Node();

        // World
        final Node world = new Node();
        world.attachChild(lights);
        world.attachChild(skyBoxes);
        world.attachChild(terrains);
        world.attachChild(entities);
        world.attachChild(particles);
        world.attachChild(waterTiles);
        mainScene.attachChild(world);

        // Gui
        final Node imagesNode = new Node();
        imagesNode.attachChild(images);
        mainScene.attachChild(imagesNode);

        final Node gui = new Node();

        final TImage shadowsImage = new TImage(new Texture2D(scene.getEffect(Shadows.class).getTexture()));
        shadowsImage.getStyle().x.set(Proportion.of(.1f));
        shadowsImage.getStyle().y.set(Proportion.of(.1f));
        shadowsImage.getStyle().dimensions.set(200, 200);
        shadowsImage.getStyle().borderColour.set(Colour.WHITE);
        shadowsImage.getStyle().borders.set(4);
        gui.attachChild(shadowsImage);

        final TSlider slider = new TSlider();
        slider.getStyle().x.set(600);
        slider.getStyle().y.set(Proportion.of(.1f));
        slider.getStyle().width.set(500);
        slider.getStyle().height.set(40);
        slider.getStyle().radiuses.set(10);
        slider.getStyle().backgroundColour.set(0, 0, 0, 255);

        slider.getSlider().getStyle().width.set(30);
        slider.getSlider().getStyle().y.set(Proportion.of(-.5f));
        slider.getSlider().getStyle().height.set(Proportion.of(2f));
        slider.getSlider().getStyle().backgroundColour.set(Colour.BLACK, Colour.CYAN, Orientation.VERTICAL);

        slider.setBounds(0, 1);
        slider.dynamicValueProperty().addListener((o, old, current) -> {
            int hours = (int) (24 * current);
            int minutes = (int) (24 * 60 * current) - hours * 60;
            dayNightManager.setCurrentHour(Hour.of(hours, minutes, 0));
        });
        gui.attachChild(slider);

        scene.attachChild(gui);
        scene.attachChild(mainScene);

        progressBar.setValue(90);
        renderer.render(loadingScreen, camera);
        window.swapBuffers();
    }

    @Override
    public void onUpdate(Keyboard keyboard, Mouse mouse) {
        if (keyboard.isKeyPressed(Configs.DRAW_LINES_KEY))
            GlUtils.drawPolygonLine();
        if (keyboard.isKeyPressed(Configs.DRAW_FILLED_KEY))
            GlUtils.drawPolygonFill();

        scene.update();

        dayNightManager.update();
        sun.getColour().set(dayNightManager.getCurrentColour());
        fog.getColour().set(dayNightManager.getCurrentColour());
        final float time = dayNightManager.getCurrentHour().getNormalizedTime();

        final float angle = Maths.PI * time;
        sun.getDirection().x = (float) Math.cos(angle);
        sun.getDirection().y = (float) Math.sin(angle) + 1;
    }

    @Override
    public void onRender(RenderManager renderer) {
        renderer.setShadowPower(dayNightManager.getCurrentColour().lengthSquared() / 4);
        renderer.render(scene);
    }
}
