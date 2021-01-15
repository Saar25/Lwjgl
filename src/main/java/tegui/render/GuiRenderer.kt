package tegui.render

import engine.rendering.RenderContext
import engine.rendering.Renderer2D
import glfw.window.Window
import maths.joml.Vector2i
import opengl.constants.RenderMode
import opengl.objects.Vao
import opengl.shaders.*
import opengl.textures.ITexture
import opengl.utils.GlRendering
import opengl.utils.GlUtils
import tegui.GuiObject

class GuiRenderer : Renderer2D<GuiObject>() {

    private val shadersProgram: ShadersProgram<GuiObject>

    init {
        this.shadersProgram = ShadersProgram.create(Shader.createVertex(VERT_FILE), Shader.createFragment(FRAG_FILE))

        shadersProgram.addPerInstanceUniform(object : UniformValueProperty<GuiObject>("transformation") {
            override fun getUniformValue(state: RenderState<GuiObject>): UniformValue {
                return state.instance.transform.transformationMatrix
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformValueProperty<GuiObject>("windowSize") {
            override fun getUniformValue(state: RenderState<GuiObject>): UniformValue {
                return Vector2i(Window.current().width, Window.current().height)
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformBooleanProperty<GuiObject>("hasTexture") {
            override fun getUniformValue(state: RenderState<GuiObject>): Boolean {
                return state.instance.hasTexture()
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformValueProperty<GuiObject>("bounds") {
            override fun getUniformValue(state: RenderState<GuiObject>): UniformValue {
                return state.instance.style.bounds.asVector()
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformValueProperty<GuiObject>("borders") {
            override fun getUniformValue(state: RenderState<GuiObject>): UniformValue {
                return state.instance.style.bordersAttribute.get().asVector()
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformValueProperty<GuiObject>("radiuses") {
            override fun getUniformValue(state: RenderState<GuiObject>): UniformValue {
                return state.instance.style.borderRadiusAttribute.get().asVector()
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformUIntProperty<GuiObject>("borderColour") {
            override fun getUniformValue(state: RenderState<GuiObject>): Int {
                return state.instance.style.borderColourAttribute.get().asInt()
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformValueProperty<GuiObject>("colourModifier") {
            override fun getUniformValue(state: RenderState<GuiObject>): UniformValue {
                return state.instance.style.colourModifier
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformValueProperty<GuiObject>("cornersColours") {
            override fun getUniformValue(state: RenderState<GuiObject>): UniformValue {
                return state.instance.style.backgroundColourAttribute.get().asIVec4()
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformTextureProperty<GuiObject>("u_texture", 0) {
            override fun getUniformValue(state: RenderState<GuiObject>): ITexture? {
                return state.instance.texture
            }
        })
    }

    override fun render(context: RenderContext) {
        if (!anyProcessed()) {
            return
        }

        GlUtils.enableAlphaBlending()
        GlUtils.disableDepthTest()
        GlUtils.disableCulling()

        shadersProgram.bind()
        val state = RenderState<GuiObject>(this, context.camera)
        shadersProgram.updatePerRenderUniforms(state)

        Vao.bindIfNone()
        for (guiObject in renderList) {
            val renderState = RenderState(this, guiObject, context.camera)
            shadersProgram.updatePerInstanceUniforms(renderState)
            GlRendering.drawArrays(RenderMode.TRIANGLE_STRIP, 0, 4)
        }
    }

    override fun cleanUp() {
        shadersProgram.delete()
    }

    companion object {
        private const val VERT_FILE = "/engine/tegui/render/GuiVertex.glsl"
        private const val FRAG_FILE = "/engine/tegui/render/GuiFragment.glsl"

        @JvmStatic
        val instance: GuiRenderer = GuiRenderer()
    }
}
