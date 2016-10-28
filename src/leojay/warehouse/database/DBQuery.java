package leojay.warehouse.database;

import leojay.warehouse.tools.QLog;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询类，用于查询继承于DBObject类的数据表
 *
 * package:pre.cl.quicksend.database
 * project: Quicksend
 * author:leojay
 * time:16/9/3__13:54
 */
public class DBQuery<T extends MyToolsDBObject> {
    T t;
    DBSQLHelp connect;
    public static final String QLog_KEY = "select class";

    public DBQuery(T t) {
        this.t = t;
        connect = t.getConnect();
    }

    /**
     * 读取数据
     *
     * @param mode     读取方式
     * @param listener 结果监听
     */
    public void readData(MODE mode, DBReadListener<T> listener) {
        try {
            readData(mode, null, listener);
        } catch (Throwable e) {
            QLog.e(this, QLog_KEY, "错误信息:" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 读取数据
     *
     * @param mode     读取方式
     * @param where    条件
     * @param listener 结果监听
     */
    public void readData(MODE mode, String where, DBReadListener<T> listener) {
        String sql = "select * from `" + t.getTableName() + "` ";
        switch (mode) {
            case Single:
                sql += "where `" + t.UNId_ARG + "`='" + t.uniqueId + "';";
                selectDB(sql, listener);
                break;
            case Condition:
                if (where == null) {
                    try {
                        throw new Exception("Condition 模式下,必须输入where值");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    sql += "where " + where + ";";
                    selectDB(sql, listener);
                }
                break;
            case All:
                selectDB(sql, listener);
                break;
            default:
                try {
                    throw new Exception("选择模式错误!!");
                } catch (Exception e) {
                    QLog.e(this, QLog_KEY, "模式错误!!");
                    e.printStackTrace();
                }
        }
    }

    /**
     * 根据sql语句查询数据库
     *
     * @param select_sql sql语句
     * @param listener   结果监听
     */
    private void selectDB(final String select_sql, final DBReadListener<T> listener) {
        QLog.i(this, "即将执行的sql语句:" + select_sql);
        connect.connect(new OnDBSQLQueryListener() {
            @Override
            public void onQuery(ResultSet resultSet) throws SQLException {
                try {
                    List<T> data = new ArrayList<T>();
                    while (resultSet.next()) {
                        T t2 = (T) t.getClass().newInstance();
                        Field[] fields = t2.getClass().getDeclaredFields();
                        for (Field f : fields) {
                            f.setAccessible(true);
                            String type = f.getType().getSimpleName();
                            if (type.equals("int") || type.equals("Integer")) {
                                f.set(t2, resultSet.getInt(f.getName()));
                            } else if (type.equals("String")) {
                                f.set(t2, resultSet.getString(f.getName()));
                            }
                        }
                        t2.uniqueId = resultSet.getString(MyToolsDBObject.UNId_ARG);
                        t2.createTime = resultSet.getString(MyToolsDBObject.CREATE_TIME);
                        t2.updateTime = resultSet.getString(MyToolsDBObject.UPDATE_TIME);
                        data.add(t2);
                    }
                    listener.result(data);
                } catch (InstantiationException e) {
                    QLog.e(this, QLog_KEY, "实例化对象错误,信息:" + e.getMessage());
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    QLog.e(this, QLog_KEY, "非法访问异常,信息:" + e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public String setSQLString() {
                return select_sql;
            }

            @Override
            public String onError(String error) {
                QLog.e(this, QLog_KEY, "查询错误,错误信息:" + error);
                listener.onError(error);
                return null;
            }
        });
    }

    /**
     * 查询模式
     */
    public enum MODE {
        Single,//单条记录查询
        Condition, //条件查询
        All//所有查询
    }
}
