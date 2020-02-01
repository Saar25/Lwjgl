package engine.rendering;

import engine.engineObjects.Fog;
import engine.entity.EntityRenderer;
import engine.light.LightBatch;
import engine.particles.ParticleRenderer;
import engine.postprocessing.PostProcessing;
import engine.rendering.background.Background;
import engine.rendering.background.BackgroundColour;
import engine.shadows.ShadowsRenderer;
import engine.shadows.Sun;
import engine.shape.lowpoly.LowPolyShapeRenderer;
import engine.shape.smooth.SmoothShapeRenderer;
import engine.skybox.SkyBoxRenderer;
import engine.terrain.TerrainRenderer;
import engine.terrain.lowpoly.HighPolyTerrainRenderer;
import engine.terrain.lowpoly.LowPolyTerrainRenderer;
import engine.terrain.multitexture.MultiTextureTerrainRenderer;
import engine.water.WaterTileRenderer;
import engine.water.flat.FlatWaterRenderer;
import engine.water.lowpoly.LowpolyWavyWaterRenderer;
import engine.water.simple.SimpleWaterRenderer;
import engine.water.wavy.WavyWaterRenderer;
import glfw.window.Window;
import opengl.constants.DataType;
import opengl.constants.FormatType;
import opengl.fbos.*;
import opengl.fbos.attachment.RenderBufferAttachmentMS;
import opengl.fbos.attachment.TextureAttachment;
import opengl.objects.ClipPlane;
import opengl.textures.TextureConfigs;
import opengl.textures.parameters.MagFilterParameter;
import opengl.utils.GlBuffer;
import tegui.render.GuiRenderer;

import java.util.ArrayList;
import java.util.List;

public class RenderManager {

    private final PostProcessing postProcessing;

    private final List<Renderer3D> renderers3D = new ArrayList<>();
    private final List<Renderer2D> renderers2D = new ArrayList<>();
    private final List<Renderer> lateRenderers = new ArrayList<>();
    private final List<Renderer> renderers = new ArrayList<>();

    private Fbo mainScreenFbo;
    private MultisampledFbo mainScreenFboMs;
    private RenderOutputData outputData;

    private final RenderContextBuilder contextBuilder = new RenderContextBuilder();

    private Background background = new BackgroundColour(0, 0, 0);

    private boolean anyChange = true;

    public RenderManager() {
        this.postProcessing = new PostProcessing();

        addRenderer(ShadowsRenderer.getInstance());
        addRenderer(GuiRenderer.getInstance());

        addRenderer(MultiTextureTerrainRenderer.getInstance());
        addRenderer(HighPolyTerrainRenderer.getInstance());
        addRenderer(LowPolyTerrainRenderer.getInstance());
        addRenderer(TerrainRenderer.getInstance());

        addRenderer(FlatWaterRenderer.getInstance());
        addRenderer(SimpleWaterRenderer.getInstance());
        addRenderer(WavyWaterRenderer.getInstance());
        addRenderer(LowpolyWavyWaterRenderer.getInstance());
        addRenderer(WaterTileRenderer.getInstance());

        addRenderer(LowPolyShapeRenderer.getInstance());
        addRenderer(SmoothShapeRenderer.getInstance());

        addRenderer(EntityRenderer.getInstance());
        addLateRenderer(SkyBoxRenderer.getInstance());
        addLateRenderer(ParticleRenderer.getInstance());

        setShadowPower(.7f);
    }

    private void recreateMainScreenFbo(int width, int height) {
        if (mainScreenFbo == null || !mainScreenFbo.isSized(width, height)) {
            if (mainScreenFbo != null) {
                mainScreenFboMs.delete();
                mainScreenFbo.delete();
            }

            this.mainScreenFboMs = new MultisampledFbo(width, height);
            mainScreenFboMs.addAttachment(RenderBufferAttachmentMS.ofColour(0, 4));
            mainScreenFboMs.addAttachment(RenderBufferAttachmentMS.ofColour(1, 4));
            mainScreenFboMs.addAttachment(RenderBufferAttachmentMS.ofDepth(4));

            this.mainScreenFbo = Fbo.create(width, height);
            mainScreenFbo.addAttachment(TextureAttachment.ofColour(0,
                    new TextureConfigs(FormatType.RGBA8, FormatType.RGBA, DataType.U_BYTE)));
            mainScreenFbo.addAttachment(TextureAttachment.ofColour(1,
                    new TextureConfigs(FormatType.RGB8, FormatType.RGBA, DataType.U_BYTE)));
            mainScreenFbo.addAttachment(TextureAttachment.ofDepth(FormatType.DEPTH_COMPONENT24));

            this.outputData = new RenderOutputData(
                    mainScreenFbo.getAttachments().get(0).getTexture(),
                    mainScreenFbo.getAttachments().get(1).getTexture(),
                    mainScreenFbo.getDepthAttachment().getTexture()
            );
            Renderer.getContext().setOutputData(getOutput());
            contextBuilder.setOutputData(getOutput());
        }
    }

    private void updateCamera(Camera camera) {
        camera.updateViewMatrix();
        camera.updateProjectionMatrix();
        camera.updateProjectionViewMatrix();
    }

    private void finish() {
        renderers.forEach(Renderer::finish);
        LightBatch.clear();
    }

    /**
     * Only renders the given node without any preparations
     *
     * @param renderNode the node to render
     * @param camera     the camera
     */
    public void render(Spatial renderNode, Camera camera) {
        renderNode.process();
        updateCamera(camera);
        processLightBatch();

        background.clear();

        this.contextBuilder.setCamera(camera);
        final RenderContext context = contextBuilder.create();

        for (Renderer renderer : renderers) {
            renderer.render(context);
        }
        finish();
    }

    /**
     * Renders the whole scene and project it from the camera view. This will
     * render all the objects attached to the scene. In case any of the extra
     * rendering functions enable before (such as shadow mapping, water
     * reflection etc...)
     *
     * @param scene the scene to render
     */
    public void render(Scene scene) {
        render(ScreenFbo.getInstance(), scene);
    }

    public void render(IFbo fbo, Scene scene) {
        scene.processEffects(this);

        final Camera camera = scene.getCamera();
        updateCamera(camera);

        final int windowWidth = Window.current().getWidth();
        final int windowHeight = Window.current().getHeight();
        recreateMainScreenFbo(windowWidth, windowHeight);
        postProcessing.resize(windowWidth, windowHeight);

        scene.process();
        checkForChanges();

        processLightBatch();
        setClipPlane(ClipPlane.NONE);

        mainScreenFboMs.bind(FboTarget.DRAW_FRAMEBUFFER);
        background.clear();

        this.contextBuilder.setCamera(camera);
        final RenderContext context = contextBuilder.create();

        for (Renderer3D renderer : renderers3D) {
            renderer.render(context);
        }

        mainScreenFboMs.blitFbo(mainScreenFbo, MagFilterParameter.NEAREST, GlBuffer.DEPTH);
        // Blit all fbo attachments
        for (int i = 0; i < 2; i++) {
            mainScreenFbo.setDrawAttachments(i);
            mainScreenFboMs.setReadAttachment(i);
            mainScreenFboMs.blitFbo(mainScreenFbo);
        }

        mainScreenFbo.setDrawAttachments(0);
        fbo.bind(FboTarget.DRAW_FRAMEBUFFER);
        background.clear();
        postProcessing.processToFbo(mainScreenFbo, mainScreenFbo, getOutput());

        mainScreenFbo.bind();

        for (Renderer renderer : lateRenderers) {
            renderer.render(context);
        }

        mainScreenFbo.blitFbo(fbo);

        ScreenFbo.getInstance().bind();
        for (Renderer2D r : renderers2D) {
            r.render(context);
        }

        finish();
    }

    /**
     * Returns the output of the renderer
     *
     * @return the output of the renderer
     */
    public RenderOutputData getOutput() {
        return outputData;
    }

    /**
     * Returns the post processing pipeline
     *
     * @return the post processing pipeline
     */
    public PostProcessing getPostProcessing() {
        return postProcessing;
    }

    private void addRenderer(Renderer3D<?> renderer) {
        if (!renderers.contains(renderer)) {
            renderers.add(renderer);
            renderers3D.add(renderer);
        }
    }

    private void addRenderer(Renderer2D<?> renderer) {
        if (!renderers.contains(renderer)) {
            renderers.add(renderer);
            renderers2D.add(renderer);
        }
    }

    private void addLateRenderer(Renderer<?> renderer) {
        if (!renderers.contains(renderer)) {
            renderers.add(renderer);
            lateRenderers.add(renderer);
        }
    }

    /**
     * Sets the fog that will be used
     *
     * @param fog the new fog
     */
    public void setFog(Fog fog) {
        Renderer.getContext().setFog(fog);
        contextBuilder.setFog(fog);
    }

    /**
     * Sets the sun that will be used
     *
     * @param sun the new sun
     */
    public void setSun(Sun sun) {
        Renderer.getContext().setSun(sun);
        contextBuilder.setSun(sun);
    }

    /**
     * Sets the shadow power will be used
     *
     * @param shadowPower the new fog
     */
    public void setShadowPower(float shadowPower) {
        Renderer.getContext().setShadowPower(shadowPower);
        contextBuilder.setShadowPower(shadowPower);
    }

    /**
     * Sets the shadow power will be used
     *
     * @param shadowBias the new fog
     */
    public void setShadowBias(float shadowBias) {
        Renderer.getContext().setShadowBias(shadowBias);
        contextBuilder.setShadowBias(shadowBias);
    }

    /**
     * Sets the maximum distance the shadows will appear
     *
     * @param shadowDistance the maximum distance
     */
    public void setShadowDistance(float shadowDistance) {
        Renderer.getContext().setShadowDistance(shadowDistance);
        contextBuilder.setShadowDistance(shadowDistance);
    }

    /**
     * Sets the clipping plane to use when clipping the objects
     *
     * @param clipPlane the clipping plane
     */
    public void setClipPlane(ClipPlane clipPlane) {
        Renderer.getContext().setClipPlane(clipPlane);
        contextBuilder.setClipPlane(clipPlane);
    }

    private void processLightBatch() {
        Renderer.getContext().setLights(LightBatch.getLights());
        contextBuilder.getLights().clear();
        contextBuilder.getLights().addAll(LightBatch.getLights());
    }

    /**
     * Sets the background of the scene
     *
     * @param background the background
     */
    public void setBackground(Background background) {
        this.background = background;
    }

    /**
     * Returns whether any change occurred, if anything rendered it will be true
     *
     * @return true if any new changed happened in the screen, false otherwise
     */
    public boolean isAnyChange() {
        return anyChange;
        // TODO: check if camera view matrix changed
    }

    public void setAnyChange(boolean anyChange) {
        this.anyChange = anyChange;
    }

    private void checkForChanges() {
        anyChange = false;
        for (Renderer renderer : renderers) {
            anyChange |= renderer.anyProcessed();
        }
    }

    /**
     * Clean up everything that was created and used, should be called when the program is closed
     */
    public void cleanUp() {
        renderers.forEach(Renderer::cleanUp);
        postProcessing.cleanUp();
        background.delete();
        mainScreenFbo.delete();
    }
}
