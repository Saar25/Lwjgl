package engine.postprocessing.custom

import engine.postprocessing.AbstractPostProcessor
import engine.postprocessing.PostProcessor
import engine.rendering.RenderOutputData
import opengl.shaders.RenderState
import opengl.shaders.UniformTextureProperty
import opengl.textures.ITexture

class Custom() : AbstractPostProcessor(FRAG_FILE), PostProcessor {

    init {
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<RenderOutputData>("albedo", 0) {
            override fun getUniformValue(state: RenderState<RenderOutputData>): ITexture {
                return state.instance.colour
            }
        })
    }

    companion object {
        private const val FRAG_FILE = "/engine/postprocessing/custom/CustomFragment.glsl"
    }

}
