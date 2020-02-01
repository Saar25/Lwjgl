package engine.postprocessing.mix

import engine.postprocessing.AbstractPostProcessor
import engine.rendering.RenderOutputData
import engine.postprocessing.PostProcessor
import opengl.shaders.RenderState
import opengl.shaders.UniformFloatProperty
import opengl.shaders.UniformTextureProperty
import opengl.textures.ITexture
import opengl.textures.Texture

class MixPostProcessor() : AbstractPostProcessor(FRAG_FILE), PostProcessor {

    constructor(scalar: Float) : this() {
        this.scalar = scalar
    }

    constructor(combination: Texture) : this() {
        this.combination = combination
    }

    var scalar: Float = .5f
    var combination: Texture = Texture.NONE

    init {
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<RenderOutputData>("texture1", 0) {
            override fun getUniformValue(state: RenderState<RenderOutputData>): ITexture {
                return state.instance.colour
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<RenderOutputData>("texture2", 0) {
            override fun getUniformValue(state: RenderState<RenderOutputData>): ITexture {
                return combination
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<RenderOutputData>("scalar") {
            override fun getUniformValue(state: RenderState<RenderOutputData>): Float {
                return scalar
            }
        })
    }

    companion object {
        private const val FRAG_FILE = "/engine/postprocessing/mix/MixFragment.glsl"
    }
}
