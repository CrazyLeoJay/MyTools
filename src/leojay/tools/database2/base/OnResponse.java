package leojay.tools.database2.base;

/**
 * <p>
 * package:leojay.warehouse.database2.base<br>
 * project: MyTools<br>
 * author:leojay<br>
 * time:16/12/3__00:25<br>
 * </p>
 */
public interface OnResponse {

    String onError(String error);

    String toSQLInstruct() throws Exception;
}
