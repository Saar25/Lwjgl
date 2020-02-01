package engine.water.simple

import engine.light.Light
import engine.light.UniformLightProperty
import engine.rendering.RenderContext
import engine.rendering.Renderer3D
import engine.water.WaterTileRenderer
import maths.joml.Vector3f
import opengl.shaders.*
import opengl.textures.ITexture
import opengl.utils.GlUtils

class SimpleWaterRenderer @Throws(Exception::class) private constructor() : Renderer3D<SimpleWater>() {

    val shadersProgram: ShadersProgram<SimpleWater>

    fun Boolean.toInt() = if (this) 1 else 0

    init {
        shadersProgram = ShadersProgram.create(Shader.createVertex(VERTEX_FILE), Shader.createFragment(FRAGMENT_FILE))

        shadersProgram.addPerRenderUniform(object : UniformValueProperty<SimpleWater>("projectionViewMatrix") {
            override fun getUniformValue(state: RenderState<SimpleWater>): UniformValue {
                return state.camera.projectionViewMatrix
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformValueProperty<SimpleWater>("in_cameraPosition") {
            override fun getUniformValue(state: RenderState<SimpleWater>): UniformValue {
                return state.camera.transform.position
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformValueProperty<SimpleWater>("clipPlane") {
            override fun getUniformValue(state: RenderState<SimpleWater>): UniformValue {
                return getContext().clipPlane.value
            }
        })
        shadersProgram.addPerRenderUniform(UniformArrayProperty("lights", 10, { getContext().lights.size }) { name, index ->
            object : UniformLightProperty<SimpleWater>(name) {
                override fun getUniformValue(state: RenderState<SimpleWater>): Light {
                    return getContext().lights[index]
                }
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformIntProperty<SimpleWater>("lightsCount") {
            override fun getUniformValue(state: RenderState<SimpleWater>): Int {
                return getContext().lights.size
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<SimpleWater>("nearPlane") {
            override fun getUniformValue(state: RenderState<SimpleWater>): Float {
                return state.camera.nearPlane
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<SimpleWater>("farPlane") {
            override fun getUniformValue(state: RenderState<SimpleWater>): Float {
                return state.camera.farPlane
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformIntProperty<SimpleWater>("availableTextures") {
            override fun getUniformValue(state: RenderState<SimpleWater>): Int {
                var bits = 0
                bits = (bits shl 1) + (WaterTileRenderer.instance.depthTexture != null).toInt()
                bits = (bits shl 1) + (WaterTileRenderer.instance.refractionTexture != null).toInt()
                bits = (bits shl 1) + (WaterTileRenderer.instance.reflectionTexture != null).toInt()
                return bits
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformValueProperty<SimpleWater>("transformationMatrix") {
            override fun getUniformValue(state: RenderState<SimpleWater>): UniformValue {
                return state.instance.transform.transformationMatrix
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformValueProperty<SimpleWater>("waterColour") {
            override fun getUniformValue(state: RenderState<SimpleWater>): Vector3f {
                return state.instance.colour
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<SimpleWater>("reflectionTexture", 0) {
            override fun getUniformValue(state: RenderState<SimpleWater>): ITexture? {
                return WaterTileRenderer.instance.reflectionTexture
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<SimpleWater>("refractionTexture", 1) {
            override fun getUniformValue(state: RenderState<SimpleWater>): ITexture? {
                return WaterTileRenderer.instance.refractionTexture
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<SimpleWater>("depthTexture", 2) {
            override fun getUniformValue(state: RenderState<SimpleWater>): ITexture? {
                return WaterTileRenderer.instance.depthTexture
            }
        })

        shadersProgram.bindAttribute(0, "in_position")
    }

    override fun render(context: RenderContext) {
        shadersProgram.bind()

        GlUtils.disableCulling()
        GlUtils.enableDepthTest()
        GlUtils.enableAlphaBlending()

        val renderState = RenderState<SimpleWater>(this, context.camera)
        shadersProgram.updatePerRenderUniforms(renderState)

        for (flatWater in renderList) {
            val instanceState = RenderState<SimpleWater>(this, flatWater, context.camera)
            shadersProgram.updatePerInstanceUniforms(instanceState)

            flatWater.model.render()
        }

        shadersProgram.unbind()
    }

    override fun cleanUp() {
        shadersProgram.delete()
    }

    companion object {
        private const val VERTEX_FILE = "/engine/water/simple/simpleWaterVertex.glsl"
        private const val FRAGMENT_FILE = "/engine/water/simple/simpleWaterFragment.glsl"

        @JvmStatic
        val instance: SimpleWaterRenderer = SimpleWaterRenderer()
    }
}
