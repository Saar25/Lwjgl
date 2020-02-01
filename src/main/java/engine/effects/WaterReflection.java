package engine.effects;

import engine.rendering.Camera;
import engine.rendering.RenderManager;
import engine.util.node.Group;
import engine.water.WaterTileRenderer;
import opengl.constants.DataType;
import opengl.constants.FormatType;
import opengl.fbos.Fbo;
import opengl.fbos.FboTarget;
import opengl.fbos.attachment.RenderBufferAttachment;
import opengl.fbos.attachment.TextureAttachment;
import opengl.objects.ClipPlane;
import opengl.textures.TextureConfigs;
import opengl.utils.GlUtils;

public class WaterReflection extends ExtraRenderEffect {

    private static final int WIDTH = 640;
    private static final int HEIGHT = 360;

    public WaterReflection(Group group) {
        this(group, WIDTH, HEIGHT);
    }

    public WaterReflection(Group group, int width, int height) {
        super(group, width, height);
    }

    @Override
    protected Fbo createFbo() {
        final Fbo fbo = Fbo.create(width, height);
        fbo.addAttachment(TextureAttachment.ofColour(0,
                new TextureConfigs(FormatType.RGB8, FormatType.RGB, DataType.U_BYTE)));
        fbo.addAttachment(RenderBufferAttachment.ofDepth());
        return fbo;
    }

    @Override
    public void process(RenderManager renderManager, Camera camera) {
        fbo.bind(FboTarget.DRAW_FRAMEBUFFER);
        GlUtils.clearColourAndDepthBuffer();
        GlUtils.enableClipPlane(0);

        ClipPlane clipPlane = ClipPlane.ofAbove(-1);
        renderManager.setClipPlane(clipPlane);

        camera.reflect();
        renderManager.render(group, camera);
        camera.reflect();
    }

    @Override
    protected void onEnable() {
        WaterTileRenderer.getInstance().setReflectionTexture(
                fbo.getAttachments().get(0).getTexture());
    }

    @Override
    public void onDisable() {
        WaterTileRenderer.getInstance().setReflectionTexture(null);
    }
}
