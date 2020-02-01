package engine.postprocessing.sunshafts

import engine.postprocessing.ComposedPostProcessor
import engine.rendering.RenderOutputData
import engine.postprocessing.PostProcessor
import engine.postprocessing.combine.Combine
import engine.postprocessing.radialblur.RadialBlur
import engine.rendering.Camera
import maths.joml.Vector2f
import maths.joml.Vector3f
import maths.utils.Vector2
import maths.utils.Vector4
import engine.util.lengths.Proportion

class SunShafts(private val camera: Camera, private val sunPosition: Vector3f)
    : ComposedPostProcessor(Sun(), RadialBlur(100), Combine()), PostProcessor {

    val sun: Sun = getPostProcessor(Sun::class.java)
    private val radialBlur = getPostProcessor(RadialBlur::class.java)
    private val combine = getPostProcessor(Combine::class.java)

    override fun resize(width: Int, height: Int) {
        sun.resize(width, height)
        radialBlur.resize(width / 2, height / 2)
        combine.resize(width, height)
    }

    private fun calculateCenter(): Vector2f {
        val center = Vector2.create()
        val coords = Vector4.create()
        coords.set(sunPosition, 0f).mul(camera.projectionViewMatrix)
        center.set(coords.x, coords.y).mul(1 / coords.w)
        return center.mul(.5f).add(.5f, .5f)
    }

    override fun beforeProcess(renderOutputData: RenderOutputData) {
        val center = calculateCenter()
        combine.combination = renderOutputData.colour
        radialBlur.x = Proportion.of(center.x)
        radialBlur.y = Proportion.of(center.y)
        sun.center.set(center)
    }

}