package engine.postprocessing.combine

import engine.postprocessing.AbstractPostProcessor
import engine.rendering.RenderOutputData
import engine.postprocessing.PostProcessor
import opengl.shaders.RenderState
import opengl.shaders.UniformFloatProperty
import opengl.shaders.UniformTextureProperty
import opengl.textures.ITexture
import opengl.textures.Texture

class Combine() : AbstractPostProcessor(FRAG_FILE), PostProcessor {

    constructor(scalar1: Float, scalar2: Float) : this() {
        this.scalar1 = scalar1
        this.scalar2 = scalar2
    }

    constructor(combination: Texture) : this() {
        this.combination = combination
    }

    var scalar1: Float = 1f
    var scalar2: Float = 1f
    var combination: Texture = Texture.NONE

    init {
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<RenderOutputData>("texture1", 0) {
            override fun getUniformValue(state: RenderState<RenderOutputData>): ITexture {
                return state.instance.colour
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<RenderOutputData>("texture2", 1) {
            override fun getUniformValue(state: RenderState<RenderOutputData>): ITexture {
                return combination
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<RenderOutputData>("scalar1") {
            override fun getUniformValue(state: RenderState<RenderOutputData>): Float {
                return scalar1
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<RenderOutputData>("scalar2") {
            override fun getUniformValue(state: RenderState<RenderOutputData>): Float {
                return scalar2
            }
        })
    }

    companion object {
        private const val FRAG_FILE = "/engine/postprocessing/combine/combineFragment.glsl"
    }
}
