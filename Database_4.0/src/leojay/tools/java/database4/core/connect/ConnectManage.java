package leojay.tools.java.database4.core.connect;

/**
 * 数据库连接管理
 * <p>
 * time: 17/2/27__19:34
 *
 * @author leojay
 */
public abstract class ConnectManage<T> {

//    public ConnectPool createConnectionPool(int nThreads) {
//        return new ConnectPool(nThreads);
//    }

    protected abstract T createConnect();

    protected abstract void close(T conn);

}
