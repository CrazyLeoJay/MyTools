package leojay.tools.java.database5.core.connect;

import java.util.concurrent.Callable;

/**
 * <p>
 * time: 17/2/27__20:19
 *
 * @param <C> 连接类
 * @param <R> 返回类
 * @author leojay
 */
public abstract class Operation<C, R> implements Callable<R> {
    private ConnectManager<C> connectManager;

    protected ConnectManager<C> getConnectManager() {
        return connectManager;
    }

    public void setConnectManager(ConnectManager<C> connectManager) {
        this.connectManager = connectManager;
    }

}
