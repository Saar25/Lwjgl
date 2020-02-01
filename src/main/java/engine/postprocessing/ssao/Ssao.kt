package engine.postprocessing.ssao

import engine.postprocessing.AbstractPostProcessor
import engine.postprocessing.PostProcessor
import engine.rendering.Camera
import engine.rendering.RenderOutputData
import glfw.window.Window
import maths.joml.Vector2i
import maths.joml.Vector3f
import maths.utils.Vector3
import opengl.constants.DataType
import opengl.constants.FormatType
import opengl.shaders.*
import opengl.textures.ITexture
import opengl.textures.Texture
import opengl.textures.TextureTarget
import opengl.textures.parameters.MagFilterParameter
import opengl.textures.parameters.MinFilterParameter
import opengl.textures.parameters.WrapParameter
import opengl.utils.MemoryUtils
import java.nio.FloatBuffer
import kotlin.random.Random

class Ssao(val camera: Camera) : AbstractPostProcessor(FRAG_FILE), PostProcessor {

    init {
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<RenderOutputData>("albedo", 0) {
            override fun getUniformValue(state: RenderState<RenderOutputData>): ITexture {
                return state.instance.colour
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<RenderOutputData>("normal", 1) {
            override fun getUniformValue(state: RenderState<RenderOutputData>): ITexture {
                return state.instance.normal
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<RenderOutputData>("depth", 2) {
            override fun getUniformValue(state: RenderState<RenderOutputData>): ITexture {
                return state.instance.depth
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<RenderOutputData>("noise", 3) {
            override fun getUniformValue(state: RenderState<RenderOutputData>): ITexture {
                return noiseTexture
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformValueProperty<RenderOutputData>("dimensions") {
            override fun getUniformValue(state: RenderState<RenderOutputData>): UniformValue {
                return Vector2i(Window.current().width, Window.current().height)
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformValueProperty<RenderOutputData>("projection") {
            override fun getUniformValue(state: RenderState<RenderOutputData>): UniformValue {
                return camera.projectionMatrix
            }
        })

        UniformArrayProperty<RenderOutputData>("samples", 64) { name, index ->
            object : UniformValueProperty<RenderOutputData>(name) {
                override fun getUniformValue(state: RenderState<RenderOutputData>?): UniformValue {
                    return kernelArray[index]
                }
            }
        }.load(null)
    }

    companion object {

        private const val FRAG_FILE = "/engine/postprocessing/ssao/SsaoFragment.glsl"

        private const val noiseSize: Int = 4

        private val noiseTexture: Texture = Texture.create()

        private val kernelArray = Array<Vector3f>(64) { i -> Vector3.of(i.toFloat()) }

        private fun lerp(a: Float, b: Float, f: Float): Float {
            return a + f * (b - a)
        }

        init {
            // Kernel array
            for (i in kernelArray.indices) {
                val sample: Vector3f = kernelArray[i]
                sample.x = Random.nextFloat() * 2.0f - 1.0f
                sample.y = Random.nextFloat() * 2.0f - 1.0f
                sample.z = Random.nextFloat()
                val scale = lerp(0.1f, 1.0f, i / 64.0f)
                sample.normalize(Random.nextFloat() * scale)
            }

            // Noise texture
            val buffer: FloatBuffer = MemoryUtils.allocFloat(noiseSize * noiseSize * 3)
            for (i in 1..noiseSize * noiseSize) {
                buffer.put(Random.nextFloat() * 2.0f - 1.0f)
                buffer.put(Random.nextFloat() * 2.0f - 1.0f)
                buffer.put(0.0f)
            }

            noiseTexture.allocate(TextureTarget.TEXTURE_2D, 0, FormatType.RGB16F,
                    noiseSize, noiseSize, 0, FormatType.RGB, DataType.FLOAT, buffer)
            noiseTexture.functions.minFilter(MinFilterParameter.NEAREST)
            noiseTexture.functions.magFilter(MagFilterParameter.NEAREST)
            noiseTexture.functions.wrapS(WrapParameter.REPEAT)
            noiseTexture.functions.wrapT(WrapParameter.REPEAT)
        }

    }

}
