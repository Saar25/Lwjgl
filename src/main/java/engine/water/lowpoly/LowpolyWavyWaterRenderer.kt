package engine.water.lowpoly

import engine.light.Light
import engine.light.UniformLightProperty
import engine.rendering.RenderContext
import engine.rendering.Renderer3D
import engine.water.WaterTileRenderer
import opengl.shaders.*
import opengl.textures.ITexture
import opengl.utils.GlUtils

class LowpolyWavyWaterRenderer @Throws(Exception::class) private constructor() : Renderer3D<LowpolyWavyWater>() {

    private val shadersProgram: ShadersProgram<LowpolyWavyWater>

    private fun Boolean.toInt() = if (this) 1 else 0

    init {
        this.shadersProgram = ShadersProgram.create(Shader.createVertex(VERTEX_FILE), Shader.createFragment(FRAGMENT_FILE))

        shadersProgram.addPerRenderUniform(object : UniformValueProperty<LowpolyWavyWater>("projectionViewMatrix") {
            override fun getUniformValue(state: RenderState<LowpolyWavyWater>): UniformValue {
                return state.camera.projectionViewMatrix
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformValueProperty<LowpolyWavyWater>("in_cameraPosition") {
            override fun getUniformValue(state: RenderState<LowpolyWavyWater>): UniformValue {
                return state.camera.transform.position
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformValueProperty<LowpolyWavyWater>("clipPlane") {
            override fun getUniformValue(state: RenderState<LowpolyWavyWater>): UniformValue {
                return getContext().clipPlane.value
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformValueProperty<LowpolyWavyWater>("waterColour") {
            override fun getUniformValue(state: RenderState<LowpolyWavyWater>): UniformValue {
                return state.instance.colour
            }
        })
        shadersProgram.addPerRenderUniform(UniformArrayProperty("lights", 10, { getContext().lights.size }) { name, index ->
            object : UniformLightProperty<LowpolyWavyWater>(name) {
                override fun getUniformValue(state: RenderState<LowpolyWavyWater>): Light {
                    return getContext().lights[index]
                }
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformIntProperty<LowpolyWavyWater>("lightsCount") {
            override fun getUniformValue(state: RenderState<LowpolyWavyWater>): Int {
                return getContext().lights.size
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<LowpolyWavyWater>("nearPlane") {
            override fun getUniformValue(state: RenderState<LowpolyWavyWater>): Float {
                return state.camera.nearPlane
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<LowpolyWavyWater>("farPlane") {
            override fun getUniformValue(state: RenderState<LowpolyWavyWater>): Float {
                return state.camera.farPlane
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformValueProperty<LowpolyWavyWater>("transformationMatrix") {
            override fun getUniformValue(state: RenderState<LowpolyWavyWater>): UniformValue {
                return state.instance.transform.transformationMatrix
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformFloatProperty<LowpolyWavyWater>("amplitude") {
            override fun getUniformValue(state: RenderState<LowpolyWavyWater>): Float {
                return state.instance.amplitude
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<LowpolyWavyWater>("time") {
            override fun getUniformValue(state: RenderState<LowpolyWavyWater>): Float {
                return WaterTileRenderer.instance.time
            }
        })

        shadersProgram.addPerInstanceUniform(object : UniformIntProperty<LowpolyWavyWater>("availableTextures") {
            override fun getUniformValue(state: RenderState<LowpolyWavyWater>): Int {
                var bits = 0
                bits = (bits shl 1) + (WaterTileRenderer.instance.depthTexture != null).toInt()
                bits = (bits shl 1) + (WaterTileRenderer.instance.refractionTexture != null).toInt()
                bits = (bits shl 1) + (WaterTileRenderer.instance.reflectionTexture != null).toInt()
                return bits
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<LowpolyWavyWater>("reflectionTexture", 0) {
            override fun getUniformValue(state: RenderState<LowpolyWavyWater>): ITexture? {
                return WaterTileRenderer.instance.reflectionTexture
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<LowpolyWavyWater>("refractionTexture", 1) {
            override fun getUniformValue(state: RenderState<LowpolyWavyWater>): ITexture? {
                return WaterTileRenderer.instance.refractionTexture
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<LowpolyWavyWater>("depthTexture", 2) {
            override fun getUniformValue(state: RenderState<LowpolyWavyWater>): ITexture? {
                return WaterTileRenderer.instance.depthTexture
            }
        })

        shadersProgram.bindAttribute(0, "in_position")
        shadersProgram.bindAttribute(1, "in_position1")
        shadersProgram.bindAttribute(2, "in_position2")
    }

    override fun render(context: RenderContext) {
        shadersProgram.bind()

        GlUtils.disableCulling()
        GlUtils.enableDepthTest()
        GlUtils.enableAlphaBlending()

        val renderState = RenderState<LowpolyWavyWater>(this, context.camera)
        shadersProgram.updatePerRenderUniforms(renderState)

        for (waterTile in renderList) {
            val instanceState = RenderState<LowpolyWavyWater>(this, waterTile, context.camera)
            shadersProgram.updatePerInstanceUniforms(instanceState)

            waterTile.model.render()
        }

        shadersProgram.unbind()
    }

    override fun cleanUp() {
        LowpolyWavyWater.deleteModel()
        shadersProgram.delete()
    }

    companion object {
        private const val VERTEX_FILE = "/engine/water/lowpoly/lowpolyWavyWaterVertex.glsl"
        private const val FRAGMENT_FILE = "/engine/water/lowpoly/lowpolyWavyWaterFragment.glsl"

        @JvmStatic
        val instance: LowpolyWavyWaterRenderer = LowpolyWavyWaterRenderer()
    }
}
