package engine.effects;

import engine.light.DirectionalLight;
import engine.rendering.*;
import engine.shadows.ShadowsRenderer;
import engine.shadows.Sun;
import engine.util.node.GGroup;
import engine.util.node.Nodes;
import opengl.constants.DataType;
import opengl.constants.FormatType;
import opengl.fbos.Fbo;
import opengl.fbos.FboTarget;
import opengl.fbos.attachment.Attachment;
import opengl.fbos.attachment.TextureAttachment;
import opengl.objects.ClipPlane;
import opengl.textures.Texture;
import opengl.textures.TextureConfigs;
import opengl.textures.parameters.MagFilterParameter;
import opengl.textures.parameters.MinFilterParameter;
import opengl.textures.parameters.WrapParameter;
import opengl.utils.GlUtils;

public class Shadows extends ExtraRenderEffect {

    private static final int WIDTH = 4096;
    private static final int HEIGHT = 4096;

    private final Sun sun;

    public Shadows(GGroup<?> group, DirectionalLight light) {
        super(group, WIDTH, HEIGHT);
        this.sun = new Sun(getTexture(), light);
    }

    public Sun getSun() {
        return sun;
    }

    @Override
    protected Fbo createFbo() {
        final Fbo fbo = Fbo.create(width, height);
        //final Attachment attachment = TextureAttachment.ofDepth(FormatType.DEPTH_COMPONENT24);
        final Attachment attachment = TextureAttachment.ofDepth(new TextureConfigs(
                FormatType.DEPTH_COMPONENT24, FormatType.DEPTH_COMPONENT, DataType.FLOAT));
        attachment.getTexture().getFunctions()
                .magFilter(MagFilterParameter.NEAREST)
                .minFilter(MinFilterParameter.NEAREST)
                .wrapS(WrapParameter.CLAMP_TO_BORDER)
                .wrapT(WrapParameter.CLAMP_TO_BORDER)
                .borderColour(1, 1, 1, 1);
        fbo.addAttachment(attachment);
        return fbo;
    }

    @Override
    public void process(RenderManager renderManager, Camera camera) {
        fbo.bind(FboTarget.DRAW_FRAMEBUFFER);
        GlUtils.clearColourAndDepthBuffer();

        getSun().setEnabled(true);
        renderManager.setSun(getSun());
        renderManager.setShadowDistance(1000);

        getSun().setShadowDistance(1000);
        getSun().update(camera);

        for (Spatial spatial : Nodes.spread(group)) {
            if (spatial instanceof Renderable) {
                ShadowsRenderer.getInstance().process((Renderable) spatial);
            }
        }

        ClipPlane clipPlane = ClipPlane.NONE;
        renderManager.setClipPlane(clipPlane);

        final RenderContext context = new RenderContext();
        context.setCamera(sun.getViewCamera());
        context.setShadowDistance(1000);
        context.setSun(sun);
        ShadowsRenderer.getInstance().render(context);
        ShadowsRenderer.getInstance().finish();
    }

    @Override
    public Texture getTexture() {
        return fbo.getDepthAttachment().getTexture();
    }

    @Override
    public void onEnable() {
        if (getSun() != null) {
            getSun().setEnabled(true);
        }
    }

    @Override
    public void onDisable() {
        if (getSun() != null) {
            getSun().setEnabled(false);
        }
    }

}
