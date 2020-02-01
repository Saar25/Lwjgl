package engine.effects;

import engine.rendering.Camera;
import engine.rendering.RenderManager;
import engine.util.node.Group;
import engine.water.WaterTileRenderer;
import opengl.constants.DataType;
import opengl.constants.FormatType;
import opengl.fbos.Fbo;
import opengl.fbos.FboTarget;
import opengl.fbos.attachment.TextureAttachment;
import opengl.objects.ClipPlane;
import opengl.textures.TextureConfigs;
import opengl.utils.GlUtils;

public class WaterRefraction extends ExtraRenderEffect {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    public WaterRefraction(Group group) {
        super(group, WIDTH, HEIGHT);
    }

    public WaterRefraction(Group group, int width, int height) {
        super(group, width, height);
    }

    @Override
    protected Fbo createFbo() {
        final Fbo fbo = Fbo.create(width, height);
        fbo.addAttachment(TextureAttachment.ofColour(0,
                new TextureConfigs(FormatType.RGB8, FormatType.RGB, DataType.U_BYTE)));
        fbo.addAttachment(TextureAttachment.ofDepth(FormatType.DEPTH_COMPONENT24));
        return fbo;
    }

    @Override
    public void process(RenderManager renderManager, Camera camera) {
        fbo.bind(FboTarget.DRAW_FRAMEBUFFER);
        GlUtils.clearColourAndDepthBuffer();
        GlUtils.enableClipPlane(0);

        ClipPlane clipPlane = ClipPlane.ofBelow(1);
        renderManager.setClipPlane(clipPlane);

        renderManager.render(group, camera);
    }

    @Override
    public void onEnable() {
        WaterTileRenderer.getInstance().setRefractionTexture(fbo.getAttachments().get(0).getTexture());
        WaterTileRenderer.getInstance().setDepthTexture(fbo.getDepthAttachment().getTexture());
    }

    @Override
    public void onDisable() {
        WaterTileRenderer.getInstance().setRefractionTexture(null);
        WaterTileRenderer.getInstance().setDepthTexture(null);
    }
}
