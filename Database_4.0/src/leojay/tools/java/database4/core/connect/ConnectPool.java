package leojay.tools.java.database4.core.connect;

import leojay.tools.java.database4.core.TableClass;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
    private C connect;

    public ConnectPool(int nChreads, C connect) {
        this.connect = connect;
        service = Executors.newFixedThreadPool(nChreads);
    }

    public <V, T extends TableClass> V submitOrder(Operation<C, T, V> operation) throws ExecutionException, InterruptedException {
        operation.setConnect(connect);
        Future<V> submit = service.submit(operation);
        return submit.get();
    }

    public void shutdown() {
        if (!service.isShutdown()) service.shutdown();
    }

    public C getConnect() {
        return connect;
    }
}
