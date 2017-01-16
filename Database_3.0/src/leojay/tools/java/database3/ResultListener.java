package leojay.tools.java.database3;

/**
 * <p>
 * time: 17/1/16__11:30
 *
 * @author leojay
 */
public interface ResultListener<T> {
    void after(Mode mode, T resultArg);
    enum Mode{
        SUCCESS,ERROR,WORN
    }
}
