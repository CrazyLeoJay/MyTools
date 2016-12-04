package leojay.tools.database2.mysql;

import leojay.tools.database2.base.MyOperation;
import leojay.tools.database2.base.OnResponse;

import java.sql.ResultSet;

/**
 * 数据库操作响应接口
 * <p>
 * time:16/12/3__00:26
 *
 * @author:leojay
 * @see leojay.tools.database2.base.OnResponse
 */
public interface OnResponseListener extends OnResponse {
    /**
     * 响应结果
     *
     * @param mode      执行模式
     * @param resultSet 查询模式结果
     * @param b         普通模式结果
     * @param i         更新模式结果
     * @throws Exception 在执行查询后，提取结果时会发生的异常
     */
    void responseResult(MyOperation.Mode mode, ResultSet resultSet, boolean b, int i) throws Exception;
}
