package engine.postprocessing.deferredRendering

import engine.postprocessing.AbstractPostProcessor
import engine.postprocessing.PostProcessor
import engine.rendering.Camera
import engine.rendering.RenderOutputData
import maths.utils.Matrix4
import opengl.shaders.*
import opengl.textures.ITexture

class DeferredRendering(private val camera: Camera) : AbstractPostProcessor(FRAG_FILE), PostProcessor {

    init {
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<RenderOutputData>("albedo", 0) {
            override fun getUniformValue(state: RenderState<RenderOutputData>): ITexture {
                return state.instance.colour
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<RenderOutputData>("normal", 1) {
            override fun getUniformValue(state: RenderState<RenderOutputData>): ITexture {
                return state.instance.normal
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<RenderOutputData>("depth", 2) {
            override fun getUniformValue(state: RenderState<RenderOutputData>): ITexture {
                return state.instance.depth
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformValueProperty<RenderOutputData>("projMatrixInv") {
            override fun getUniformValue(state: RenderState<RenderOutputData>): UniformValue {
                return camera.projectionMatrix.invert(matrix)
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformValueProperty<RenderOutputData>("viewMatrixInv") {
            override fun getUniformValue(state: RenderState<RenderOutputData>): UniformValue {
                return camera.viewMatrix.invert(matrix)
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<RenderOutputData>("zNear") {
            override fun getUniformValue(state: RenderState<RenderOutputData>) = camera.nearPlane
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<RenderOutputData>("zFar") {
            override fun getUniformValue(state: RenderState<RenderOutputData>) = camera.farPlane
        })
        shadersProgram.addPerRenderUniform(object : UniformIntProperty<RenderOutputData>("u_index") {
            override fun getUniformValue(state: RenderState<RenderOutputData>) = index
        })
    }

    companion object {

        private const val FRAG_FILE = "/engine/postprocessing/deferredRendering/DeferredRenderingFragment.glsl"

        private val matrix = Matrix4.create()

        @JvmStatic
        var index: Int = 0

        @JvmStatic
        fun inc() = index++

    }

}
