package engine.rendering

import opengl.textures.Texture

data class RenderOutputData(val colour: Texture = Texture.NONE,
                            val normal: Texture = Texture.NONE,
                            val depth: Texture = Texture.NONE)

