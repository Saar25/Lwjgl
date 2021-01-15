package engine.postprocessing.hdr

import engine.postprocessing.AbstractPostProcessor
import engine.rendering.RenderOutputData
import opengl.shaders.RenderState
import opengl.shaders.UniformFloatProperty
import opengl.shaders.UniformTextureProperty
import opengl.textures.Texture

class Hdr(var gamma: Float = 2.2f, var exposure: Float = 1.0f) : AbstractPostProcessor(FRAG_FILE) {

    init {
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<RenderOutputData>("u_texture", 0) {
            override fun getUniformValue(state: RenderState<RenderOutputData>): Texture {
                return state.instance.colour
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<RenderOutputData>("gamma") {
            override fun getUniformValue(state: RenderState<RenderOutputData>): Float {
                return gamma
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<RenderOutputData>("exposure") {
            override fun getUniformValue(state: RenderState<RenderOutputData>): Float {
                return exposure
            }
        })
    }

    companion object {
        private const val FRAG_FILE = "/engine/postprocessing/hdr/HdrFragment.glsl"
    }

}
