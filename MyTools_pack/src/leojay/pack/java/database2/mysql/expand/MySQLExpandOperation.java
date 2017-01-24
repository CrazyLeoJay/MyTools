package leojay.pack.java.database2.mysql.expand;

import leojay.pack.java.database2.mysql.MySQLOperation;
import leojay.pack.java.database2.mysql.OnResponseListener;
import leojay.tools.java.MyToolsException;
import leojay.tools.java.QLog;
import leojay.tools.java.class_serialization.Args;
import leojay.tools.java.class_serialization.ClassArgs;
import leojay.tools.java.database2.base.MyConnection;
import leojay.tools.java.database2.base.ReadWriteResultListener;
import leojay.tools.java.database2.base.SelectMode;
import leojay.tools.java.database2.expand.DBExpandObject;
import leojay.tools.java.database2.expand.DBExpandOperation;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static leojay.tools.java.database2.base.DatabaseBase.*;

/**
 * <p>
 * time: 17/1/14__16:16
 *
 * @author leojay
 */
public class MySQLExpandOperation<T> extends DBExpandOperation<T, OnResponseListener> {
    private static final String QLOG_KEY = "MySQLExpandOperation.class";
    private MySQLOperation<DBExpandObject<T>> operation;

    /**
     * 构造函数
     *
     * @param t           数据类，继承于本类
     * @param objectClass 基础类
     */
    public MySQLExpandOperation(MyConnection<Connection> connect, DBExpandObject<T> t, Class<?> objectClass) {
        super(connect, t, objectClass);
        operation = new MySQLOperation<DBExpandObject<T>>(connect, t, objectClass);
    }

    @Override
    public void SQLRequest(Mode mode, OnResponseListener listener) {
        operation.SQLRequest(mode, listener);
    }

    @Override
    public String getIdSql(IDMode mode) {
        return operation.getIdSql(mode);
    }

    @Override
    public void createTable(final ReadWriteResultListener listener) {
        boolean isTab = isTab(f);
//        3. 若 isTab = false, 则说明数据中没有该表, 则读取该类数据, 创建数据表
        if (!isTab) {
            QLog.i(this, QLOG_KEY, "数据库中,数据表 " + f.getTableName() + " 不存在");
            QLog.i(this, QLOG_KEY, "准备建立新表……");
            SQLRequest(Mode.COMMON, new OnResponseListener() {

                @Override
                public String onError(String error) {
                    if (listener != null) listener.onError(error);
                    return error;
                }

                @Override
                public String toSQLInstruct() {

                    List<Args> classArgs = null;
                    String result = "";
                    try {
                        classArgs = ClassArgs.getThisAndSupersClassArgs(f.getTableClass(), getObjectClass());
                        for (Args item : classArgs) {
                            String name = item.getName();
                            String type = operation.typeFilter(item.getType());
                            result += ", `" + name + "` " + type;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String create_table_sql = "CREATE TABLE `" + f.getTableName() + "` ( " +
                            getIdSql(getIdMode()) + result;
                    if (operation.isCreateTimeField) {
                        create_table_sql += ",`" + CREATE_TIME + "` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP";
                    }
                    if (operation.isUpdateTimeField) {
                        create_table_sql += ",`" + UPDATE_TIME + "` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ";
                    }
                    create_table_sql += ", " + "PRIMARY KEY (`" + UNId_ARG + "`)) " +
                            "ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci";
                    return create_table_sql;
                }

                @Override
                public void responseResult(Mode mode, ResultSet resultSet, boolean b, int i) {
                    QLog.i(this, QLOG_KEY, "创建数据表" + f.getTableName() + "成功, 是否有返回结果:" + b);
                    MySQLOperation.sqlList.add(f.getTableName());
                    if (listener != null) listener.onAfter();
                }
            });
        }
    }

    @Override
    public void deleteTable(ReadWriteResultListener listener) {
        operation.deleteData(listener);
    }

    @Override
    public void writeData(final ReadWriteResultListener listener) {
        switch (getIdMode()) {
            case MODE_AUTO:
                f.setUniqueId(null);
                break;
            case MODE_MY_ONLY:
                f.setUniqueId(getOnlyID());
                break;
            case MODE_CUSTOM:
                if (f.getUniqueId() == null) try {
                    throw new MyToolsException("在 MODE_CUSTOM 自定义模式下，必须手动设置主键 UniqueId ");
                } catch (MyToolsException e) {
                    QLog.e(this, QLOG_KEY, e.getMessage());
                    e.printStackTrace();
                }
                break;
            default:
                QLog.e(this, QLOG_KEY, "这种错误不可能发生！");
                break;
        }

        SQLRequest(Mode.COMMON, new OnResponseListener() {
            @Override
            public String onError(String error) {
                if (listener != null) listener.onError(error);
                return error;
            }

            @Override
            public String toSQLInstruct() throws Exception {
                String sql_item = "";
                String sql_value = "";
                List<Args> classArgs;
                classArgs = ClassArgs.getThisAndSupersClassArgs(f.getTableClass(), getObjectClass());
                int i = 2;
                int j = 0;
                List<Args> dataBufferd = new ArrayList<Args>();
                for (Args map : classArgs) {
                    String value = map.getValue();
                    if (value == null || value.equals("0") || value.equals("null")) {
                        i++;
                        continue;
                    }
                    dataBufferd.add(map);
                }
                for (Args map : dataBufferd){
                    String name = map.getName();
                    String value = map.getValue();
                    sql_item += "`" + name + "`";
                    sql_value += "'" + value + "'";
                    if (i < dataBufferd.size()) {
                        sql_item += ", ";
                        sql_value += ", ";
                    }
                    j++;
                    i++;
                }
                if (operation.isUpdateTimeField) {
                    sql_item += ", `" + UPDATE_TIME + "`";
                    sql_value += ", NOW()";
                }
                if (operation.isCreateTimeField) {
                    sql_item += ", `" + CREATE_TIME + "`";
                    sql_value += ", NOW()";
                }
                String write_sql = "INSERT INTO `" + f.getTableName() + "` " +
                        "(`" + UNId_ARG + "`, " + sql_item + ") " +
                        "VALUES " +
                        "(" + f.getUniqueId() + "," + sql_value + " );";
                QLog.i(this, QLOG_KEY, "测试输出语句：" + write_sql);
                if (j == 0) throw new Exception("该对象没有设置参数！！！数据库会插入空值");
                return write_sql;
            }

            @Override
            public void responseResult(Mode mode, ResultSet resultSet, boolean b, int i) throws SQLException {
                QLog.i(this, QLOG_KEY, "执行成功！");
                if (listener != null) listener.onAfter();
            }
        });
    }

    @Override
    public void deleteData(ReadWriteResultListener listener) {
        operation.deleteData(listener);
    }

    @Override
    public void selectData(final SelectMode mode, final OnResultListener<DBExpandObject<T>> listener) {
        SQLRequest(Mode.SELECT, new OnResponseListener() {
            @Override
            public String onError(String error) {
                return error;
            }

            @Override
            public String toSQLInstruct() throws Exception {
                String sql = "SELECT * FROM `" + f.getTableName() + "` WHERE ";
                String where = "";
                String way = " AND ";
                switch (mode) {
                    case ADD:
                        way = " AND ";
                        break;
                    case OR:
                        way = " OR ";
                        break;
                    default:
                        break;
                }
                int i = 0;
                if (f.getUniqueId() != null) {
                    where = "`" + UNId_ARG + "`='" + f.getUniqueId() + "' ";
                    i++;
                }
                List<Args> classArgs;
                classArgs = ClassArgs.getThisAndSupersClassArgs(f.getTableClass(), getObjectClass());

                for (Args map : classArgs) {
                    String value = map.getValue();
                    if (value != null && !value.equals("0")) {
                        if (i != 0) where += way;
                        where += " `" + map.getName() + "`='" + map.getValue() + "' ";
                        i++;
                    }
                }
                if (f.getCreateTime() != null) {
                    if (i != 0) where += way;
                    where += " `" + CREATE_TIME + "`='" + f.getCreateTime() + "' ";
                    i++;
                }
                if (f.getUpdateTime() != null) {
                    if (i != 0) where += way;
                    where += " `" + UPDATE_TIME + "`='" + f.getUpdateTime() + "' ";
                    i++;
                }
                if (i == 0) where = "1";
                return sql + where;
            }

            @Override
            public void responseResult(Mode mode, ResultSet resultSet, boolean b, int i) throws Exception {
                List<DBExpandObject<T>> result = new ArrayList<DBExpandObject<T>>();
                List<Args> classArgs;
                classArgs = ClassArgs.getThisAndSupersClassArgs(f.getTableClass(), getObjectClass());
                while (resultSet.next()) {
                    T fs;
                    fs = (T) Class.forName(f.getTableClass().getClass().getName()).newInstance();
                    for (Args item : classArgs) {
                        Object name = resultSet.getObject(resultSet.findColumn(item.getName()));
                        Field fie = fs.getClass().getDeclaredField(item.getName());
                        fie.setAccessible(true);
                        fie.set(fs, name);
                    }
                    DBExpandObject<T> object = new DBExpandObject<T>(fs);
                    object.setUniqueId(resultSet.getString(resultSet.findColumn(UNId_ARG)));
                    if (operation.isCreateTimeField)
                        object.setCreateTime(resultSet.getString(resultSet.findColumn(CREATE_TIME)));
                    if (operation.isUpdateTimeField)
                        object.setUpdateTime(resultSet.getString(resultSet.findColumn(UPDATE_TIME)));
                    result.add(object);
                }
                if (listener != null) listener.result(result);
            }
        });
    }

    public void selectData(final OnResultListener<DBExpandObject<T>> listener) {
        selectData(SelectMode.ADD, listener);
    }

    @Override
    public void updateData(final ReadWriteResultListener listener) {
        SQLRequest(Mode.UPDATE, new OnResponseListener() {
            @Override
            public String onError(String error) {
                if (listener != null) listener.onError(error);
                return null;
            }

            @Override
            public String toSQLInstruct() throws Exception {
                String sql_last = "";
                List<Args> classArgs;
                classArgs = ClassArgs.getThisAndSupersClassArgs(f.getTableClass(), getObjectClass());
                int i = 0;
                for (Args map : classArgs) {
                    String value = map.getValue();
                    if (value != null && !value.equals("0")) {
                        sql_last += "`" + map.getName() + "`='" + value + "', ";
                        i++;
                    }
                }
                String sql = "UPDATE `" + f.getTableName() + "` SET " + sql_last + "`" + UPDATE_TIME + "`= NOW() " +
                        "Where `" + UNId_ARG + "`='" + f.getUniqueId() + "';";
                QLog.i(this, QLOG_KEY, "即将执行的sql语句为: " + sql);
                return sql;
            }

            @Override
            public void responseResult(Mode mode, ResultSet resultSet, boolean b, int i) throws Exception {
                if (listener != null) listener.onAfter();
            }
        });
    }

    private <T extends DBExpandObject> boolean isTab(T t) {
        return operation.isTab(t);
    }

    public boolean isTab() {
        return operation.isTab(f);
    }

}
