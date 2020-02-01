package engine.postprocessing.deferredfog

import engine.engineObjects.Fog
import engine.engineObjects.UniformFogProperty
import engine.postprocessing.AbstractPostProcessor
import engine.rendering.Camera
import engine.rendering.RenderOutputData
import maths.utils.Matrix4
import opengl.shaders.*
import opengl.textures.Texture

class DeferredFog(val fog: Fog, val camera: Camera) : AbstractPostProcessor(FRAG_FILE) {

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
        shadersProgram.addPerRenderUniform(object : UniformFogProperty<RenderOutputData>("fog") {
            override fun getUniformValue(state: RenderState<RenderOutputData>): Fog {
                return fog
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformValueProperty<RenderOutputData>("projectionMatrixInv") {
            override fun getUniformValue(state: RenderState<RenderOutputData>): UniformValue {
                return camera.projectionMatrix.invert(projectionMatrixInv)
            }
        })
    }

    companion object {
        private const val FRAG_FILE = "/engine/postprocessing/deferredfog/DeferredFogFragment.glsl"

        private val projectionMatrixInv = Matrix4.create()
    }

}
