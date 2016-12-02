package leojay.warehouse.database2.base;

import java.sql.ResultSet;

/**
 * <p>
 * package:leojay.warehouse.database2.base<br/>
 * project: MyTools<br/>
 * author:leojay<br/>
 * time:16/12/3__00:25<br/>
 * </p>
 */
public interface OnResponse {

    String onError(String error);

    String toSQLInstruct() throws Exception;

    void responseResult(MyOperation.Mode mode, ResultSet resultSet, boolean b, int i) throws Exception;
}
