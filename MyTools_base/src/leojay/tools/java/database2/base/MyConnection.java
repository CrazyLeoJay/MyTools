package leojay.tools.java.database2.base;

/**
 * 获取数据库链接接口
 * <p>
 * 该接口要求实现一个方法，通过子接口返回一个链接
 * <p>
 * time:16/11/29__19:21
 *
 * @param <F> 用来操作数据库的类
 * @author leojay
 */
public interface MyConnection<F> {
    /**
     * 用来实现与数据库的交互,一般要求在使用完 链接类 后，还需关闭链接类
     * <p>
     * 具体使用方式可参照
     * leojay.tools.database2.mysql.MySQLOperation类的
     * SQLRequest(MyOperation.Mode, OnResponse)方法，如下连接
     *
     * @param listener 链接监听
     */
    void connect(OnConnectListener<F> listener);

    /**
     * 这个子接口表示在以获得<B>链接类</B>后的操作。
     * 并在 done方法中写出具体实现
     *
     * @param <F> 链接类
     */
    interface OnConnectListener<F> {
        /**
         * 发生错误后调用
         *
         * @param error 错误信息
         */
        void onError(String error);

        /**
         * 数据库操作的具体实现
         *
         * @param conn 链接类
         * @throws Exception 读写操作异常捕获
         */
        void done(F conn) throws Exception;

    }
}
