package engine.postprocessing.gammacorrection

import engine.postprocessing.AbstractPostProcessor
import engine.rendering.RenderOutputData
import opengl.shaders.RenderState
import opengl.shaders.UniformFloatProperty
import opengl.shaders.UniformTextureProperty
import opengl.textures.Texture

class GammaCorrection(var factor: Float = 2.2f) : AbstractPostProcessor(FRAG_FILE) {

    init {
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<RenderOutputData>("u_texture", 0) {
            override fun getUniformValue(state: RenderState<RenderOutputData>): Texture {
                return state.instance.colour
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<RenderOutputData>("factor") {
            override fun getUniformValue(state: RenderState<RenderOutputData>): Float {
                return factor
            }
        })
    }

    companion object {
        private const val FRAG_FILE = "/engine/postprocessing/gammacorrection/GammaCorrectionFragment.glsl"
    }

}
