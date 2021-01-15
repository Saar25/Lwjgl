package engine.entity

import engine.light.Light
import engine.light.UniformLightProperty
import engine.rendering.RenderContext
import engine.rendering.Renderer3D
import maths.joml.FrustumIntersection
import maths.utils.Matrix4
import opengl.shaders.*
import opengl.textures.ITexture
import opengl.utils.GlUtils

internal class EntityRenderer : Renderer3D<Entity>() {

    private val shadersProgram: ShadersProgram<Entity>
    private val frustumIntersection: FrustumIntersection = FrustumIntersection()

    init {
        this.shadersProgram = ShadersProgram.create(VERT_FILE, FRAG_FILE)

        shadersProgram.addPerRenderUniform(object : UniformValueProperty<Entity>("in_cameraPosition") {
            override fun getUniformValue(state: RenderState<Entity>): UniformValue {
                return state.camera.transform.position
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformValueProperty<Entity>("clipPlane") {
            override fun getUniformValue(state: RenderState<Entity>): UniformValue {
                return getContext().clipPlane.value
            }
        })
        shadersProgram.addPerRenderUniform(UniformArrayProperty<Entity>("lights", 10, { getContext().lights.size }) { name, index ->
            object : UniformLightProperty<Entity>(name) {
                override fun getUniformValue(state: RenderState<Entity>): Light {
                    return getContext().lights[index]
                }
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformIntProperty<Entity>("lightsCount") {
            override fun getUniformValue(state: RenderState<Entity>): Int {
                return getContext().lights.size
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<Entity>("specularPower") {
            override fun getUniformValue(state: RenderState<Entity>): Float {
                return 10f
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformBooleanProperty<Entity>("enableShadows") {
            override fun getUniformValue(state: RenderState<Entity>): Boolean {
                return getContext().sun != null && getContext().sun.isEnabled
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<Entity>("shadowDistance") {
            override fun getUniformValue(state: RenderState<Entity>): Float {
                return getContext().shadowDistance
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<Entity>("shadowPower") {
            override fun getUniformValue(state: RenderState<Entity>): Float {
                return getContext().shadowPower
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<Entity>("shadowBias") {
            override fun getUniformValue(state: RenderState<Entity>): Float {
                return getContext().shadowBias
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<Entity>("shadowMap", 1) {
            override fun getUniformValue(state: RenderState<Entity>): ITexture? {
                return getContext().sun.shadowMap
            }

            override fun valueAvailable(): Boolean {
                return getContext().sun != null && getContext().sun.isEnabled
            }
        })

        shadersProgram.addPerInstanceUniform(object : UniformValueProperty<Entity>("mvpMatrix") {
            override fun getUniformValue(state: RenderState<Entity>): UniformValue {
                return Matrix4.pool.poolAndGive().set(state.camera.projectionViewMatrix)
                        .mul(state.instance.transform.transformationMatrix)
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformValueProperty<Entity>("transformation") {
            override fun getUniformValue(state: RenderState<Entity>): UniformValue {
                return state.instance.transform.transformationMatrix
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformValueProperty<Entity>("viewMatrix") {
            override fun getUniformValue(state: RenderState<Entity>): UniformValue {
                return state.camera.viewMatrix
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformValueProperty<Entity>("shadowSpaceMatrix") {
            override fun getUniformValue(state: RenderState<Entity>): UniformValue {
                return getContext().sun.toViewSpace(state.instance.transform.transformationMatrix, Matrix4.pool.poolAndGive())
            }

            override fun valueAvailable(): Boolean {
                return getContext().sun != null && getContext().sun.isEnabled
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformIntProperty<Entity>("textureRowsCount") {
            override fun getUniformValue(state: RenderState<Entity>): Int {
                return state.instance.skin.textureRows
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformValueProperty<Entity>("textureOffset") {
            override fun getUniformValue(state: RenderState<Entity>): UniformValue {
                return state.instance.skin.textureOffset
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformTextureProperty<Entity>("u_texture", 0) {
            override fun getUniformValue(state: RenderState<Entity>): ITexture? {
                return state.instance.skin.texture
            }
        })
    }

    override fun render(context: RenderContext) {
        if (renderList.size < 1) {
            return
        }

        shadersProgram.bind()

        GlUtils.enableCulling()
        GlUtils.enableDepthTest()
        GlUtils.enableDepthMasking()
        GlUtils.enableAlphaBlending()

        val renderState = RenderState<Entity>(this, context.camera)
        shadersProgram.updatePerRenderUniforms(renderState)

        for (entity in renderList) {
            frustumIntersection.set(context.camera.projectionViewMatrix.mul(
                    entity.transform.transformationMatrix, Matrix4.pool.poolAndGive()))
            if (!checkRenderPass(entity)) {
                continue
            }

            val instanceState = RenderState<Entity>(this, entity, context.camera)
            shadersProgram.updatePerInstanceUniforms(instanceState)

            if (entity.skin.isTransparent) {
                GlUtils.disableCulling()
            } else {
                GlUtils.enableCulling()
            }

            entity.model.render()
        }

        shadersProgram.unbind()
    }

    override fun cleanUp() {
        shadersProgram.delete()
    }

    private fun checkRenderPass(entity: Entity): Boolean {
        return !checkClippingCulling(entity.transform.position) &&
                frustumIntersection.testAab(entity.model.bounds.min, entity.model.bounds.max)
    }

    companion object {

        private const val VERT_FILE = "/engine/entity/entityVert.glsl"
        private const val FRAG_FILE = "/engine/entity/entityFrag.glsl"

        @JvmStatic
        val instance: EntityRenderer = EntityRenderer()

    }
}
