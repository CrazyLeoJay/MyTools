package leojay.warehouse.database2.base;

/**
 * 获取数据库链接接口
 * <p>
 * package:leojay.warehouse.database2<br>
 * project: MyTools<br/>
 * author:leojay<br/>
 * time:16/11/29__19:21<br/>
 * </p>
 */
public interface MyConnection<F> {
    void connect(OnConnectListener<F> listener);

    interface  OnConnectListener<F> {

        void onError(String error);

        void done(F conn);

    }
}
