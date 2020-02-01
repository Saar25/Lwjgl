package game.showcase;

import engine.components.MovementComponent;
import engine.components.particles.ExpandComponent;
import engine.gameengine.SimpleApplication;
import engine.gameengine.GameEngine;
import engine.models.Skin;
import engine.particles.ParticleSystem;
import engine.particles.placers.SphereParticlePlacer;
import engine.rendering.RenderManager;
import engine.rendering.camera.CameraControllers;
import engine.rendering.camera.CameraKeyboardMovement;
import engine.rendering.camera.CameraLookAtRotation;
import engine.rendering.camera.ThirdPersonCamera;
import glfw.input.Keyboard;
import glfw.input.Mouse;
import glfw.window.Window;

public class Particles extends SimpleApplication {

    public static void main(String[] args) {
        try {
            SimpleApplication game = new Particles();
            GameEngine gameEngine = new GameEngine("Particles", 1200, 741, game);
            gameEngine.init();
            gameEngine.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInit(Window window, RenderManager renderer, Keyboard keyboard, Mouse mouse) throws Exception {
        final Skin skin = loader.loadSkin("/textures/particleFire.png").setTextureRows(4);
        final ParticleSystem particleSystem = new ParticleSystem(new SphereParticlePlacer(50), skin);
        particleSystem.getComponents().add(() -> new MovementComponent());
        particleSystem.getComponents().add(() -> new ExpandComponent(4));
        particleSystem.getTransform().setScale(2);
        particleSystem.setMaxParticles(50000);
        particleSystem.setMaxPerUpdate(50);
        particleSystem.getTransform().setPosition(0, 0, 0);
        scene.attachChild(particleSystem);

        camera.getTransform().setPosition(20, 20, 20);
        final CameraKeyboardMovement movement = new CameraKeyboardMovement(keyboard);
        final CameraLookAtRotation rotation = new CameraLookAtRotation(particleSystem);
        camera.setController(new CameraControllers(movement, rotation));
        movement.setSpeed(.2f);

        camera.setController(new ThirdPersonCamera(mouse, particleSystem));
    }

    @Override
    public void onUpdate(Keyboard keyboard, Mouse mouse) {
        scene.update();
    }

    @Override
    public void onRender(RenderManager renderer) {
        renderer.render(scene);
    }

}