package engine.water

import engine.gameengine.Time
import engine.light.Light
import engine.light.UniformLightProperty
import engine.rendering.RenderContext
import engine.rendering.Renderer3D
import opengl.shaders.*
import opengl.textures.ITexture
import opengl.textures.Texture
import opengl.utils.GlUtils

class WaterTileRenderer @Throws(Exception::class) private constructor() : Renderer3D<WaterTile>() {

    var reflectionTexture: Texture? = null
    var refractionTexture: Texture? = null
    var depthTexture: Texture? = null

    var time: Float = 0.toFloat()

    private val shadersProgram: ShadersProgram<WaterTile>

    fun Boolean.toInt() = if (this) 1 else 0

    init {
        this.shadersProgram = ShadersProgram.create(Shader.createVertex(VERTEX_FILE), Shader.createFragment(FRAGMENT_FILE))

        shadersProgram.addPerRenderUniform(object : UniformValueProperty<WaterTile>("projectionViewMatrix") {
            override fun getUniformValue(state: RenderState<WaterTile>): UniformValue {
                return state.camera.projectionViewMatrix
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformValueProperty<WaterTile>("in_cameraPosition") {
            override fun getUniformValue(state: RenderState<WaterTile>): UniformValue {
                return state.camera.transform.position
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformValueProperty<WaterTile>("clipPlane") {
            override fun getUniformValue(state: RenderState<WaterTile>): UniformValue {
                return getContext().clipPlane.value
            }
        })
        shadersProgram.addPerRenderUniform(UniformArrayProperty("lights", 10, { getContext().lights.size }) { name, index ->
            object : UniformLightProperty<WaterTile>(name) {
                override fun getUniformValue(state: RenderState<WaterTile>): Light {
                    return getContext().lights[index]
                }
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformIntProperty<WaterTile>("lightsCount") {
            override fun getUniformValue(state: RenderState<WaterTile>): Int {
                return getContext().lights.size
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<WaterTile>("nearPlane") {
            override fun getUniformValue(state: RenderState<WaterTile>): Float {
                return state.camera.nearPlane
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<WaterTile>("farPlane") {
            override fun getUniformValue(state: RenderState<WaterTile>): Float {
                return state.camera.farPlane
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformValueProperty<WaterTile>("transformationMatrix") {
            override fun getUniformValue(state: RenderState<WaterTile>): UniformValue {
                return state.instance.transform.transformationMatrix
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformFloatProperty<WaterTile>("distortionOffset") {
            override fun getUniformValue(state: RenderState<WaterTile>): Float {
                return state.instance.distortionOffset
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<WaterTile>("time") {
            override fun getUniformValue(state: RenderState<WaterTile>): Float {
                return time
            }
        })

        shadersProgram.addPerInstanceUniform(object : UniformIntProperty<WaterTile>("availableTextures") {
            override fun getUniformValue(state: RenderState<WaterTile>): Int {
                var bits = 0
                bits = (bits shl 1) + (state.instance.dudvMap != null).toInt()
                bits = (bits shl 1) + (state.instance.normalsMap != null).toInt()
                bits = (bits shl 1) + (depthTexture != null).toInt()
                bits = (bits shl 1) + (refractionTexture != null).toInt()
                bits = (bits shl 1) + (reflectionTexture != null).toInt()
                return bits
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<WaterTile>("reflectionTexture", 0) {
            override fun getUniformValue(state: RenderState<WaterTile>): ITexture? {
                return reflectionTexture
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<WaterTile>("refractionTexture", 1) {
            override fun getUniformValue(state: RenderState<WaterTile>): ITexture? {
                return refractionTexture
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<WaterTile>("depthTexture", 2) {
            override fun getUniformValue(state: RenderState<WaterTile>): ITexture? {
                return depthTexture
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformTextureProperty<WaterTile>("normalsMap", 3) {
            override fun getUniformValue(state: RenderState<WaterTile>): ITexture? {
                return state.instance.normalsMap
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformTextureProperty<WaterTile>("dudvMap", 4) {
            override fun getUniformValue(state: RenderState<WaterTile>): ITexture? {
                return state.instance.dudvMap
            }
        })

        shadersProgram.bindAttribute(0, "in_position")
    }

    override fun render(context: RenderContext) {
        time += Time.getDelta()

        shadersProgram.bind()

        GlUtils.disableCulling()
        GlUtils.enableDepthTest()
        GlUtils.enableAlphaBlending()

        val renderState = RenderState<WaterTile>(this, context.camera)
        shadersProgram.updatePerRenderUniforms(renderState)

        for (waterTile in renderList) {
            val instanceState = RenderState<WaterTile>(this, waterTile, context.camera)
            shadersProgram.updatePerInstanceUniforms(instanceState)

            waterTile.model.render()
        }

        shadersProgram.unbind()
    }

    override fun cleanUp() {
        WaterTile.deleteModel()
        shadersProgram.delete()
    }

    companion object {
        private const val VERTEX_FILE = "/engine/water/waterVertex.glsl"
        private const val FRAGMENT_FILE = "/engine/water/waterFragment.glsl"

        @JvmStatic
        val instance: WaterTileRenderer = WaterTileRenderer()
    }
}
