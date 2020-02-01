package engine.water.flat

import engine.light.Light
import engine.light.UniformLightProperty
import engine.rendering.RenderContext
import engine.rendering.Renderer3D
import engine.water.WaterTileRenderer
import opengl.shaders.*
import opengl.textures.ITexture
import opengl.utils.GlUtils

class FlatWaterRenderer @Throws(Exception::class) private constructor() : Renderer3D<FlatWater>() {

    val shadersProgram: ShadersProgram<FlatWater>

    fun Boolean.toInt() = if (this) 1 else 0

    init {
        shadersProgram = ShadersProgram.create(Shader.createVertex(VERTEX_FILE), Shader.createFragment(FRAGMENT_FILE))

        shadersProgram.addPerRenderUniform(object : UniformValueProperty<FlatWater>("projectionViewMatrix") {
            override fun getUniformValue(state: RenderState<FlatWater>): UniformValue {
                return state.camera.projectionViewMatrix
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformValueProperty<FlatWater>("in_cameraPosition") {
            override fun getUniformValue(state: RenderState<FlatWater>): UniformValue {
                return state.camera.transform.position
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformValueProperty<FlatWater>("clipPlane") {
            override fun getUniformValue(state: RenderState<FlatWater>): UniformValue {
                return getContext().clipPlane.value
            }
        })
        shadersProgram.addPerRenderUniform(UniformArrayProperty("lights", 10, { getContext().lights.size }) { name, index ->
            object : UniformLightProperty<FlatWater>(name) {
                override fun getUniformValue(state: RenderState<FlatWater>): Light {
                    return getContext().lights[index]
                }
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformIntProperty<FlatWater>("lightsCount") {
            override fun getUniformValue(state: RenderState<FlatWater>): Int {
                return getContext().lights.size
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<FlatWater>("nearPlane") {
            override fun getUniformValue(state: RenderState<FlatWater>): Float {
                return state.camera.nearPlane
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<FlatWater>("farPlane") {
            override fun getUniformValue(state: RenderState<FlatWater>): Float {
                return state.camera.farPlane
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformValueProperty<FlatWater>("transformationMatrix") {
            override fun getUniformValue(state: RenderState<FlatWater>): UniformValue {
                return state.instance.transform.transformationMatrix
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformFloatProperty<FlatWater>("distortionOffset") {
            override fun getUniformValue(state: RenderState<FlatWater>): Float {
                return state.instance.distortionOffset
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformIntProperty<FlatWater>("availableTextures") {
            override fun getUniformValue(state: RenderState<FlatWater>): Int {
                var bits = 0
                bits = (bits shl 1) + (state.instance.dudvMap != null).toInt()
                bits = (bits shl 1) + (state.instance.normalsMap != null).toInt()
                bits = (bits shl 1) + (WaterTileRenderer.instance.depthTexture != null).toInt()
                bits = (bits shl 1) + (WaterTileRenderer.instance.refractionTexture != null).toInt()
                bits = (bits shl 1) + (WaterTileRenderer.instance.reflectionTexture != null).toInt()
                return bits
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<FlatWater>("reflectionTexture", 0) {
            override fun getUniformValue(state: RenderState<FlatWater>): ITexture? {
                return WaterTileRenderer.instance.reflectionTexture
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<FlatWater>("refractionTexture", 1) {
            override fun getUniformValue(state: RenderState<FlatWater>): ITexture? {
                return WaterTileRenderer.instance.refractionTexture
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<FlatWater>("depthTexture", 2) {
            override fun getUniformValue(state: RenderState<FlatWater>): ITexture? {
                return WaterTileRenderer.instance.depthTexture
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformTextureProperty<FlatWater>("normalsMap", 3) {
            override fun getUniformValue(state: RenderState<FlatWater>): ITexture? {
                return state.instance.normalsMap
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformTextureProperty<FlatWater>("dudvMap", 4) {
            override fun getUniformValue(state: RenderState<FlatWater>): ITexture? {
                return state.instance.dudvMap
            }
        })

        shadersProgram.bindAttribute(0, "in_position")
    }

    override fun render(context: RenderContext) {
        shadersProgram.bind()

        GlUtils.disableCulling()
        GlUtils.enableDepthTest()
        GlUtils.enableAlphaBlending()

        val renderState = RenderState<FlatWater>(this, context.camera)
        shadersProgram.updatePerRenderUniforms(renderState)

        for (flatWater in renderList) {
            val instanceState = RenderState<FlatWater>(this, flatWater, context.camera)
            shadersProgram.updatePerInstanceUniforms(instanceState)

            flatWater.model.render()
        }

        shadersProgram.unbind()
    }

    override fun cleanUp() {
        shadersProgram.delete()
    }

    companion object {
        private const val VERTEX_FILE = "/engine/water/flat/flatWaterVertex.glsl"
        private const val FRAGMENT_FILE = "/engine/water/flat/flatWaterFragment.glsl"

        @JvmStatic
        val instance: FlatWaterRenderer = FlatWaterRenderer()
    }
}
