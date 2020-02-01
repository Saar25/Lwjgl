package tegui.text

import engine.rendering.RenderContext
import engine.rendering.Renderer3D
import opengl.shaders.ShadersProgram

class LetterRenderer private constructor() : Renderer3D<Letter>() {

    private val shadersProgram: ShadersProgram<Letter>

    init {
        this.shadersProgram = ShadersProgram.create(VERT_FILE, FRAG_FILE)

        shadersProgram.bindAttribute(0, "in_character")
        shadersProgram.bindAttribute(1, "in_position")
    }

    override fun render(context: RenderContext) {
        if (!anyProcessed()) {
            return
        }

        shadersProgram.bind()

    }

    override fun cleanUp() {
        shadersProgram.delete()
    }

    companion object {

        private const val VERT_FILE = "/tegui/text/letterVertexShader.glsl"
        private const val FRAG_FILE = "/tegui/text/letterFragmentShader.glsl"

        @JvmStatic
        var instance: LetterRenderer = LetterRenderer()
    }
}
