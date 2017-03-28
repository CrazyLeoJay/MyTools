package leojay.tools.java.sqlconnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.*;

/**
 * <p>
 * time: 17/3/10__16:37
 *
 * @author leojay
 */
public class UsuallySQLConnect {

    private UsuallySQLConnect() {
    }

    public static Connection getConnect(Config config) throws ExecutionException, InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(1);
        Future<Connection> submit = service.submit(new ConnectCallable(config));
        service.shutdown();
        return submit.get();
    }

    private static class ConnectCallable implements Callable<Connection> {
        Config config;

        public ConnectCallable(Config config) {
            this.config = config;
        }

        @Override
        public Connection call() throws Exception {
            Connection conn = null;
            //1.加载驱动程序
            Class.forName(config.getDB_Driver()).newInstance();
            //2. 获得数据库连接
            conn = DriverManager.getConnection(config.getDB_url(),
                    config.getUSER_NAME(), config.getPASSWORD());
            return conn;
        }


    }

    private static int i = 0;

    /**
     * 关闭数据库链接
     *
     * @param conn 要关闭的链接
     */
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                i = 0;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (i < 3) try {
                    if (!conn.isClosed()) {
                        i++;
                        System.out.println("数据库未关闭 ……尝再次关闭");
                        close(conn);
                    } else {
                        conn = null;
                    }
                } catch (SQLException e) {
                    System.out.println("关闭异常");
                    e.printStackTrace();
                }
            }
        }
    }
}
