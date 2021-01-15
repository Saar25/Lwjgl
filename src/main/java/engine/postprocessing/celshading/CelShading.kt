package engine.postprocessing.celshading

import engine.postprocessing.AbstractPostProcessor
import engine.postprocessing.PostProcessor
import engine.rendering.RenderOutputData
import opengl.shaders.RenderState
import opengl.shaders.UniformTextureProperty
import opengl.textures.ITexture

class CelShading : AbstractPostProcessor(FRAG_FILE), PostProcessor {

    init {
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<RenderOutputData>("u_texture", 0) {
            override fun getUniformValue(state: RenderState<RenderOutputData>): ITexture {
                return state.instance.colour
            }
        })
    }

    companion object {
        private const val FRAG_FILE = "/engine/postprocessing/celshading/CelShadingFragment.glsl"
    }
}
