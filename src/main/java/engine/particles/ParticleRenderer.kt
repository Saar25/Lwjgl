package engine.particles

import engine.rendering.RenderContext
import engine.rendering.Renderer3D
import opengl.shaders.*
import opengl.textures.ITexture
import opengl.utils.GlUtils

class ParticleRenderer private constructor() : Renderer3D<ParticleSystem>() {

    private val shaderProgram: ShadersProgram<ParticleSystem>

    init {
        this.shaderProgram = ShadersProgram.create(VERT_FILE, FRAG_FILE)

        shaderProgram.addPerRenderUniform(object : UniformValueProperty<ParticleSystem>("projectionViewMatrix") {
            override fun getUniformValue(state: RenderState<ParticleSystem>): UniformValue {
                return state.camera.projectionViewMatrix
            }
        })
        shaderProgram.addPerRenderUniform(object : UniformValueProperty<ParticleSystem>("viewMatrix") {
            override fun getUniformValue(state: RenderState<ParticleSystem>): UniformValue {
                return state.camera.viewMatrix
            }
        })
        shaderProgram.addPerRenderUniform(object : UniformValueProperty<ParticleSystem>("clipPlane") {
            override fun getUniformValue(state: RenderState<ParticleSystem>): UniformValue {
                return getContext().clipPlane.value
            }
        })
        shaderProgram.addPerInstanceUniform(object : UniformIntProperty<ParticleSystem>("textureRows") {
            override fun getUniformValue(state: RenderState<ParticleSystem>): Int {
                return state.instance.skin.textureRows
            }
        })
        shaderProgram.addPerInstanceUniform(object : UniformTextureProperty<ParticleSystem>("u_texture", 0) {
            override fun getUniformValue(state: RenderState<ParticleSystem>): ITexture {
                return state.instance.skin.texture
            }
        })

        shaderProgram.bindAttribute(0, "position")
        shaderProgram.bindAttribute(1, "scale")
        shaderProgram.bindAttribute(2, "age")
    }

    override fun render(context: RenderContext) {
        if (!anyProcessed()) {
            return
        }

        GlUtils.enableDepthTest()
        GlUtils.disableDepthMasking()
        GlUtils.enableAdditiveBlending()

        shaderProgram.bind()
        shaderProgram.updatePerRenderUniforms(RenderState(this, context.camera))

        for (particleSystem in renderList) {
            shaderProgram.updatePerInstanceUniforms(RenderState(this, particleSystem, context.camera))

            particleSystem.model.render()
        }

        GlUtils.enableDepthMasking()
        shaderProgram.unbind()
    }

    override fun cleanUp() {
        shaderProgram.delete()
    }

    companion object {

        private const val VERT_FILE = "/engine/particles/particlesVert.glsl"
        private const val FRAG_FILE = "/engine/particles/particlesFrag.glsl"

        @JvmStatic
        val instance: ParticleRenderer = ParticleRenderer()
    }
}
