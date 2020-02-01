package engine.engineObjects

import opengl.shaders.*

abstract class UniformFogProperty<T>(val name: String) : UniformProperty<T> {

    private val properties: List<UniformProperty<T>> = listOf<UniformProperty<T>>(
            object : UniformFloatProperty<T>("$name.minDistance") {
                override fun getUniformValue(state: RenderState<T>): Float {
                    return this@UniformFogProperty.getUniformValue(state).minDistance
                }
            },
            object : UniformFloatProperty<T>("$name.maxDistance") {
                override fun getUniformValue(state: RenderState<T>): Float {
                    return this@UniformFogProperty.getUniformValue(state).maxDistance
                }
            },
            object : UniformValueProperty<T>("$name.colour") {
                override fun getUniformValue(state: RenderState<T>): UniformValue {
                    return this@UniformFogProperty.getUniformValue(state).colour
                }
            }
    )

    override fun load(state: RenderState<T>) {
        if (valueAvailable()) {
            properties.forEach { uniforms -> uniforms.load(state) }
        }
    }

    override fun initialize(shadersProgram: ShadersProgram<T>) {
        properties.forEach { uniforms -> uniforms.initialize(shadersProgram) }
    }

    abstract fun getUniformValue(state: RenderState<T>): Fog
}
