package leojay.tools.java.database;

/**
 * package:pre.cl.quicksend.database.listener
 * project: Quicksend
 * author:leojay
 * time:16/9/3__17:30
 */
public interface ReturnError {

    /**
     * @param message 错误信息
     * @return 返回 message
     */
    String onError(String message);
}
