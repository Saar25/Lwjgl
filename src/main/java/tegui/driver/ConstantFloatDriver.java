package tegui.driver;

public class ConstantFloatDriver extends FloatDriver implements ValueDriver<Float> {

    public ConstantFloatDriver(float value) {
        this.value = value;
    }

    @Override
    public void update() {

    }
}
