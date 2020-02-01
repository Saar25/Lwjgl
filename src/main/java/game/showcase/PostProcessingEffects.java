package game.showcase;

import engine.effects.WaterReflection;
import engine.effects.WaterRefraction;
import engine.engineObjects.Fog;
import engine.gameengine.GameEngine;
import engine.gameengine.SimpleApplication;
import engine.light.DirectionalLight;
import engine.models.Skin;
import engine.postprocessing.PostProcessor;
import engine.postprocessing.bloom.Bloom;
import engine.postprocessing.contrast.Contrast;
import engine.postprocessing.custom.Custom;
import engine.postprocessing.deferredfog.DeferredFog;
import engine.postprocessing.radialblur.RadialBlur;
import engine.rendering.RenderManager;
import engine.rendering.camera.CameraControllers;
import engine.rendering.camera.CameraKeyboardMovement;
import engine.rendering.camera.CameraLookAtRotation;
import engine.skybox.SkyBox;
import engine.terrain.Terrain;
import engine.terrain.texture.TextureTerrain;
import engine.util.node.Group;
import engine.water.WaterTile;
import glfw.input.Keyboard;
import glfw.input.Mouse;
import glfw.window.Window;
import maths.utils.Vector3;
import opengl.textures.CubeMapTexture;
import tegui.style.property.Colour;

public class PostProcessingEffects extends SimpleApplication {

    public static void main(String[] args) {
        try {
            SimpleApplication game = new PostProcessingEffects();
            GameEngine gameEngine = new GameEngine("PostProcessingEffects", 1200, 741, game);
            gameEngine.init();
            gameEngine.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInit(Window window, RenderManager renderer, Keyboard keyboard, Mouse mouse) throws Exception {
        // Initialize the game

        final Bool isContrast = new Bool();
        final PostProcessor contrast = new Contrast(1.4f);
        keyboard.onKeyPress('1').perform(e -> {
            if (isContrast.flip()) {
                renderer.getPostProcessing().add(contrast);
            } else {
                renderer.getPostProcessing().remove(contrast);
            }
        });


        final Bool isBloom = new Bool();
        final PostProcessor bloom = new Bloom(.2f, .7f, .7f);
        keyboard.onKeyPress('2').perform(e -> {
            if (isBloom.flip()) {
                renderer.getPostProcessing().add(bloom);
            } else {
                renderer.getPostProcessing().remove(bloom);
            }
        });

        final Bool isFog = new Bool();
        final Fog f = new Fog(2000, 2300, Vector3.of(0));
        final PostProcessor fog = new DeferredFog(f, camera);
        keyboard.onKeyPress('3').perform(e -> {
            if (isFog.flip()) {
                renderer.getPostProcessing().add(fog);
            } else {
                renderer.getPostProcessing().remove(fog);
            }
        });

        final Bool isRadialBlur = new Bool();
        final PostProcessor radialBlur = new RadialBlur(10, 2f);
        keyboard.onKeyPress('4').perform(e -> {
            if (isRadialBlur.flip()) {
                renderer.getPostProcessing().add(radialBlur);
            } else {
                renderer.getPostProcessing().remove(radialBlur);
            }
        });

        final Bool isCustom = new Bool();
        final PostProcessor custom = new Custom();
        keyboard.onKeyPress('5').perform(e -> {
            if (isCustom.flip()) {
                renderer.getPostProcessing().add(custom);
            } else {
                renderer.getPostProcessing().remove(custom);
            }
        });

        keyboard.onKeyPress('T').perform(e -> mouse.setShown(!mouse.isShown()));

        final DirectionalLight sun = new DirectionalLight(Vector3.of(7, 2, 5), Vector3.of(1), .7f);
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
        scene.attachChild(skyBox);

        final Skin skin = loader.loadSkin("/terrains/sand.png");
        final Terrain terrain = new TextureTerrain(Vector3.of(0, 0, 0), 1600, skin);
        scene.attachChild(terrain);

        WaterTile.defaultDudvMap = loader.loadTexture("/maps/waterDudvMap.png");
        WaterTile.defaultNormalsMap = loader.loadTexture("/maps/waterNormalMap.png");
        scene.attachChild(new WaterTile(terrain));

        final Group waterEffects = new Group();
        waterEffects.attachChild(sun);
        waterEffects.attachChild(terrain);
        waterEffects.attachChild(skyBox);
        scene.addEffect(new WaterRefraction(waterEffects));
        scene.addEffect(new WaterReflection(waterEffects));

        camera.setController(new CameraControllers(
                new CameraKeyboardMovement(keyboard),
                new CameraLookAtRotation(terrain)));
        camera.getTransform().setPosition(-700, 300, -700);
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

    private static class Bool {
        private boolean value = false;

        private boolean flip() {
            return this.value = !value;
        }
    }

}