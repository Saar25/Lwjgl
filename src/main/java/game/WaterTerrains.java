package game;

import engine.effects.Shadows;
import engine.effects.WaterReflection;
import engine.effects.WaterRefraction;
import engine.entity.Entity;
import engine.gameengine.GameEngine;
import engine.gameengine.SimpleApplication;
import engine.light.DirectionalLight;
import engine.models.Model;
import engine.models.Skin;
import engine.rendering.RenderManager;
import engine.rendering.background.BackgroundGradientColour;
import engine.rendering.camera.CameraControllers;
import engine.rendering.camera.CameraKeyboardSmoothMovement;
import engine.rendering.camera.CameraMouseDragSmoothRotation;
import engine.shape.Shape;
import engine.shape.Sphere;
import engine.terrain.Terrain;
import engine.terrain.generation.ColourGenerator;
import engine.terrain.generation.HeightColourGenerator;
import engine.terrain.generation.NormalColourGenerator;
import engine.terrain.generation.PerlinNoise;
import engine.terrain.lowpoly.LPTerrainConfigs;
import engine.terrain.lowpoly.LowPolyTerrain;
import engine.util.lengths.Proportion;
import engine.util.node.Group;
import engine.water.lowpoly.LowpolyWavyWater;
import engine.water.wavy.WavyWater;
import glfw.input.Keyboard;
import glfw.input.Mouse;
import glfw.window.Window;
import maths.utils.Vector3;
import opengl.textures.Texture2D;
import opengl.utils.GlUtils;
import tegui.TPanel;
import tegui.control.TSlider;
import tegui.objects.TImage;
import tegui.style.Style;
import tegui.style.property.Colour;
import tegui.style.property.Orientation;

public class WaterTerrains extends SimpleApplication {

    public static void main(String[] args) {
        try {
            SimpleApplication game = new WaterTerrains();
            GameEngine gameEngine = new GameEngine("WaterTerrains", 1200, 741, game);
            gameEngine.init();
            gameEngine.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onInit(Window window, RenderManager renderer, Keyboard keyboard, Mouse mouse) throws Exception {
        final DirectionalLight sun = new DirectionalLight(Vector3.of(2, 7, 5), Vector3.of(1f, 1f, 1f), 1f);
        scene.attachChild(sun);

        final ColourGenerator colourGenerator = new HeightColourGenerator()
                .withColour(-5.0f, Vector3.of(.76f, .69f, .50f))
                .withColour(30.0f, Vector3.of(0.5f, 0.6f, 0.0f))
                .withColour(100.f, Vector3.of(.41f, .41f, .41f));

        final ColourGenerator colourGenerator2 = new NormalColourGenerator(Vector3.upward())
                .withColour(0.8f, Vector3.of(.41f, .41f, .41f))
                .withColour(1.0f, Vector3.of(0.5f, 0.6f, 0.0f));

        final Terrain terrain = new LowPolyTerrain(new LPTerrainConfigs()
                .setHeightGenerator(new PerlinNoise(.03f, 800, 3, 50))
                .setPosition(Vector3.of(0, 0, 0))
                .setSize(1000).setVertices(32)
                .setColourGenerator(colourGenerator2)
        );
        scene.attachChild(terrain);

        final WavyWater water = new LowpolyWavyWater(terrain, 256);
        scene.attachChild(water);

        final Shape shape = new Sphere();
        shape.getTransform().setScale(10);
        shape.getTransform().setPosition(20, 150, 20);
        scene.attachChild(shape);

        final Model model = loader.loadModel("/tree.obj");
        final Skin skin = loader.loadSkin("/tree.png");
        final Entity entity = new Entity(model, skin);
        entity.getTransform().setPosition(10, terrain.getHeight(10, 10), 10);
        entity.getTransform().setScale(10);
        scene.attachChild(entity);

        final Group group = new Group();
        group.attachChild(shape);
        group.attachChild(terrain);
        group.attachChild(entity);
        scene.addEffect(new Shadows(group, sun));

        final Group waterEffects = new Group();
        waterEffects.attachChild(terrain);
        waterEffects.attachChild(sun);
        final WaterReflection reflection = new WaterReflection(waterEffects, 2560, 1440);
        final WaterRefraction refraction = new WaterRefraction(waterEffects);
        scene.addEffect(reflection);
        scene.addEffect(refraction);

        final Style defaultStyle = new Style();
        defaultStyle.backgroundColour.set(128, 128, 128, 255);
        defaultStyle.borderColour.set(255, 255, 255, 255);
        defaultStyle.dimensions.set(200, 200);
        defaultStyle.borders.set(5);

        final TImage image1 = new TImage(new Texture2D(reflection.getTexture()));
        image1.getStyle().setParent(defaultStyle);
        image1.getStyle().position.set(10, 10);
        scene.attachChild(image1);

        final TImage image2 = new TImage(new Texture2D(refraction.getTexture()));
        image2.getStyle().setParent(defaultStyle);
        image2.getStyle().position.set(225, 10);
        scene.attachChild(image2);

        final TImage image3 = new TImage(new Texture2D(scene.getEffect(Shadows.class).getTexture()));
        image3.getStyle().setParent(defaultStyle);
        image3.getStyle().position.set(440, 10);
        scene.attachChild(image3);

        final TPanel panel = new TPanel();
        panel.getStyle().position.set(50, 500);
        panel.getStyle().dimensions.set(400, 130);
        panel.getStyle().backgroundColour.set(0, 0, 0, 200);

        final Colour orange = new Colour(250, 135, 52, 180);
        final Colour purple = new Colour(172, 10, 184, 180);

        panel.getChildrenStyle().height.set(40);
        panel.getChildrenStyle().width.set(Proportion.of(.9f));
        panel.getChildrenStyle().x.set(Proportion.of(.05f));
        panel.getChildrenStyle().radiuses.set(20);
        panel.getChildrenStyle().borders.set(1);
        panel.getChildrenStyle().backgroundColour.set(
                orange, purple, Orientation.HORIZONTAL);

        final TSlider waterAmplitudeSlider = new TSlider();
        waterAmplitudeSlider.getStyle().y.set(70);

        water.amplitudeProperty().bind(waterAmplitudeSlider.dynamicValueProperty());
        waterAmplitudeSlider.setBounds(0, 10);
        waterAmplitudeSlider.setValue(1);

        final TSlider terrainAmplitudeSlider = new TSlider();
        terrainAmplitudeSlider.getStyle().y.set(20);

        terrain.amplitudeProperty().bind(terrainAmplitudeSlider.dynamicValueProperty());
        terrainAmplitudeSlider.setBounds(0, 10);
        terrainAmplitudeSlider.setValue(1);

        panel.getChildren().add(terrainAmplitudeSlider);
        panel.getChildren().add(waterAmplitudeSlider);
        scene.attachChild(panel);

        keyboard.keyPressEventListeners().add(e -> {
            if (e.getKeyCode() == 'E') {
                final int s = 200 - image1.getStyle().dimensions.getWidth();
                image1.getStyle().dimensions.set(s, s);
                image2.getStyle().dimensions.set(s, s);
                image3.getStyle().dimensions.set(s, s);
            }
        });

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

        if (keyboard.isKeyPressed('R')) {
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