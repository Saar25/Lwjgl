package engine.light

import opengl.shaders.*

abstract class UniformLightProperty<T>(val name: String) : UniformProperty<T> {

    private val properties: List<UniformProperty<T>> = listOf(
            object : UniformValueProperty<T>("$name.colour") {
                override fun getUniformValue(state: RenderState<T>): UniformValue {
                    return this@UniformLightProperty.getUniformValue(state).colour
                }
            },
            object : UniformValueProperty<T>("$name.position") {
                override fun getUniformValue(state: RenderState<T>): UniformValue {
                    return this@UniformLightProperty.getUniformValue(state).position
                }
            },
            object : UniformValueProperty<T>("$name.attenuation") {
                override fun getUniformValue(state: RenderState<T>): UniformValue {
                    return this@UniformLightProperty.getUniformValue(state).attenuation
                }
            },
            object : UniformBooleanProperty<T>("$name.directional") {
                override fun getUniformValue(state: RenderState<T>): Boolean {
                    return this@UniformLightProperty.getUniformValue(state).isDirectional
                }
            },
            object : UniformFloatProperty<T>("$name.intensity") {
                override fun getUniformValue(state: RenderState<T>): Float {
                    return this@UniformLightProperty.getUniformValue(state).intensity
                }
            })

    override fun load(state: RenderState<T>) {
        if (valueAvailable()) {
            properties.forEach { p -> p.load(state) }
        }
    }

    override fun initialize(shadersProgram: ShadersProgram<T>) {
        properties.forEach { p -> p.initialize(shadersProgram) }
    }

    abstract fun getUniformValue(state: RenderState<T>): Light
}
