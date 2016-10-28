package leojay.warehouse.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * package:pre.cl.quicksend.logcontrol
 * project: Quicksend
 * author:leojay
 * time:16/8/22__16:16
 * 日志格式:    [当前时间]-类名-message-信息
 */
public final class QLog {

    private QLog() {
    }

    /**
     * 提示信息
     *
     * @param <T> 任意类
     * @param t   当前类
     * @param arg message 或 key\message
     */
    public static <T> void i(T t, String... arg) {
        setMessage(t, "info", arg);
    }

    /**
     * 提示错误
     *
     * @param <T> 任意类
     * @param t   当前类
     * @param arg message 或 key\message
     */
    public static <T> void e(T t, String... arg) {
        setMessage(t, "error", arg);
    }

    /**
     * 提示警告
     *
     * @param <T> 任意类
     * @param t   当前类
     * @param arg message 或 key\message
     */
    public static <T> void w(T t, String... arg) {
        setMessage(t, "warning", arg);
    }


    //生成打印文本,并打印.亦可写入数据库
    private static <T> String setMessage(T t, String type, String... args) {
        String data = getThisData();
        String page = t.getClass().getName();
        String key = null;
        String message = null;

        switch (args.length) {
            case 1:
                message = args[0];
                break;
            case 2:
                key = args[0];
                message = args[1];
                break;
            default:
                try {
                    throw new Exception("类: " + t.getClass().getSimpleName() + " 日志参数错误输入");
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }

        StackTraceElement ste = new Throwable().getStackTrace()[2];

        String arg = "";
        if (key != null) {
            arg = "(" + key + ")";
        }
        String result = "[" + data + "]" +
                " package: " + page +
                " \\|/ type:" + type +
                "_ " + arg + "(" + ste.getFileName() + " :" + ste.getLineNumber() + "): " + message;
        System.out.println(result);
        return result;
    }

    private static String getThisData() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return sdf.format(date);
    }

}
