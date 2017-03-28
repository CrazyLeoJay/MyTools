package leojay.tools.java.database4.core.assist_arg;

/**
 * <p>
 * time: 17/1/16__11:30
 *
 * @author leojay
 */
public interface ResultListener<T> {
    void after(StateMode mode, T resultArg);
}
