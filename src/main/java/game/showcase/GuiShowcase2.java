package game.showcase;

import engine.gameengine.GameEngine;
import engine.gameengine.SimpleApplication;
import engine.light.DirectionalLight;
import engine.postprocessing.contrast.Contrast;
import engine.postprocessing.radialblur.RadialBlur;
import engine.rendering.RenderManager;
import engine.shape.Shape;
import engine.shape.Sphere;
import engine.skybox.SkyBox;
import engine.terrain.Terrain;
import engine.terrain.generation.HeightColourGenerator;
import engine.terrain.generation.SimplexNoise;
import engine.terrain.lowpoly.LPTerrainConfigs;
import engine.terrain.lowpoly.LowPolyTerrain;
import engine.util.lengths.Proportion;
import glfw.input.Keyboard;
import glfw.input.Mouse;
import glfw.window.Window;
import maths.joml.Quaternionf;
import maths.utils.Quaternion;
import maths.utils.Vector3;
import opengl.textures.CubeMapTexture;
import opengl.utils.GlUtils;
import tegui.TContainer;
import tegui.control.TCheckBox;
import tegui.control.TProgressBar;
import tegui.control.TSlider;
import tegui.control.TToggleButton;
import tegui.driver.LinearFloatDriver;
import tegui.style.Style;
import tegui.style.property.Colour;

public class GuiShowcase2 extends SimpleApplication {

    public static void main(String[] args) {
        try {
            SimpleApplication game = new GuiShowcase2();
            GameEngine gameEngine = new GameEngine("GuiShowcase2", 1200, 741, game, true);
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
        keyboard.onKeyPress('T').perform(e -> GlUtils.drawPolygonLine());
        keyboard.onKeyRelease('T').perform(e -> GlUtils.drawPolygonFill());

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

        final SkyBox skyBox = new SkyBox(CubeMapTexture.builder()
                .positiveX("/skyBoxes/space/right.png")
                .positiveY("/skyBoxes/space/top.png")
                .positiveZ("/skyBoxes/space/front.png")
                .negativeX("/skyBoxes/space/left.png")
                .negativeY("/skyBoxes/space/bottom.png")
                .negativeZ("/skyBoxes/space/back.png")
                .create()
        );
        skyBox.setSkyColour(Colour.TRANSPARENT);

        final TContainer panel = new TContainer();
        createToggleButtons(panel, renderer);
        createRotationSliders(panel);
    }

    private void createRotationSliders(TContainer container) {
        final Style style = new Style();
        style.backgroundColour.set(Colour.BLACK);
        style.borderColour.set(Colour.WHITE);
        style.dimensions.set(200, 20);
        style.radiuses.set(5);
        style.borders.set(3);

        final TSlider xSlider = new TSlider(50, 50);
        xSlider.getStyle().setParent(style);
        xSlider.setBounds(-1, 1);
        xSlider.dynamicValueProperty().addListener(
                (o, old, value) -> quaternion.x = value);
        container.attachChild(xSlider);

        final TSlider ySlider = new TSlider(50, 150);
        ySlider.getStyle().setParent(style);
        ySlider.setBounds(-1, 1);
        ySlider.dynamicValueProperty().addListener(
                (o, old, value) -> quaternion.y = value);
        container.attachChild(ySlider);

        final TSlider zSlider = new TSlider(50, 250);
        zSlider.getStyle().setParent(style);
        zSlider.setBounds(-1, 1);
        zSlider.dynamicValueProperty().addListener(
                (o, old, value) -> quaternion.z = value);
        container.attachChild(zSlider);

        final TSlider wSlider = new TSlider(50, 350);
        wSlider.getStyle().setParent(style);
        wSlider.setBounds(-1, 1);
        wSlider.dynamicValueProperty().addListener(
                (o, old, value) -> quaternion.w = value);
        wSlider.setValue(1);
        container.attachChild(wSlider);
    }

    private void createToggleButtons(TContainer container, RenderManager renderer) throws Exception {
        final Style style = new Style();
        style.backgroundColour.set(0, 0, 0, 255);
        style.radiuses.set(5);
        style.borders.set(3);
        style.borderColour.set(255, 255, 255, 255);

        final TCheckBox showGui = new TCheckBox();
        showGui.getStyle().setParent(style);
        showGui.getStyle().dimensions.set(200, 20);
        showGui.getStyle().x.set(Proportion.of(.8f));
        showGui.getStyle().y.set(50);
        showGui.checkedProperty().addListener((o, old, value) -> {
            if (value) {
                scene.attachChild(container);
            } else {
                scene.detachChild(container);
            }
        });
        scene.attachChild(showGui);

        final TProgressBar contrastProgressBar = new TProgressBar();
        contrastProgressBar.getStyle().setParent(style);
        contrastProgressBar.getStyle().dimensions.set(200, 20);
        contrastProgressBar.getStyle().x.set(Proportion.of(.65f));
        contrastProgressBar.getStyle().y.set(150);
        final Contrast contrast = new Contrast(1.9f);
        final TToggleButton contrastButton = new TToggleButton();
        contrastButton.getStyle().setParent(style);
        contrastButton.getStyle().dimensions.set(200, 20);
        contrastButton.getStyle().x.set(Proportion.of(.8f));
        contrastButton.getStyle().y.set(150);
        contrastButton.valueProperty().addListener((o, old, value) -> {
            if (value) {
                renderer.getPostProcessing().add(contrast);
                container.getChildren().add(contrastProgressBar);
                contrastProgressBar.setProgressDriver(new LinearFloatDriver(0f, 100f, 2f));
            } else {
                renderer.getPostProcessing().remove(contrast);
                container.getChildren().remove(contrastProgressBar);
                contrastProgressBar.setProgressDriver(null);
                contrastProgressBar.setValue(1f);
            }
        });
        contrastProgressBar.progressProperty().addListener((o, old, value) -> {
            if (value == 100f) {
                container.getChildren().remove(contrastProgressBar);
                contrastProgressBar.setProgressDriver(null);
                contrastProgressBar.setValue(1f);
            }
            contrast.setFactor(value / 100f + 1f);
        });
        container.attachChild(contrastButton);


        final TProgressBar radialBlurProgressBar = new TProgressBar();
        radialBlurProgressBar.getStyle().setParent(style);
        radialBlurProgressBar.getStyle().dimensions.set(200, 20);
        radialBlurProgressBar.getStyle().x.set(Proportion.of(.65f));
        radialBlurProgressBar.getStyle().y.set(250);
        final RadialBlur radialBlur = new RadialBlur(10, 2f);
        final TToggleButton radialBlurButton = new TToggleButton();
        radialBlurButton.getStyle().setParent(style);
        radialBlurButton.getStyle().dimensions.set(200, 20);
        radialBlurButton.getStyle().x.set(Proportion.of(.8f));
        radialBlurButton.getStyle().y.set(250);
        radialBlurButton.valueProperty().addListener((o, old, value) -> {
            if (value) {
                renderer.getPostProcessing().add(radialBlur);
                container.getChildren().add(radialBlurProgressBar);
                radialBlurProgressBar.setProgressDriver(new LinearFloatDriver(0f, 100f, 2f));
            } else {
                renderer.getPostProcessing().remove(radialBlur);
                container.getChildren().remove(radialBlurProgressBar);
                radialBlurProgressBar.setProgressDriver(null);
                radialBlurProgressBar.setValue(1f);
            }
            radialBlur.setFactor(radialBlur.getFactor() + .1f);
        });
        radialBlurProgressBar.progressProperty().addListener((o, old, value) -> {
            if (value == 100f) {
                container.getChildren().remove(radialBlurProgressBar);
                radialBlurProgressBar.setProgressDriver(null);
                radialBlurProgressBar.setValue(1f);
            }
            radialBlur.setFactor(100f / value * 2f + 1f);
        });
        container.attachChild(radialBlurButton);

        final SkyBox skyBox = new SkyBox(CubeMapTexture.builder()
                .positiveX("/skyBoxes/space/right.png")
                .positiveY("/skyBoxes/space/top.png")
                .positiveZ("/skyBoxes/space/front.png")
                .negativeX("/skyBoxes/space/left.png")
                .negativeY("/skyBoxes/space/bottom.png")
                .negativeZ("/skyBoxes/space/back.png")
                .create()
        );
        skyBox.setSkyColour(Colour.TRANSPARENT);

        final TToggleButton skyBoxButton = new TToggleButton();
        skyBoxButton.getStyle().setParent(style);
        skyBoxButton.getStyle().dimensions.set(200, 20);
        skyBoxButton.getStyle().x.set(Proportion.of(.8f));
        skyBoxButton.getStyle().y.set(350);
        skyBoxButton.valueProperty().addListener((o, old, value) -> {
            if (value) {
                scene.attachChild(skyBox);
            } else {
                scene.detachChild(skyBox);
            }
        });
        container.attachChild(skyBoxButton);

        final TToggleButton lowPolyButton = new TToggleButton();
        lowPolyButton.getStyle().setParent(style);
        lowPolyButton.getStyle().dimensions.set(200, 20);
        lowPolyButton.getStyle().x.set(Proportion.of(.8f));
        lowPolyButton.getStyle().y.set(450);
        lowPolyButton.valueProperty().addListener(
                (o, old, value) -> shape.setLowPoly(value));
        container.attachChild(lowPolyButton);
        lowPolyButton.valueProperty().set(true);
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