package game.showcase;

import engine.components.MovementComponent;
import engine.components.particles.ExplosionComponent;
import engine.components.physics.GravityComponent;
import engine.gameengine.SimpleApplication;
import engine.gameengine.GameEngine;
import engine.models.Skin;
import engine.particles.ParticleSystem;
import engine.particles.placers.CircleParticlePlacer;
import engine.particles.placers.EmitterParticlePlacer;
import engine.particles.placers.LineParticlePlacer;
import engine.rendering.RenderManager;
import glfw.input.Keyboard;
import glfw.input.Mouse;
import glfw.window.Window;
import maths.utils.Vector3;

public class Particles3 extends SimpleApplication {

    public static void main(String[] args) {
        try {
            SimpleApplication game = new Particles3();
            GameEngine gameEngine = new GameEngine("Particles3", 1200, 741, game);
            gameEngine.init();
            gameEngine.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInit(Window window, RenderManager renderer, Keyboard keyboard, Mouse mouse) throws Exception {
        final Skin skin1 = loader.loadSkin("/textures/particles.png").setTextureRows(4);
        final Skin skin2 = loader.loadSkin("/textures/particleFire.png").setTextureRows(4);
        final Skin skin3 = loader.loadSkin("/textures/particleStar.png");

        final ParticleSystem particleSystem1 = new ParticleSystem(
                new CircleParticlePlacer(Vector3.of(0, 0, 0), 10), skin1);
        particleSystem1.getComponents().add(() -> new MovementComponent());
        particleSystem1.getComponents().add(() -> new GravityComponent());
        particleSystem1.setMaxPerUpdate(10);
        particleSystem1.setMaxParticles(20000);
        //scene.attachChild(particleSystem1);

        final ParticleSystem particleSystem2 = new ParticleSystem(
                new EmitterParticlePlacer(Vector3.of(0, 0, 0)), skin2);
        particleSystem2.getComponents().add(() -> new GravityComponent());
        particleSystem2.getComponents().add(() -> new MovementComponent());
        particleSystem2.getComponents().add(() -> new ExplosionComponent(70, 10));
        particleSystem2.setMaxPerUpdate(10);
        particleSystem2.setMaxParticles(10000);
        particleSystem2.setMaxAge(3);
        //scene.attachChild(particleSystem2);

        final ParticleSystem particleSystem3 = new ParticleSystem(
                new LineParticlePlacer(Vector3.of(10, 0, 0)), skin3);
        particleSystem3.getComponents().add(() -> new MovementComponent());
        particleSystem3.getComponents().add(() -> new GravityComponent());
        particleSystem3.setMaxPerUpdate(10);
        particleSystem3.setMaxParticles(10000);
        scene.attachChild(particleSystem3);

        camera.getTransform().setPosition(0, 10, -50);
        camera.getTransform().lookAt(Vector3.of(0, -25, 0));
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