package leojay.warehouse.database2.mysql;

import leojay.warehouse.database2.base.MyOperation;
import leojay.warehouse.database2.base.OnResponse;

import java.sql.ResultSet;

/**
 * <p>
 * package:leojay.warehouse.database2.mysql<br/>
 * project: MyTools<br/>
 * author:leojay<br/>
 * time:16/12/3__00:26<br/>
 * </p>
 */
interface OnResponseListener extends OnResponse {
    void responseResult(MyOperation.Mode mode, ResultSet resultSet, boolean b, int i) throws Exception;
}
