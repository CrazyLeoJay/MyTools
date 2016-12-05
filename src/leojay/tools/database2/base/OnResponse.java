package leojay.tools.database2.base;

/**
 * 这是一个数据库的操作和响应类
 * <p>
 * 这里要求使用时建立一个新的接口并继承于本接口：
 * <p>
 * 需要直接复制一下代码生成一个新的接口来使用！将方法<code>responseResult</code>参数根据实际情况修改
 * <PRE>
 * |    public interface OnResponseListener extends OnResponse {
 * |        //这是一个MySQL返回的示例
 * |        void responseResult(MyOperation.Mode mode, ResultSet resultSet, boolean b, int i) throws Exception;
 * |    }
 * </PRE>
 * 通过<code>toSQLInstruct</code>方法发送指令，也就是SQL语句。
 * <br>通过<code>onError</code>方法响应发生的错误。
 * <p>
 * time:16/12/3__00:25
 *
 * @author:leojay
 */
public interface OnResponse {
    /**
     * 发生的错误
     *
     * @param error 错误信息
     * @return 错误信息
     */
    String onError(String error);

    /**
     * SQL指令
     *
     * @return SQL语句
     * @throws Exception 发生异常的捕获，一般设置当sql语句为空时抛出异常
     */
    String toSQLInstruct() throws Exception;
}