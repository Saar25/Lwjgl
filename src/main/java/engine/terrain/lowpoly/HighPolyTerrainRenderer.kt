package engine.terrain.lowpoly

import engine.light.Light
import engine.light.UniformLightProperty
import engine.models.Lod
import engine.rendering.RenderContext
import engine.rendering.Renderer3D
import engine.terrain.Terrain
import maths.utils.Matrix4
import opengl.shaders.*
import opengl.textures.ITexture
import opengl.utils.GlUtils

open class HighPolyTerrainRenderer @Throws(Exception::class) constructor() : Renderer3D<Terrain>() {

    protected val shadersProgram: ShadersProgram<Terrain>

    init {
        shadersProgram = ShadersProgram.create(Shader.createVertex(VERT_FILE), Shader.createFragment(FRAG_FILE))

        shadersProgram.addPerRenderUniform(object : UniformValueProperty<Terrain>("projectionViewMatrix") {
            override fun getUniformValue(state: RenderState<Terrain>): UniformValue {
                return state.camera.projectionViewMatrix
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformValueProperty<Terrain>("cameraPosition") {
            override fun getUniformValue(state: RenderState<Terrain>): UniformValue {
                return state.camera.transform.position
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformValueProperty<Terrain>("clipPlane") {
            override fun getUniformValue(state: RenderState<Terrain>): UniformValue {
                return getContext().clipPlane.value
            }
        })
        shadersProgram.addPerRenderUniform(UniformArrayProperty<Terrain>("lights", 10, { getContext().lights.size }) { name, index ->
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
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<Terrain>("shadowDistance") {
            override fun getUniformValue(state: RenderState<Terrain>): Float {
                return getContext().shadowDistance
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformFloatProperty<Terrain>("amplitude") {
            override fun getUniformValue(state: RenderState<Terrain>): Float {
                return state.instance.amplitude
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<Terrain>("shadowMap", 0) {
            override fun getUniformValue(state: RenderState<Terrain>): ITexture {
                return getContext().sun.shadowMap
            }

            override fun valueAvailable(): Boolean {
                return getContext().sun != null && getContext().sun.isEnabled
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformBooleanProperty<Terrain>("enableShadows") {
            override fun getUniformValue(state: RenderState<Terrain>): Boolean {
                return getContext().sun != null && getContext().sun.isEnabled
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

        shadersProgram.addPerInstanceUniform(object : UniformValueProperty<Terrain>("transformationMatrix") {
            override fun getUniformValue(state: RenderState<Terrain>): UniformValue {
                return state.instance.transform.transformationMatrix
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformValueProperty<Terrain>("shadowSpaceMatrix") {
            override fun getUniformValue(state: RenderState<Terrain>): UniformValue {
                return getContext().sun.toViewSpace(state.instance.transform.transformationMatrix, Matrix4.pool.poolAndGive())
            }

            override fun valueAvailable(): Boolean {
                return getContext().sun != null && getContext().sun.isEnabled
            }
        })

    }

    override fun render(context: RenderContext) {
        shadersProgram.bind()

        GlUtils.enableCulling()
        GlUtils.disableBlending()
        GlUtils.enableDepthTest()
        GlUtils.enableDepthMasking()
        GlUtils.setProvokingVertexLast()

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

            terrain.model.render()
        }

        shadersProgram.unbind()
    }

    override fun cleanUp() {
        shadersProgram.delete()
    }

    companion object {
        private const val VERT_FILE = "/engine/terrain/lowpoly/HighPolyTerrainVert.glsl"
        private const val FRAG_FILE = "/engine/terrain/lowpoly/HighPolyTerrainFrag.glsl"

        @JvmStatic
        val instance = HighPolyTerrainRenderer()
    }
}
