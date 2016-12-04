package leojay.tools.database;

import leojay.tools.MyToolsException;
import leojay.tools.QLog;

import java.sql.*;

/**
 * 可以创建一个数据库连接，但它基于一个配置文件，通过 SQLConfig 类获取参数
 *
 * package:cn.ilinkerstudio.database
 * project: i-LinkerStudio
 * author:leojay
 * time:16/10/6__20:04
 */
public class DBSQLConnect implements DBSQLHelp {

    private static final String QLOG_KEY = "SQL_connect";
    private final SQLConfig config;

    public DBSQLConnect(SQLConfig config) {
        this.config = config;
//        config = new SQLConfig(configName);
    }

    private void connect(OnConnectListener listener) {
        if (SQLConfig.isHaveConfig()) {
            try {
                Connection conn = null;
                try {
                    //1.加载驱动程序
                    Class.forName(config.DB_Driver).newInstance();
                    //2. 获得数据库连接
                    conn = DriverManager.getConnection(config.DB_url,
                            config.USER_NAME, config.PASSWORD);
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

    /**
     * 输入数据
     * 监听返回布尔值
     */
    @Override
    public void connect(final OnDBSQLInsertListener listener){
                    final String sqlString = listener.setSQLString();
        EmptyException(sqlString);
        connect(new OnConnectListener() {
            @Override
            public void done(Connection connection) throws SQLException {
                if (connection != null) {
                    if (!sqlString.isEmpty()) {
                        PreparedStatement ps = connection.prepareStatement(sqlString);
                        boolean execute = ps.execute();
                        listener.onInsert(execute);
                    }
                }
            }

            @Override
            public String onError(String error) {
                return listener.onError(error);
            }
        });
    }

    /**
     * 更新数据
     * 监听返回整数，为成功执行sql语句的条数
     */
    @Override
    public void connect(final OnDBSQLUpdateListener listener) {
                    final String sqlString = listener.setSQLString();
        EmptyException(sqlString);
        connect(new OnConnectListener() {
            @Override
            public void done(Connection connection) throws SQLException {
                if (connection != null) {
                    if (!sqlString.isEmpty()) {
                        PreparedStatement ps = connection.prepareStatement(sqlString);
                        int i = ps.executeUpdate();
                        listener.onUpdate(i);
                    }
                }
            }

            @Override
            public String onError(String error) {
                return listener.onError(error);
            }
        });
    }

    /**
     * 查询数据
     * 监听返回  ResultSet
     */
    @Override
    public void connect(final OnDBSQLQueryListener listener) {
        final String sqlString = listener.setSQLString();
        EmptyException(sqlString);
        connect(new OnConnectListener() {
            @Override
            public void done(Connection connection) throws SQLException {
                if (connection != null) {
                    if (!sqlString.isEmpty()) {
                        PreparedStatement ps = connection.prepareStatement(sqlString);
                        ResultSet execute = ps.executeQuery();
                        listener.onQuery(execute);
                    }
                }
            }

            @Override
            public String onError(String error) {
                return listener.onError(error);
            }
        });
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

    interface OnConnectListener {
        void done(Connection connection) throws SQLException;

        String onError(String error);
    }

    private void EmptyException(String sql){
        if (sql == null || sql.equals("")) try {
            throw new MyToolsException("sql语句不能为空！！！");
        } catch (MyToolsException e) {
            QLog.e(this, QLOG_KEY, e.getMessage());
            e.printStackTrace();
        }
    }

}
