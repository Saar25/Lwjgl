package engine.shadows

import engine.rendering.*
import maths.joml.FrustumIntersection
import maths.utils.Matrix4
import opengl.shaders.*
import opengl.textures.ITexture
import opengl.utils.GlUtils

class ShadowsRenderer private constructor() : Renderer3D<Renderable>() {

    private val shaderProgram: ShadersProgram<Renderable>

    init {
        this.shaderProgram = ShadersProgram.create(VERT_FILE, FRAG_FILE)

        shaderProgram.addPerInstanceUniform(object : UniformValueProperty<Renderable>("mvpMatrix") {
            override fun getUniformValue(state: RenderState<Renderable>): UniformValue {
                return state.camera.projectionViewMatrix.mul(
                        state.instance.transform.transformationMatrix, matrix)
            }
        })
        shaderProgram.addPerInstanceUniform(object : UniformIntProperty<Renderable>("textureRowsCount") {
            override fun getUniformValue(state: RenderState<Renderable>): Int {
                return state.instance.skin.textureRows
            }
        })
        shaderProgram.addPerInstanceUniform(object : UniformValueProperty<Renderable>("textureOffset") {
            override fun getUniformValue(state: RenderState<Renderable>): UniformValue {
                return state.instance.skin.textureOffset
            }
        })
        shaderProgram.addPerInstanceUniform(object : UniformTextureProperty<Renderable>("u_texture", 0) {
            override fun getUniformValue(state: RenderState<Renderable>): ITexture {
                return state.instance.skin.texture
            }
        })

        shaderProgram.bindAttribute(0, "in_position")
        shaderProgram.bindAttribute(1, "in_texCoord")
    }

    private fun checkRenderPass(camera: Camera, render: Renderable): Boolean {
        camera.projectionViewMatrix.mul(render.transform.transformationMatrix, matrix)
        return FrustumIntersection(matrix).testAab(render.model.bounds.min, render.model.bounds.max)
    }

    override fun render(context: RenderContext) {
        if (!anyProcessed()) {
            return
        }

        val renderMap = RenderingUtils.optimizeList(renderList)

        shaderProgram.bind()
        val renderState = RenderState(this, context.camera)
        shaderProgram.updatePerRenderUniforms(renderState)

        GlUtils.enableDepthTest()
        GlUtils.disableBlending()

        for ((model, renders) in renderMap) {
            for (render in renders) {
                if (!checkRenderPass(context.camera, render)) {
                    continue
                }
                val instanceState = RenderState(this, render, context.camera)
                shaderProgram.updatePerInstanceUniforms(instanceState)

                if (render.skin.isTransparent) {
                    GlUtils.disableCulling()
                } else {
                    GlUtils.enableCulling()
                }
                model.render()
            }
        }

        shaderProgram.unbind()
    }

    override fun cleanUp() {
        shaderProgram.delete()
    }

    companion object {

        private const val VERT_FILE = "/engine/shadows/shadowsVert.glsl"
        private const val FRAG_FILE = "/engine/shadows/shadowsFrag.glsl"

        private val matrix = Matrix4.create()

        @JvmStatic
        var instance: ShadowsRenderer = ShadowsRenderer()
    }

}
