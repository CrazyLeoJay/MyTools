package leojay.pack.java.database3.mysql.tools;

import leojay.tools.java.QLog;
import leojay.tools.java.database3.base.DatabaseConfig;
import leojay.tools.java.database3.base.DatabaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * <p>
 * time: 17/1/20__12:16
 *
 * @author leojay
 */
public final class MySQLConnection implements DatabaseConnection<Connection> {
    private static final String QLOG_KEY = "mysqlConnection.class";
    private DatabaseConfig config;

    public MySQLConnection(DatabaseConfig config) {
        this.config = config;
    }

    @Override
    public void createConnect(OnConnectListener<Connection> listener) {
        if (config.isHaveConfig()) {
            Connection conn = null;
            try {
                //1.加载驱动程序
                Class.forName(config.getDB_Driver()).newInstance();
                //2. 获得数据库连接
                conn = DriverManager.getConnection(config.getDB_url(),
                        config.getUSER_NAME(), config.getPASSWORD());
            } catch (ClassNotFoundException e) {
                QLog.e(this, "JDBC加载失败");
                e.printStackTrace();
                listener.onError("JDBC加载失败 :" + e.getMessage());
            } catch (SQLException e) {
                String s = "数据库链接失败,请检查数据库";
                QLog.e(this, s + " : " + e.getMessage());
                e.printStackTrace();
                listener.onError(s + e.getMessage());
            } catch (InstantiationException e) {
                QLog.e(this, "JDBC加载失败: " + e.getMessage());
                e.printStackTrace();
                listener.onError("JDBC加载失败" + e.getMessage());
            } catch (IllegalAccessException e) {
                QLog.e(this, "权限问题：" + e.getMessage());
                e.printStackTrace();
                listener.onError("权限问题" + e.getMessage());
            }

            try {
                if (conn != null && !conn.isClosed()) {
                    try {
                        listener.done(conn);
                    } catch (SQLException e) {
                        if (e.getMessage().equals("Table 'atest' already exists")) {
                            QLog.i(this, "数据表已存在！");
                        } else {
                            QLog.e(this, QLOG_KEY, "执行发生错误!" + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    close(conn);
                }
            } catch (SQLException e) {
                String s = "无法判断是否关闭数据库链接，错误信息 -->";
                QLog.e(this, s + e.getMessage());
                e.printStackTrace();
                listener.onError(s);
            }
        } else {
            QLog.e(this, QLOG_KEY, "未加载配置文件!");
        }
    }

    /**
     * 关闭数据库链接
     *
     * @param conn 要关闭的链接
     */
    private void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (!conn.isClosed()) {
                        System.out.println("数据库未关闭 ……尝再次关闭");
                        close(conn);
                    } else {
                        conn = null;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
