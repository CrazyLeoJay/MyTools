package leojay.tools.java.database2.base;

/**
 * <p>
 * time: 17/1/13__19:16
 *
 * @author leojay
 */
public interface ReadWriteResultListener {
    void onError(String error);
    void onAfter();
}
