package engine.postprocessing.sunshafts

import engine.postprocessing.AbstractPostProcessor
import engine.rendering.RenderOutputData
import maths.joml.Vector2f
import maths.utils.Vector2
import opengl.shaders.*
import opengl.textures.ITexture

class Sun(var radius: Float = .1f, var center: Vector2f = Vector2.of(.5f)) : AbstractPostProcessor(FRAG_FILE) {

    init {
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<RenderOutputData>("u_texture", 0) {
            override fun getUniformValue(state: RenderState<RenderOutputData>): ITexture {
                return state.instance.depth
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<RenderOutputData>("radius2") {
            override fun getUniformValue(state: RenderState<RenderOutputData>): Float {
                return radius * radius
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformValueProperty<RenderOutputData>("center") {
            override fun getUniformValue(state: RenderState<RenderOutputData>): UniformValue {
                return center
            }
        })
    }

    companion object {
        private const val FRAG_FILE = "/engine/postprocessing/sunshafts/SunFragment.glsl"
    }
}
