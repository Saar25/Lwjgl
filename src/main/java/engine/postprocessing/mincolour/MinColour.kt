package engine.postprocessing.mincolour

import engine.postprocessing.AbstractPostProcessor
import engine.postprocessing.PostProcessor
import engine.rendering.RenderOutputData
import maths.joml.Vector3f
import maths.utils.Vector3
import opengl.shaders.RenderState
import opengl.shaders.UniformTextureProperty
import opengl.shaders.UniformValueProperty
import opengl.textures.ITexture

class MinColour constructor(r: Float, g: Float, b: Float)
    : AbstractPostProcessor(FRAG_FILE), PostProcessor {

    val minColour: Vector3f = Vector3.of(r, g, b)

    init {
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<RenderOutputData>("u_texture", 0) {
            override fun getUniformValue(state: RenderState<RenderOutputData>): ITexture {
                return state.instance.colour
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformValueProperty<RenderOutputData>("minColour") {
            override fun getUniformValue(state: RenderState<RenderOutputData>): Vector3f {
                return minColour
            }
        })
    }

    companion object {

        private const val FRAG_FILE = "/engine/postprocessing/mincolour/minColourFrag.glsl"

    }

}
