package leojay.tools.java.database5.core.connect;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

/**
 * Created by CrazyLeoJay on 2017/5/26.
 */
public interface ConnectManager<C> {
    C getConnect() throws ExecutionException, InterruptedException, SQLException;
    void close();
}
