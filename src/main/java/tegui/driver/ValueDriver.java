package tegui.driver;

public interface ValueDriver<T> {

    void update();

    T getValue();

}
