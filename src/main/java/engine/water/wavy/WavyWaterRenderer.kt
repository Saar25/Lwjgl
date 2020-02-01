package engine.water.wavy

import engine.light.Light
import engine.light.UniformLightProperty
import engine.rendering.RenderContext
import engine.rendering.Renderer3D
import engine.water.WaterTileRenderer
import opengl.shaders.*
import opengl.textures.ITexture
import opengl.utils.GlUtils

class WavyWaterRenderer @Throws(Exception::class) private constructor() : Renderer3D<WavyWater>() {

    private val shadersProgram: ShadersProgram<WavyWater>

    private fun Boolean.toInt() = if (this) 1 else 0

    init {
        this.shadersProgram = ShadersProgram.create(Shader.createVertex(VERTEX_FILE), Shader.createFragment(FRAGMENT_FILE))

        shadersProgram.addPerRenderUniform(object : UniformValueProperty<WavyWater>("projectionViewMatrix") {
            override fun getUniformValue(state: RenderState<WavyWater>): UniformValue {
                return state.camera.projectionViewMatrix
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformValueProperty<WavyWater>("in_cameraPosition") {
            override fun getUniformValue(state: RenderState<WavyWater>): UniformValue {
                return state.camera.transform.position
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformValueProperty<WavyWater>("clipPlane") {
            override fun getUniformValue(state: RenderState<WavyWater>): UniformValue {
                return getContext().clipPlane.value
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformValueProperty<WavyWater>("waterColour") {
            override fun getUniformValue(state: RenderState<WavyWater>): UniformValue {
                return state.instance.colour
            }
        })
        shadersProgram.addPerRenderUniform(UniformArrayProperty("lights", 10, { getContext().lights.size }) { name, index ->
            object : UniformLightProperty<WavyWater>(name) {
                override fun getUniformValue(state: RenderState<WavyWater>): Light {
                    return getContext().lights[index]
                }
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformIntProperty<WavyWater>("lightsCount") {
            override fun getUniformValue(state: RenderState<WavyWater>): Int {
                return getContext().lights.size
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<WavyWater>("nearPlane") {
            override fun getUniformValue(state: RenderState<WavyWater>): Float {
                return state.camera.nearPlane
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<WavyWater>("farPlane") {
            override fun getUniformValue(state: RenderState<WavyWater>): Float {
                return state.camera.farPlane
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformValueProperty<WavyWater>("transformationMatrix") {
            override fun getUniformValue(state: RenderState<WavyWater>): UniformValue {
                return state.instance.transform.transformationMatrix
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformFloatProperty<WavyWater>("amplitude") {
            override fun getUniformValue(state: RenderState<WavyWater>): Float {
                return state.instance.amplitude
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<WavyWater>("time") {
            override fun getUniformValue(state: RenderState<WavyWater>): Float {
                return WaterTileRenderer.instance.time
            }
        })

        shadersProgram.addPerInstanceUniform(object : UniformIntProperty<WavyWater>("availableTextures") {
            override fun getUniformValue(state: RenderState<WavyWater>): Int {
                var bits = 0
                bits = (bits shl 1) + (WaterTileRenderer.instance.depthTexture != null).toInt()
                bits = (bits shl 1) + (WaterTileRenderer.instance.refractionTexture != null).toInt()
                bits = (bits shl 1) + (WaterTileRenderer.instance.reflectionTexture != null).toInt()
                return bits
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<WavyWater>("reflectionTexture", 0) {
            override fun getUniformValue(state: RenderState<WavyWater>): ITexture? {
                return WaterTileRenderer.instance.reflectionTexture
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<WavyWater>("refractionTexture", 1) {
            override fun getUniformValue(state: RenderState<WavyWater>): ITexture? {
                return WaterTileRenderer.instance.refractionTexture
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<WavyWater>("depthTexture", 2) {
            override fun getUniformValue(state: RenderState<WavyWater>): ITexture? {
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

        val renderState = RenderState<WavyWater>(this, context.camera)
        shadersProgram.updatePerRenderUniforms(renderState)

        for (waterTile in renderList) {
            val instanceState = RenderState<WavyWater>(this, waterTile, context.camera)
            shadersProgram.updatePerInstanceUniforms(instanceState)

            waterTile.model.render()
        }

        shadersProgram.unbind()
    }

    override fun cleanUp() {
        WavyWater.deleteModel()
        shadersProgram.delete()
    }

    companion object {
        private const val VERTEX_FILE = "/engine/water/wavy/wavyWaterVertex.glsl"
        private const val FRAGMENT_FILE = "/engine/water/wavy/wavyWaterFragment.glsl"

        @JvmStatic
        val instance: WavyWaterRenderer = WavyWaterRenderer()
    }
}
