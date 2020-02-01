package engine.shape.lowpoly

import engine.light.Light
import engine.light.UniformLightProperty
import engine.rendering.AbstractRenderer3D
import engine.shape.Shape
import maths.joml.FrustumIntersection
import maths.utils.Matrix4
import maths.utils.Vector3
import opengl.shaders.*
import opengl.textures.ITexture
import opengl.utils.GlUtils

class LowPolyShapeRenderer private constructor() : AbstractRenderer3D<Shape>(VERT_FILE, FRAG_FILE) {

    init {
        shadersProgram.addPerInstanceUniform(object : UniformValueProperty<Shape>("colour") {
            override fun getUniformValue(state: RenderState<Shape>): UniformValue {
                return state.instance.colour.rgbaVector()
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformValueProperty<Shape>("clipPlane") {
            override fun getUniformValue(state: RenderState<Shape>): UniformValue {
                return getContext().clipPlane.value
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformValueProperty<Shape>("cameraPosition") {
            override fun getUniformValue(state: RenderState<Shape>): UniformValue {
                return state.camera.transform.position
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformIntProperty<Shape>("lightsCount") {
            override fun getUniformValue(state: RenderState<Shape>): Int {
                return getContext().lights.size
            }
        })
        shadersProgram.addPerRenderUniform(UniformArrayProperty("lights", 10, { getContext().lights.size }, { name, index ->
            object : UniformLightProperty<Shape>(name) {
                override fun getUniformValue(state: RenderState<Shape>): Light {
                    return getContext().lights[index]
                }
            }
        }
        ))

        shadersProgram.addPerInstanceUniform(object : UniformValueProperty<Shape>("transformation") {
            override fun getUniformValue(state: RenderState<Shape>): UniformValue {
                return state.instance.transform.transformationMatrix
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformValueProperty<Shape>("mvpMatrix") {
            override fun getUniformValue(state: RenderState<Shape>): UniformValue {
                val transformation = state.instance.transform.transformationMatrix
                return state.camera.projectionViewMatrix.mul(transformation, Matrix4.create())
            }
        })
        shadersProgram.addPerInstanceUniform(object : UniformValueProperty<Shape>("shadowSpaceMatrix") {
            override fun getUniformValue(state: RenderState<Shape>): UniformValue {
                return getContext().sun.toViewSpace(state.instance.transform.transformationMatrix, Matrix4.pool.poolAndGive())
            }

            override fun valueAvailable(): Boolean {
                return getContext().sun != null && getContext().sun.isEnabled
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<Shape>("shadowPower") {
            override fun getUniformValue(state: RenderState<Shape>): Float {
                return getContext().shadowPower
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<Shape>("shadowBias") {
            override fun getUniformValue(state: RenderState<Shape>): Float {
                return getContext().shadowBias
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformTextureProperty<Shape>("shadowMap", 0) {
            override fun getUniformValue(state: RenderState<Shape>): ITexture {
                return getContext().sun.shadowMap
            }

            override fun valueAvailable(): Boolean {
                return getContext().sun != null && getContext().sun.isEnabled
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformBooleanProperty<Shape>("enableShadows") {
            override fun getUniformValue(state: RenderState<Shape>): Boolean {
                return getContext().sun != null && getContext().sun.isEnabled
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformFloatProperty<Shape>("shadowDistance") {
            override fun getUniformValue(state: RenderState<Shape>): Float {
                return getContext().shadowDistance
            }
        })
    }

    override fun onRenderStage(renderState: RenderState<Shape>?) {
        GlUtils.enableAlphaBlending()
        GlUtils.enableDepthTest()
        GlUtils.enableCulling()
        GlUtils.setProvokingVertexFirst()
    }

    override fun canCullInstance(state: RenderState<Shape>): Boolean {
        val min = Vector3.of(state.instance.model.bounds.min)
        val max = Vector3.of(state.instance.model.bounds.max)
        return !FrustumIntersection(state.camera.projectionViewMatrix.mul(
                state.instance.transform.transformationMatrix, Matrix4.pool.poolAndGive()))
                .testAab(min, max)
    }

    override fun render(instance: Shape) {
        instance.model.render()
    }

    companion object {
        private const val VERT_FILE = "/engine/shape/lowpoly/lowPolyShapeVert.glsl"
        private const val FRAG_FILE = "/engine/shape/lowpoly/lowPolyShapeFrag.glsl"

        @JvmStatic
        val instance = LowPolyShapeRenderer()
    }

}
