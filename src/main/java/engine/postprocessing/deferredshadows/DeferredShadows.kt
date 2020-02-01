package engine.postprocessing.deferredshadows

import engine.effects.Shadows
import engine.postprocessing.AbstractPostProcessor
import engine.rendering.Camera
import engine.rendering.RenderOutputData
import engine.rendering.Renderer
import maths.utils.Matrix4
import opengl.shaders.*
import opengl.textures.Texture

class DeferredShadows(private val shadows: Shadows, private val camera: Camera) : AbstractPostProcessor(FRAG_FILE) {

    init {
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<RenderOutputData>("colourTexture", 0) {
            override fun getUniformValue(state: RenderState<RenderOutputData>): Texture {
                return state.instance.colour
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<RenderOutputData>("depthTexture", 1) {
            override fun getUniformValue(state: RenderState<RenderOutputData>): Texture {
                return state.instance.depth
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<RenderOutputData>("shadowTexture", 2) {
            override fun getUniformValue(state: RenderState<RenderOutputData>): Texture {
                return shadows.texture
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<RenderOutputData>("shadowDistance") {
            override fun getUniformValue(state: RenderState<RenderOutputData>): Float {
                return Renderer.getContext().shadowDistance
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<RenderOutputData>("shadowPower") {
            override fun getUniformValue(state: RenderState<RenderOutputData>): Float {
                return Renderer.getContext().shadowPower
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<RenderOutputData>("shadowBias") {
            override fun getUniformValue(state: RenderState<RenderOutputData>): Float {
                return Renderer.getContext().shadowBias
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformValueProperty<RenderOutputData>("cameraPosition") {
            override fun getUniformValue(state: RenderState<RenderOutputData>): UniformValue {
                return camera.transform.position
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformValueProperty<RenderOutputData>("sunSpaceMatrix") {
            override fun getUniformValue(state: RenderState<RenderOutputData>): UniformValue {
                return shadows.sun.getSpaceMatrix(matrix)
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformValueProperty<RenderOutputData>("camProjMatrixInv") {
            override fun getUniformValue(state: RenderState<RenderOutputData>): UniformValue {
                return camera.projectionMatrix.invert(matrix)
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformValueProperty<RenderOutputData>("camViewMatrixInv") {
            override fun getUniformValue(state: RenderState<RenderOutputData>): UniformValue {
                return camera.viewMatrix.invert(matrix)
            }
        })
    }

    companion object {
        private const val FRAG_FILE = "/engine/postprocessing/deferredshadows/DeferredShadowsFragment.glsl"

        private val matrix = Matrix4.create()
    }

}
