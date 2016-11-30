package leojay.warehouse.database2.base;

import java.sql.Connection;

/**
 * 获取数据库链接接口
 * <p>
 * package:leojay.warehouse.database2<br>
 * project: MyTools<br/>
 * author:leojay<br/>
 * time:16/11/29__19:21<br/>
 * </p>
 */
public interface MyConnection {
    void connect(OnConnectListener listener);

    interface OnConnectListener {

        void onError(String error);

        void done(Connection conn);

    }
}
