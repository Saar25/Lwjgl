package engine.terrain

import engine.light.Light
import engine.light.UniformLightProperty
import engine.models.Lod
import engine.rendering.RenderContext
import engine.rendering.Renderer3D
import maths.utils.Matrix4
import opengl.shaders.*
import opengl.textures.ITexture
import opengl.utils.GlUtils

class TerrainRenderer @Throws(Exception::class) constructor() : Renderer3D<Terrain>() {

    private val shadersProgram: ShadersProgram<Terrain>

    init {
        shadersProgram = ShadersProgram.create(Shader.createVertex(VERT_FILE), Shader.createFragment(FRAG_FILE))

        shadersProgram.addPerRenderUniform(object : UniformValueProperty<Terrain>("projectionViewMatrix") {
            override fun getUniformValue(state: RenderState<Terrain>): UniformValue {
                return state.camera.projectionViewMatrix
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformValueProperty<Terrain>("in_cameraPosition") {
            override fun getUniformValue(state: RenderState<Terrain>): UniformValue {
                return state.camera.transform.position
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformValueProperty<Terrain>("clipPlane") {
            override fun getUniformValue(state: RenderState<Terrain>): UniformValue {
                return getContext().clipPlane.value
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<Terrain>("shadowDistance") {
            override fun getUniformValue(state: RenderState<Terrain>): Float {
                return getContext().shadowDistance
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<Terrain>("shadowPower") {
            override fun getUniformValue(state: RenderState<Terrain>): Float {
                return getContext().shadowPower
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<Terrain>("shadowBias") {
            override fun getUniformValue(state: RenderState<Terrain>): Float {
                return getContext().shadowBias
            }
        })
        shadersProgram.addPerRenderUniform(UniformArrayProperty("lights", 10, { getContext().lights.size }) { name, index ->
            object : UniformLightProperty<Terrain>(name) {
                override fun getUniformValue(state: RenderState<Terrain>): Light {
                    return getContext().lights[index]
                }
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformIntProperty<Terrain>("lightsCount") {
            override fun getUniformValue(state: RenderState<Terrain>): Int {
                return getContext().lights.size
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformValueProperty<Terrain>("transformationMatrix") {
            override fun getUniformValue(state: RenderState<Terrain>): UniformValue {
                return state.instance.transform.transformationMatrix
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformBooleanProperty<Terrain>("multiTexturing") {
            override fun getUniformValue(state: RenderState<Terrain>): Boolean {
                return state.instance.skin.hasTexture()
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformBooleanProperty<Terrain>("flatShading") {
            override fun getUniformValue(state: RenderState<Terrain>): Boolean {
                return state.instance.skin == null || !state.instance.skin.hasTexture()
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformValueProperty<Terrain>("shadowMapSpaceMatrix") {
            override fun getUniformValue(state: RenderState<Terrain>): UniformValue {
                return getContext().sun.toViewSpace(state.instance.transform.transformationMatrix, Matrix4.pool.poolAndGive())
            }

            override fun valueAvailable(): Boolean {
                return getContext().sun != null && getContext().sun.isEnabled
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformTextureProperty<Terrain>("u_texture", 0) {
            override fun getUniformValue(state: RenderState<Terrain>): ITexture? {
                return state.instance.skin.texture
            }
        })

        shadersProgram.addPerRenderUniform(object : UniformBooleanProperty<Terrain>("enableShadows") {
            override fun getUniformValue(state: RenderState<Terrain>): Boolean {
                return getContext().sun != null && getContext().sun.isEnabled
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<Terrain>("shadowMap", 5) {
            override fun getUniformValue(state: RenderState<Terrain>): ITexture? {
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
        GlUtils.enableDepthTest()
        GlUtils.disableBlending()
        GlUtils.setProvokingVertexFirst()

        val renderState = RenderState<Terrain>(this, context.camera)
        shadersProgram.updatePerRenderUniforms(renderState)

        for (terrain in renderList) {
            val instanceState = RenderState<Terrain>(this, terrain, context.camera)
            shadersProgram.updatePerInstanceUniforms(instanceState)

            val terrainPosition = terrain.transform.position
            val cameraPosition = context.camera.transform.position
            val distance = terrainPosition.distance(cameraPosition)
            val lod = (distance / terrain.size).toInt()
            (terrain.model.lod as Lod).set(lod)

            //terrain.model.setLevelOfDetail(lodLevel)

            terrain.model.render()
        }

        shadersProgram.unbind()
    }

    override fun cleanUp() {
        shadersProgram.delete()
    }

    companion object {
        private const val VERT_FILE = "/engine/terrain/terrainVert.glsl"
        private const val FRAG_FILE = "/engine/terrain/terrainFrag.glsl"

        @JvmStatic
        val instance: TerrainRenderer = TerrainRenderer()

        @JvmStatic
        var lodLevel: Int = 3
    }
}
