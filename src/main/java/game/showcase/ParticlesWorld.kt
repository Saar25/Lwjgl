package game.showcase

import engine.components.*
import engine.components.animation.SimpleAnimationComponent
import engine.components.physics.GravityComponent
import engine.components.physics.TerrainCollisionComponent
import engine.effects.Shadows
import engine.effects.WaterReflection
import engine.effects.WaterRefraction
import engine.engineObjects.Fog
import engine.entity.Entity
import engine.gameengine.GameEngine
import engine.gameengine.SimpleApplication
import engine.light.DirectionalLight
import engine.models.Skin
import engine.particles.ParticleSystem
import engine.particles.placers.SphereParticlePlacer
import engine.postprocessing.deferredfog.DeferredFog
import engine.rendering.RenderManager
import engine.rendering.background.BackgroundColour
import engine.rendering.camera.ThirdPersonCamera
import engine.terrain.TerrainVertex
import engine.terrain.generation.NormalColourGenerator
import engine.terrain.generation.PerlinNoise
import engine.terrain.lowpoly.LPTerrainConfigs
import engine.terrain.lowpoly.LowPolyTerrain
import engine.util.node.Group
import engine.water.lowpoly.LowpolyWavyWater
import glfw.input.Keyboard
import glfw.input.Mouse
import glfw.window.Window
import maths.utils.Vector3
import tegui.style.property.Colour
import java.util.*

class ParticlesWorld : SimpleApplication() {

    override fun onInit(window: Window, renderer: RenderManager, keyboard: Keyboard, mouse: Mouse) {
        // Initialize the game

        val fog = Fog(1500f, 2000f, Colour.CYAN.rgbVector())
        renderer.postProcessing.add(DeferredFog(fog, camera))

        keyboard.onKeyPress('T').perform { mouse.isShown = !mouse.isShown }

        renderer.setBackground(BackgroundColour(0.5f, 1.0f, 1.0f))

        val sun = DirectionalLight()
        sun.direction = Vector3.of(-7f, 5f, -3f)
        sun.setColour(Vector3.of(1f, 1f, 1f))
        sun.intensity = 1f
        scene.attachChild(sun)

        val heightGenerator = PerlinNoise(Random().nextLong(), .03f, 800f, 3, 50f)
        val normalColourGenerator = NormalColourGenerator(Vector3.upward())
                .withColour(0.8f, Vector3.of(.41f, .41f, .41f))
                .withColour(1.0f, Vector3.of(0.5f, 0.6f, 0.0f))
        val sandColour = Vector3.of(.76f, .69f, .50f)
        val colourGenerator = { vertex: TerrainVertex ->
            if (vertex.position.y < 20) sandColour
            else normalColourGenerator.generateColour(vertex)
        }

        val terrain = LowPolyTerrain(LPTerrainConfigs()
                .setHeightGenerator(heightGenerator)
                .setColourGenerator(colourGenerator)
                .setPosition(Vector3.of(0f, 0f, 0f))
                .setSize(4000f).setVertices(32 * 8))
        scene.attachChild(terrain)

        val water = LowpolyWavyWater(terrain, 256)
        scene.attachChild(water)

        val skin = Skin.of("/textures/player.png")
        val model = loader.loadModel("/objModels/player.obj")
        val player = Entity(model, skin)
        player.transform.scale.set(3f)
        player.components.add(MovementComponent())
        player.components.add(ActiveMovementComponent(60f, 40f, 60f, 10f))
        player.components.add(SimpleAnimationComponent())
        player.components.add(KeyboardInputComponent(keyboard))
        player.components.add(TerrainCollisionComponent(terrain))
        player.components.add(BackFaceComponent(camera.transform))
        player.components.add(GravityComponent())
        player.components.add(FloatingComponent(0f))
        scene.attachChild(player)

        camera.setController(ThirdPersonCamera(mouse, player, Vector3.of(0f, 5f, 0f)))

        val waterEffects = Group()
        waterEffects.attachChild(terrain)
        waterEffects.attachChild(player)
        waterEffects.attachChild(sun)
        scene.addEffect(WaterReflection(waterEffects))
        scene.addEffect(WaterRefraction(waterEffects))

        val shadowEffects = Group()
        shadowEffects.attachChild(player)
        shadowEffects.attachChild(terrain)
        scene.addEffect(Shadows(shadowEffects, sun))

        val particlesSkin = loader.loadSkin("/textures/particleFire.png").setTextureRows(4)
        keyboard.onKeyPress('E').perform {
            val particleSystem = ParticleSystem(SphereParticlePlacer(10f), particlesSkin)
            particleSystem.transform.setPosition(player.transform.position)
            particleSystem.transform.scale.set(2f)
            particleSystem.maxParticles = 3000
            particleSystem.maxPerUpdate = 10
            scene.attachChild(particleSystem)
            waterEffects.children.add(particleSystem)
        }
    }

    override fun onUpdate(keyboard: Keyboard, mouse: Mouse) {
        // Update game, get the needed input as well

        scene.update()
    }

    override fun onRender(renderer: RenderManager) {
        // Render the scene

        renderer.render(scene)
    }

}

fun main() {
    val gameEngine = GameEngine("ParticlesWorld", 1200, 741, ParticlesWorld())
    gameEngine.init()
    gameEngine.start()
}