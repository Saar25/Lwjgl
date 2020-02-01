package engine.postprocessing.bloom;

import engine.postprocessing.ComposedPostProcessor;
import engine.postprocessing.PostProcessor;
import engine.postprocessing.combine.Combine;
import engine.postprocessing.gaussianblur.GaussianBlur;
import engine.postprocessing.mincolour.MinColour;
import engine.rendering.RenderOutputData;

public class Bloom extends ComposedPostProcessor implements PostProcessor {

    private final Combine combine;

    private Bloom(MinColour minColour, GaussianBlur gaussianBlur, Combine combine) {
        super(minColour, gaussianBlur, combine);
        this.combine = combine;
    }

    public Bloom(float r, float g, float a) {
        this(new MinColour(r, g, a), new GaussianBlur(16, 4), new Combine(1, 1.2f));
    }

    public static Bloom create(float r, float g, float a) throws Exception {
        final MinColour minColour = new MinColour(r, g, a);
        final GaussianBlur gaussianBlur = new GaussianBlur(16, 4);
        final Combine combine = new Combine(1, 1.2f);
        return new Bloom(minColour, gaussianBlur, combine);
    }

    @Override
    public void beforeProcess(RenderOutputData postProcessingData) {
        combine.setCombination(postProcessingData.getColour());
    }
}
