package engine.postprocessing.outline

import engine.postprocessing.AbstractPostProcessor
import engine.postprocessing.PostProcessor
import engine.rendering.RenderOutputData
import opengl.shaders.RenderState
import opengl.shaders.UniformTextureProperty
import opengl.textures.ITexture

class Outline : AbstractPostProcessor(FRAG_FILE), PostProcessor {

    init {
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<RenderOutputData>("albedo", 0) {
            override fun getUniformValue(state: RenderState<RenderOutputData>): ITexture {
                return state.instance.colour;
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<RenderOutputData>("normal", 1) {
            override fun getUniformValue(state: RenderState<RenderOutputData>): ITexture {
                return state.instance.normal;
            }
        })
    }

    companion object {

        private const val FRAG_FILE = "/engine/postprocessing/outline/OutlineFragment.glsl"

    }

}
