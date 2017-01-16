package leojay.tools.java.database;

/**
 * package:cn.ilinkerstudio.leojay.tools.java.database
 * project: i-LinkerStudio
 * author:leojay
 * time:16/10/7__11:01
 */
public interface OnDBWriteListener extends ReturnError {
    /**
     * @param number 返回成功数量
     */
    void result(int number);
}
