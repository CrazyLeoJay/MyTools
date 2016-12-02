package leojay.warehouse.database2.mysql;

import leojay.warehouse.database2.base.MyConfig;
import leojay.warehouse.database2.base.MyConnection;
import leojay.warehouse.tools.QLog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * package:leojay.warehouse.database2
 * project: MyTools
 * author:leojay
 * time:16/11/30__13:18
 */
class MySQLConnect implements MyConnection<Connection> {

    private MyConfig config;
    private static final String QLOG_KEY = "MySQLConnect.class";

    MySQLConnect(MyConfig config) {
        this.config = config;
    }

    @Override
    public void connect(OnConnectListener<Connection> listener) {
        if (config.isHaveConfig()) {
            try {
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
                    listener.onError("JDBC加载失败" + e.getMessage());
                } catch (SQLException e) {
                    QLog.e(this, "数据库链接失败,请检查数据库");
                    e.printStackTrace();
                    listener.onError("数据库链接失败,请检查数据库" + e.getMessage());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    listener.onError("JDBC加载失败" + e.getMessage());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                if (conn != null && !conn.isClosed()) {
                    listener.done(conn);
                    close(conn);
                }
            } catch (SQLException e) {
                QLog.e(this, QLOG_KEY, "读取数据库发生错误!" + e.getMessage());
                e.printStackTrace();
            }
        } else {
            QLog.e(this, QLOG_KEY, "未加载配置文件!");
        }
    }

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
