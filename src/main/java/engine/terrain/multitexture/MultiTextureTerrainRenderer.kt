package engine.terrain.multitexture

import engine.light.Light
import engine.light.UniformLightProperty
import engine.models.Lod
import engine.rendering.RenderContext
import engine.rendering.Renderer3D
import maths.utils.Matrix4
import opengl.shaders.*
import opengl.textures.ITexture
import opengl.textures.MultiTexture
import opengl.utils.GlUtils

class MultiTextureTerrainRenderer @Throws(Exception::class) constructor() : Renderer3D<MultiTextureTerrain>() {

    private val shadersProgram: ShadersProgram<MultiTextureTerrain>

    init {
        shadersProgram = ShadersProgram.create(Shader.createVertex(VERT_FILE), Shader.createFragment(FRAG_FILE))

        shadersProgram.addPerRenderUniform(object : UniformValueProperty<MultiTextureTerrain>("projectionViewMatrix") {
            override fun getUniformValue(state: RenderState<MultiTextureTerrain>): UniformValue {
                return state.camera.projectionViewMatrix
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformValueProperty<MultiTextureTerrain>("cameraPosition") {
            override fun getUniformValue(state: RenderState<MultiTextureTerrain>): UniformValue {
                return state.camera.transform.position
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformValueProperty<MultiTextureTerrain>("clipPlane") {
            override fun getUniformValue(state: RenderState<MultiTextureTerrain>): UniformValue {
                return getContext().clipPlane.value
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<MultiTextureTerrain>("shadowDistance") {
            override fun getUniformValue(state: RenderState<MultiTextureTerrain>): Float {
                return getContext().shadowDistance
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<MultiTextureTerrain>("shadowPower") {
            override fun getUniformValue(state: RenderState<MultiTextureTerrain>): Float {
                return getContext().shadowPower
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<MultiTextureTerrain>("shadowBias") {
            override fun getUniformValue(state: RenderState<MultiTextureTerrain>): Float {
                return getContext().shadowBias
            }
        })
        shadersProgram.addPerRenderUniform(UniformArrayProperty("lights", 10, { getContext().lights.size }) { name, index ->
            object : UniformLightProperty<MultiTextureTerrain>(name) {
                override fun getUniformValue(state: RenderState<MultiTextureTerrain>): Light {
                    return getContext().lights[index]
                }
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformIntProperty<MultiTextureTerrain>("lightsCount") {
            override fun getUniformValue(state: RenderState<MultiTextureTerrain>): Int {
                return getContext().lights.size
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformIntProperty<MultiTextureTerrain>("tiling") {
            override fun getUniformValue(state: RenderState<MultiTextureTerrain>): Int {
                return state.instance.tiling
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformValueProperty<MultiTextureTerrain>("transformationMatrix") {
            override fun getUniformValue(state: RenderState<MultiTextureTerrain>): UniformValue {
                return state.instance.transform.transformationMatrix
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformValueProperty<MultiTextureTerrain>("viewMatrix") {
            override fun getUniformValue(state: RenderState<MultiTextureTerrain>): UniformValue {
                return state.camera.viewMatrix
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformValueProperty<MultiTextureTerrain>("shadowSpaceMatrix") {
            override fun getUniformValue(state: RenderState<MultiTextureTerrain>): UniformValue {
                return getContext().sun.toViewSpace(state.instance.transform.transformationMatrix, Matrix4.pool.poolAndGive())
            }

            override fun valueAvailable(): Boolean {
                return getContext().sun != null && getContext().sun.isEnabled
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformMultiTextureProperty<MultiTextureTerrain>("blendMap",
                "dTexture", "rTexture", "gTexture", "bTexture", 0) {
            override fun getUniformValue(state: RenderState<MultiTextureTerrain>): MultiTexture? {
                return state.instance.skin.texture as MultiTexture
            }
        })

        shadersProgram.addPerRenderUniform(object : UniformBooleanProperty<MultiTextureTerrain>("enableShadows") {
            override fun getUniformValue(state: RenderState<MultiTextureTerrain>): Boolean {
                return getContext().sun != null && getContext().sun.isEnabled
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<MultiTextureTerrain>("shadowMap", 5) {
            override fun getUniformValue(state: RenderState<MultiTextureTerrain>): ITexture? {
                return getContext().sun.shadowMap
            }

            override fun valueAvailable(): Boolean {
                return getContext().sun != null && getContext().sun.isEnabled
            }
        })

        shadersProgram.bindAttribute(0, "position")
        shadersProgram.bindAttribute(1, "texCoord")
        shadersProgram.bindAttribute(2, "normal")
    }

    override fun render(context: RenderContext) {
        shadersProgram.bind()

        GlUtils.enableCulling()
        GlUtils.disableBlending()
        GlUtils.enableDepthTest()
        GlUtils.enableDepthMasking()
        GlUtils.setProvokingVertexFirst()

        val renderState = RenderState<MultiTextureTerrain>(this, context.camera)
        shadersProgram.updatePerRenderUniforms(renderState)

        for (terrain in renderList) {
            val instanceState = RenderState<MultiTextureTerrain>(this, terrain, context.camera)
            shadersProgram.updatePerInstanceUniforms(instanceState)

            val terrainPosition = terrain.transform.position
            val cameraPosition = context.camera.transform.position
            val distance = terrainPosition.distance(cameraPosition)
            val lod = (distance / terrain.size).toInt()
            (terrain.model.lod as Lod).set(lod)

            terrain.model.render()
        }

        shadersProgram.unbind()
    }

    override fun cleanUp() {
        shadersProgram.delete()
    }

    companion object {
        private const val VERT_FILE = "/engine/terrain/multitexture/multiTextureTerrainVert.glsl"
        private const val FRAG_FILE = "/engine/terrain/multitexture/multiTextureTerrainFrag.glsl"

        @JvmStatic
        val instance: MultiTextureTerrainRenderer = MultiTextureTerrainRenderer()

    }
}
