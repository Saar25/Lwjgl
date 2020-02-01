package game.showcase;

import engine.gameengine.GameEngine;
import engine.gameengine.SimpleApplication;
import engine.light.DirectionalLight;
import engine.rendering.RenderManager;
import engine.rendering.camera.FlyCamera;
import engine.skybox.SkyBox;
import engine.terrain.lowpoly.LowPolyPlanet;
import glfw.input.Keyboard;
import glfw.input.Mouse;
import glfw.window.Window;
import maths.utils.Vector3;
import opengl.textures.CubeMapTexture;
import opengl.utils.GlUtils;
import tegui.style.property.Colour;

public class PlanetShowcase extends SimpleApplication {

    public static void main(String[] args) {
        try {
            SimpleApplication game = new PlanetShowcase();
            GameEngine gameEngine = new GameEngine("PlanetShowcase", 1200, 741, game);
            gameEngine.init();
            gameEngine.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInit(Window window, RenderManager renderer, Keyboard keyboard, Mouse mouse) throws Exception {
        final DirectionalLight sun = new DirectionalLight();
        sun.setDirection(Vector3.of(2, 7, 5));
        sun.setColour(Vector3.of(1, 1, 1));
        sun.setIntensity(1);
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

        for (int i = 0; i < 50; i++) {
            final LowPolyPlanet planet = new LowPolyPlanet(64, 100);
            planet.getTransform().getPosition().x = (float) (Math.random() * 2000 - 1000);
            planet.getTransform().getPosition().y = (float) (Math.random() * 2000 - 1000);
            planet.getTransform().getPosition().z = (float) (Math.random() * 2000 - 1000);
            scene.attachChild(planet);
        }

        camera.setController(new FlyCamera(keyboard, mouse));
        camera.getTransform().setPosition(0, 100, 0);
    }

    @Override
    public void onUpdate(Keyboard keyboard, Mouse mouse) {
        // Update game, get the needed input as well

        scene.update();

        if (keyboard.isKeyPressed('E')) {
            GlUtils.drawPolygonLine();
        } else {
            GlUtils.drawPolygonFill();
        }
    }

    @Override
    public void onRender(RenderManager renderer) {
        // Render the scene

        renderer.render(scene);
    }

}