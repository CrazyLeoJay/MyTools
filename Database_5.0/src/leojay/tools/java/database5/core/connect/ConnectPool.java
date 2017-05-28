package leojay.tools.java.database5.core.connect;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

/**
 * 连接池
 * <p>
 * time: 17/3/10__16:29
 *
 * @param <C> 连接对象
 * @author leojay
 */
public class ConnectPool<C> {
    private ExecutorService service;
    private ConnectManager<C> connectManager;

    public ConnectPool(int nThreads, ConnectManager<C> connectManager) {
        this.connectManager = connectManager;
        service = Executors.newFixedThreadPool(nThreads);
    }

    public <V> V submitOrder(Operation<C, V> operation) throws ExecutionException, InterruptedException {
        operation.setConnectManager(connectManager);
        Future<V> submit = service.submit(operation);
        return submit.get();
    }

    Logger logger = Logger.getLogger(this.getClass().getName());
    public <V> V submitOrderNoException(Operation<C, V> operation) {
        V v = null;
        try {
            v = submitOrder(operation);
        } catch (ExecutionException e) {
            logger.warning("数据库读写错误：" + e.getMessage());
//            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return v;
    }

    public void shutdown() {
        if (!service.isShutdown()) service.shutdown();
    }

    public ConnectManager<C> getConnectManager() {
        return connectManager;
    }
}
