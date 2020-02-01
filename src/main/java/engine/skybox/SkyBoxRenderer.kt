package engine.skybox

import engine.models.Model
import engine.models.ModelGenerator
import engine.rendering.RenderContext
import engine.rendering.Renderer3D
import engine.util.Lazy
import maths.joml.Matrix4f
import maths.utils.Matrix4
import opengl.shaders.*
import opengl.textures.ITexture
import opengl.utils.DepthFunction
import opengl.utils.GlUtils

class SkyBoxRenderer private constructor() : Renderer3D<SkyBox>() {

    private val shadersProgram: ShadersProgram<SkyBox>
    private val matrix: Matrix4f = Matrix4.create()

    init {
        this.shadersProgram = ShadersProgram.create(VERT_FILE, FRAG_FILE)

        shadersProgram.addPerInstanceUniform(object : UniformValueProperty<SkyBox>("skyColour") {
            override fun getUniformValue(state: RenderState<SkyBox>): UniformValue {
                return state.instance.skyColour.rgbVector()
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformValueProperty<SkyBox>("fogColour") {
            override fun getUniformValue(state: RenderState<SkyBox>): UniformValue {
                return state.instance.fogColour.rgbaVector()
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformValueProperty<SkyBox>("mvpMatrix") {
            override fun getUniformValue(state: RenderState<SkyBox>): UniformValue {
                return Matrix4.mul(matrix, state.instance.transform.transformationMatrix)
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformTextureProperty<SkyBox>("cubeMap", 0) {
            override fun getUniformValue(state: RenderState<SkyBox>): ITexture? {
                return state.instance.skin.texture
            }
        })
    }

    override fun render(context: RenderContext) {
        if (!anyProcessed()) {
            return
        }

        shadersProgram.bind()

        GlUtils.disableCulling()
        GlUtils.setDepthFunction(DepthFunction.LEQUAL)
        GlUtils.disableDepthMasking()
        GlUtils.disableBlending()

        matrix.set(context.camera.viewMatrix).scale(context.camera.nearPlane)
        context.camera.projectionMatrix.mul(matrix, matrix)

        val renderState = RenderState(this, context.camera)
        shadersProgram.updatePerRenderUniforms(renderState)

        for (skyBox in renderList) {
            val instanceState = RenderState(this, skyBox, context.camera)
            shadersProgram.updatePerInstanceUniforms(instanceState)

            model.get().render()
        }

        shadersProgram.unbind()

        GlUtils.enableDepthMasking()
    }

    override fun cleanUp() {
        if (model.isAssigned) {
            model.get().delete()
        }
        shadersProgram.delete()
    }

    companion object {

        private const val VERT_FILE = "/engine/skybox/skyBoxVert.glsl"
        private const val FRAG_FILE = "/engine/skybox/skyBoxFrag.glsl"

        private val model = Lazy<Model> { ModelGenerator.generateCube() }

        @JvmStatic
        val instance: SkyBoxRenderer = SkyBoxRenderer()

    }
}
