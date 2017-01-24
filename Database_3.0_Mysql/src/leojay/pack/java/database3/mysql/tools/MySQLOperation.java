package leojay.pack.java.database3.mysql.tools;

import leojay.tools.java.QLog;
import leojay.tools.java.class_serialization.Args;
import leojay.tools.java.class_serialization.ClassArgs;
import leojay.tools.java.database3.base.DatabaseConnection;
import leojay.tools.java.database3.base.DatabaseOperation;
import leojay.tools.java.database3.base.SQLString;
import leojay.tools.java.database3.base.tools.ResultListener;
import leojay.tools.java.database3.base.tools.*;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * mysql数据库操作
 * <p>
 * time: 17/1/16__12:34
 *
 * @author leojay
 */
public final class MySQLOperation extends DatabaseOperation {
    private DatabaseConnection<Connection> connection;
    private MySQLString mySQLString;

    public MySQLOperation(DatabaseConnection<Connection> connection, DatabaseBase dbb) {
        super(dbb);
        this.connection = connection;
        this.mySQLString = new MySQLString(dbb);
    }

    /**
     * 产生一个数据库链接,将会根据设置的查询模式，
     * 返回相应的结果，具体看接口OnResponseListener
     *
     * @param mode     数据返回结果模式
     * @param listener 操作监听
     * @see OnResponseListener
     */
    public void SQLRequest(final Mode mode, final String sqlString,
                           final OnResponseListener<ResultSet> listener) {
        connection.createConnect(new DatabaseConnection.OnConnectListener<Connection>() {

            @Override
            public void onError(String error) {
                if (listener != null) {
                    QLog.e(this, "数据库链接失败！");
//                    listener.onError(error);
                    listener.responseResult(StateMode.CONNECTION_ERROR, null, false, 0);
                }
            }

            @Override
            public void done(Connection conn) throws SQLException {
//                QLog.i(this, QLOG_KEY, "即将执行的sql语句为: " + sql);
                StateMode stateMode = StateMode.NONE;
                ResultSet resultData = null;
                boolean b = false;
                int i = 0;
                if (conn != null) {
                    PreparedStatement ps = null;
                    ps = conn.prepareStatement(sqlString);
                    switch (mode) {
                        case COMMON:
                            b = ps.execute();
                            if (b) {
                                QLog.i(this, "_COMMON", "此次sql语句执行成功！并且有返回值！");
                                resultData = ps.getResultSet();
                            } else {
                                QLog.w(this, "_COMMON", "此次sql语句执行成功！但没有任何返回值");
                            }
                            stateMode = StateMode.SUCCESS;
                            break;
                        case UPDATE:
                            i = ps.executeUpdate();
                            QLog.i(this, "_UPDATE", "此次执行的SQL语句成功执行 " + i + " 个");
                            stateMode = StateMode.SUCCESS;
                            break;
                        case SELECT:
                            resultData = ps.executeQuery();
                            QLog.i(this, "_SELECT", "此次SQL语句执行有返回的值，使用 resultData 具体查看。");
                            stateMode = StateMode.SUCCESS;
                            break;
                        default:
                            stateMode = StateMode.WARN;
//                                throw new Exception("非正常Mode！");
                            break;
                    }
                } else {
                    stateMode = StateMode.ERROR;
                }
                listener.responseResult(stateMode, resultData, b, i);
            }
        });
    }

    @Override
    public void createTable(final ResultListener<Boolean> listener) {
        String createTableString = mySQLString.getSQL(SQLString.CMDMode.CREATE_TABLE);
        SQLRequest(Mode.UPDATE, createTableString, new OnResponseListener<ResultSet>() {
            @Override
            public void responseResult(StateMode mode, ResultSet resultData, boolean b, int i) {
                System.out.println(mode);
                if (mode == StateMode.SUCCESS) {
                    System.out.println("-----------------------------");
                    System.out.println(b + " >>> " + i);
                    if (resultData == null) {
                        System.out.println("resultData 为空");
                    } else {
                        try {
                            System.out.println(resultData.getString(0));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }

                listener.after(mode, b);
            }
        });
    }

    @Override
    public void deleteTable(final ResultListener<Boolean> listener) {
        String deleteTableString = mySQLString.getDeleteTableString();
        SQLRequest(Mode.COMMON, deleteTableString, new OnResponseListener<ResultSet>() {
            @Override
            public void responseResult(StateMode mode, ResultSet resultData, boolean b, int i) {
                System.out.println(mode);
                System.out.println("删除：" + i);
                listener.after(mode, b);
            }
        });
    }

    @Override
    public void writeData(final ResultListener<Integer> listener) {
        String insertString = mySQLString.getInsertString();
        SQLRequest(Mode.UPDATE, insertString, new OnResponseListener<ResultSet>() {
            @Override
            public void responseResult(StateMode mode, ResultSet resultData, boolean b, int i) {
                QLog.i(this, "写入……");
                listener.after(mode, i);
            }
        });
    }

    @Override
    public void deleteData(ResultListener<Integer> listener) {
        String deleteString = mySQLString.getDeleteString();
        SQLRequest(Mode.UPDATE, deleteString, new OnResponseListener<ResultSet>() {
            @Override
            public void responseResult(StateMode mode, ResultSet resultData, boolean b, int i) {

            }
        });
    }

    @Override
    public void updateData(final ResultListener<Integer> listener) {
        String updateString = mySQLString.getUpdateString();
        SQLRequest(Mode.UPDATE, updateString, new OnResponseListener<ResultSet>() {
            @Override
            public void responseResult(StateMode mode, ResultSet resultData, boolean b, int i) {
                listener.after(mode, i);
            }
        });
    }

    @Override
    public void selectData(SQLString.SelectMode mode, final ResultListener<List<DatabaseBase>> listener) {
        String selectString = mySQLString.getSelectString(mode);
        SQLRequest(Mode.SELECT, selectString, new OnResponseListener<ResultSet>() {
            @Override
            public void responseResult(StateMode mode, ResultSet resultData, boolean b, int i) {
                Object tableClass = getDatabaseBase().getTableClass();
                List<DatabaseBase> list = new ArrayList<DatabaseBase>();
                List<Args> classArgs = mySQLString.getClassArgs();
                try {
                    while (resultData.next()) {
                        Object o = ClassArgs.newInstance(getDatabaseBase().getTableClass().getClass());
                        DatabaseBase base = new DatabaseBase(o);
                        DatabaseDefaultArgs defaultArgs = base.getDefaultArgs();
                        for (Args item : classArgs) {
                            Object name = resultData.getObject(resultData.findColumn(item.getName()));
                            Field fie = o.getClass().getDeclaredField(item.getName());
                            fie.setAccessible(true);
                            fie.set(o, name);
                        }
                        defaultArgs.setUniqueId(resultData.getString(resultData.findColumn(DatabaseDefaultArgs.UNId_ARG)));
                        if (defaultArgs.isCreateTimeField()) {
                            defaultArgs.setCreateTime(resultData.getString(resultData.findColumn(DatabaseDefaultArgs.CREATE_TIME)));
                        }
                        if (defaultArgs.isUpdateTimeField()) {
                            defaultArgs.setUpdateTime(resultData.getString(resultData.findColumn(DatabaseDefaultArgs.UPDATE_TIME)));
                        }

                        list.add(base);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                listener.after(mode, list);
            }
        });
    }


    /**
     * 数据返回模式
     */
    public enum Mode {
        /**
         * 普通型，会返回布尔值
         */
        COMMON,
        /**
         * 查询模式，会返回一个查询类，需要具体情况具体判断
         */
        SELECT,
        /**
         * 更新模式，会返回一个整形，表示执行成功SQL语句的条数
         */
        UPDATE
    }
}
