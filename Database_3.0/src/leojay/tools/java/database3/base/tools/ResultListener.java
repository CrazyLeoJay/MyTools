package leojay.tools.java.database3.base.tools;

/**
 * <p>
 * time: 17/1/16__11:30
 *
 * @author leojay
 */
public interface ResultListener<T> {
    void after(StateMode mode, T resultArg);
}
