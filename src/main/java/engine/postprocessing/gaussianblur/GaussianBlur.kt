package engine.postprocessing.gaussianblur

import engine.postprocessing.AbstractPostProcessor
import engine.rendering.RenderOutputData
import engine.postprocessing.PostProcessor
import opengl.constants.DataType
import opengl.constants.FormatType
import opengl.fbos.Fbo
import opengl.fbos.FboTarget
import opengl.fbos.attachment.TextureAttachment
import opengl.shaders.RenderState
import opengl.shaders.UniformIntProperty
import opengl.shaders.UniformTextureProperty
import opengl.shadersOld.Uniform
import opengl.textures.Texture
import opengl.textures.TextureConfigs
import opengl.textures.parameters.WrapParameter
import opengl.utils.GlBuffer
import opengl.utils.GlUtils

class GaussianBlur(private var stages: Int, private val scale: Float) : AbstractPostProcessor(FRAG_FILE), PostProcessor {

    private val verticalBlur = Uniform.createBool(shadersProgram, "verticalBlur")

    init {
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<RenderOutputData>("u_texture", 0) {
            override fun getUniformValue(state: RenderState<RenderOutputData>): Texture {
                return state.instance.colour
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformIntProperty<RenderOutputData>("width") {
            override fun getUniformValue(state: RenderState<RenderOutputData>): Int {
                return fbo.width
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformIntProperty<RenderOutputData>("height") {
            override fun getUniformValue(state: RenderState<RenderOutputData>): Int {
                return fbo.height
            }
        })
    }


    override fun createFbo(width: Int, height: Int): Fbo {
        val fbo = Fbo.create((width / scale).toInt(), (height / scale).toInt())
        fbo.addAttachment(TextureAttachment.ofColour(0, TextureConfigs(
                FormatType.RGB8, FormatType.RGB, DataType.U_BYTE)))
        fbo.attachments[0].texture.functions
                .borderColour(0f, 0f, 0f, 0f)
                .wrapS(WrapParameter.CLAMP_TO_BORDER)
                .wrapT(WrapParameter.CLAMP_TO_BORDER)
        return fbo
    }

    override fun process(renderOutputData: RenderOutputData) {
        fbo.bind(FboTarget.DRAW_FRAMEBUFFER)
        GlUtils.clear(GlBuffer.COLOUR)
        shadersProgram.bind()

        shadersProgram.updatePerRenderUniforms(RenderState(null, renderOutputData, null))
        shadersProgram.updatePerInstanceUniforms(RenderState(null, renderOutputData, null))

        verticalBlur.load(true)
        draw()
        texture.bind(0)
        for (i in 1 until stages) {
            draw()
        }
        verticalBlur.load(false)
        for (i in 0 until stages) {
            draw()
        }

        shadersProgram.unbind()
    }

    companion object {
        private const val FRAG_FILE = "/engine/postprocessing/gaussianblur/gaussianBlurFragment.glsl"
    }


}
