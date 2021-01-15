package engine.postprocessing.contrast

import engine.postprocessing.AbstractPostProcessor
import engine.rendering.RenderOutputData
import opengl.shaders.RenderState
import opengl.shaders.UniformFloatProperty
import opengl.shaders.UniformTextureProperty
import opengl.textures.Texture

class Contrast(var factor: Float = 1.4f) : AbstractPostProcessor(FRAG_FILE) {

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
        private const val FRAG_FILE = "/engine/postprocessing/contrast/ContrastFragment.glsl"
    }

}
