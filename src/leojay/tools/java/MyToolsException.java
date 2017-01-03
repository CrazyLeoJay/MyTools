package leojay.tools.java;

/**
 * package:leojay.warehouse.tools
 * project: MyTools
 * author:leojay
 * time:16/10/16__18:22
 */
public class MyToolsException extends Exception {
    public MyToolsException(String message) {
        super(message);
        QLog.e(this, "设定异常：" + message);
    }
}

