package tegui.graphics.render

import engine.models.Model
import engine.models.ModelGenerator
import glfw.window.Window
import maths.joml.Vector2i
import maths.objects.Polygon
import opengl.shaders.*
import opengl.utils.GlUtils
import tegui.graphics.Graphics
import tegui.style.property.Colour
import tegui.style.property.IColour
import java.util.*

class RenderGraphics : Graphics {

    private val windowSize: Vector2i = Vector2i()

    private val shadersProgram = createProgram()
    private val renderList = ArrayList<Model>()

    private var colour: IColour = Colour.BLACK

    init {
        shadersProgram.addPerRenderUniform(object : UniformValueProperty<Model>("windowSize") {
            override fun getUniformValue(state: RenderState<Model>): UniformValue {
                return windowSize.set(Window.current().width, Window.current().height)
            }
        })
        shadersProgram.addPerRenderUniform(object : UniformUIntProperty<Model>("colour") {
            override fun getUniformValue(state: RenderState<Model>): Int {
                return colour.asInt()
            }
        })
    }

    override fun setColour(colour: IColour) {
        this.colour = colour
    }

    override fun drawLine(x1: Int, y1: Int, x2: Int, y2: Int) {
        /*val line = Line(x1.toFloat() / windowSize.x, y1.toFloat() / windowSize.y,
                x2.toFloat() / windowSize.x, y2.toFloat() / windowSize.y)
        renderList.add(line)*/
    }

    override fun drawRectangle(x: Int, y: Int, w: Int, h: Int) {

    }

    override fun fillRectangle(x: Int, y: Int, w: Int, h: Int) {

    }

    override fun drawOval(cx: Int, cy: Int, a: Int, b: Int) {

    }

    override fun fillOval(cx: Int, cy: Int, a: Int, b: Int) {

    }

    override fun fillPolygon(polygon: Polygon) {

    }

    override fun clear(clearColour: IColour) {
        GlUtils.setClearColour(clearColour.red, clearColour.green, clearColour.blue, 1f)
    }

    override fun process() {
        if (renderList.isEmpty()) {
            return
        }
        shadersProgram.bind()
        shadersProgram.updatePerRenderUniforms(RenderState(null, null))

        for (model in renderList) {
            shadersProgram.updatePerInstanceUniforms(RenderState(null, null, null))
            model.render()
        }
        renderList.clear()
    }

    override fun delete() {
        shadersProgram.delete()
    }

    companion object {

        private val RECTANGLE = ModelGenerator.generateSquare()

        private fun createProgram(): ShadersProgram<Model> {
            return ShadersProgram.create(
                    "/tegui/graphics/render/renderGraphicsVertex.glsl",
                    "/tegui/graphics/render/renderGraphicsFragment.glsl")
        }
    }
}
