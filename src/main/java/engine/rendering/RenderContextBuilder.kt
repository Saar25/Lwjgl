package engine.rendering

import engine.engineObjects.Fog
import engine.light.Light
import engine.shadows.Sun
import opengl.objects.ClipPlane

class RenderContextBuilder {

    var camera: Camera? = null
    var clipPlane: ClipPlane = ClipPlane.NONE
    var shadowDistance: Float = 0f
    var shadowBias: Float = 0f
    var shadowPower: Float = 0f
    var fog: Fog = Fog.NONE
    var sun: Sun? = null
    var outputData: RenderOutputData? = null
    val lights: List<Light> = mutableListOf()

    fun create(): RenderContext {
        // return RenderContext(camera, clipPlane, shadowDistance,
        //         shadowBias, shadowPower, fog, sun, outputData, lights)
        val context = RenderContext()
        context.camera = camera
        context.clipPlane = clipPlane
        context.shadowDistance = shadowDistance
        context.shadowBias = shadowBias
        context.shadowPower = shadowPower
        context.fog = fog
        context.sun = sun
        context.outputData = outputData
        context.lights = lights
        return context
    }

}