package leojay.tools.database2.mysql;

import leojay.tools.database2.base.DatabaseObject;
import leojay.tools.database2.base.MyConnection;
import leojay.tools.database2.base.MyOperation;
import leojay.tools.database2.base.SelectMode;
import leojay.tools.MyToolsException;
import leojay.tools.QLog;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * package:leojay.warehouse.database2<br>
 * project: MyTools<br>
 * author:leojay<br>
 * time:16/11/30__13:41<br>
 * </p>
 */
public class MySQLOperation<F extends DatabaseObject> extends MyOperation<F, OnResponseListener> {
    private static final String QLOG_KEY = "MySQLOperation.class";

    //判断是否建立数据表
    private boolean isTab = false;
    //判断是否添加创建时间字段
    private boolean isCreateTimeField = true;
    //判断是否添加更新时间字段
    private boolean isUpdateTimeField = true;
    //数据库数据表列表
    private static List<String> sqlList = new ArrayList<String>();

    private MyConnection<Connection> connect;

    MySQLOperation(MyConnection<Connection> connect, F f, Class<?> objectClass) {
        super(f, objectClass);
        this.connect = connect;
    }

    @Override
    public void SQLRequest(final Mode mode, final OnResponseListener listener) {
        connect.connect(new MyConnection.OnConnectListener<Connection>() {
            @Override
            public void onError(String error) {
                listener.onError(error);
            }

            @Override
            public void done(Connection conn) {
                try {
                    String sql = listener.toSQLInstruct();
                    QLog.i(this, QLOG_KEY, "即将执行的sql语句为: " + sql);
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ResultSet resultSet = null;
                    boolean b = false;
                    int i = 0;
                    switch (mode) {
                        case COMMON:
                            b = ps.execute();
                            if (b) {
                                QLog.i(this, QLOG_KEY + "_COMMON", "此次sql语句执行成功！并且有返回值！");
                                resultSet = ps.getResultSet();
                            } else {
                                QLog.w(this, QLOG_KEY + "_COMMON", "此次sql语句执行成功！但没有任何返回值");
                            }
                            break;
                        case UPDATE:
                            QLog.i(this, QLOG_KEY + "_UPDATE", "此次执行的SQL语句成功执行 " + i + " 个");
                            i = ps.executeUpdate();
                            break;
                        case SELECT:
                            QLog.i(this, QLOG_KEY + "_SELECT", "此次SQL语句执行有返回的值，使用 resultSet 具体查看。");
                            resultSet = ps.executeQuery();
                            break;
                        default:
                            throw new Exception("非正常Mode！");
                    }
                    listener.responseResult(mode, resultSet, b, i);
                } catch (SQLException e) {
                    QLog.w(this, QLOG_KEY, "发生数据库访问错误！:" + e.getMessage());
                    e.printStackTrace();
                    listener.onError(e.getMessage());
                } catch (Exception e) {
                    QLog.w(this, QLOG_KEY, "此次sql语句执行失败！:" + e.getMessage());
                    e.printStackTrace();
                    listener.onError(e.getMessage());
                }

            }
        });
    }


    @Override
    public void createTable() {
        isTab = isTab(f);
//        3. 若 isTab = false, 则说明数据中没有该表, 则读取该类数据, 创建数据表
        if (!isTab) {
            QLog.i(this, QLOG_KEY, "数据库中,数据表 " + f.getTableName() + " 不存在");
            QLog.i(this, QLOG_KEY, "准备建立新表……");
            SQLRequest(Mode.COMMON, new OnResponseListener() {

                @Override
                public String onError(String error) {
                    return error;
                }

                @Override
                public String toSQLInstruct() {

                    List<HashMap<String, String>> classArgs = null;
                    String result = "";
                    try {
                        classArgs = getClassArgs();
                        for (HashMap<String, String> item : classArgs) {
                            String name = item.get("name");
                            String type = typeFilter(item.get("type"));
                            result += ", `" + name + "` " + type;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String create_table_sql = "CREATE TABLE `" + f.getTableName() + "` ( " +
                            getIdSql(getIdMode()) + result;
                    if (isCreateTimeField) {
                        create_table_sql += ",`" + F.CREATE_TIME + "` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP";
                    }
                    if (isUpdateTimeField) {
                        create_table_sql += ",`" + F.UPDATE_TIME + "` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ";
                    }
                    create_table_sql += ", " + "PRIMARY KEY (`" + F.UNId_ARG + "`)) " +
                            "ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci";
                    return create_table_sql;
                }

                @Override
                public void responseResult(Mode mode, ResultSet resultSet, boolean b, int i) {
                    QLog.i(this, QLOG_KEY, "创建数据表" + f.getTableName() + "成功, 是否有返回结果:" + b);
                    sqlList.add(f.getTableName());
                }
            });
        }
    }

    @Override
    public void writeData() {
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
                return error;
            }

            @Override
            public String toSQLInstruct() throws Exception {
                String sql_item = "";
                String sql_value = "";
                List<HashMap<String, String>> classArgs = getClassArgs();
                int i = 0;
                int j = 0;
                for (HashMap<String, String> map : classArgs) {
                    String name = map.get("name");
                    String value = map.get("value");
                    if (value == null || value.equals("0") || value.equals("null")) {
                        i++;
                        continue;
                    }
                    sql_item += "`" + name + "`";
                    sql_value += "'" + value + "'";
                    if (i < map.size()) {
                        sql_item += ", ";
                        sql_value += ", ";
                    }
                    j++;
                    i++;
                }

                String write_sql = "INSERT INTO `" + f.getTableName() + "` (`" + F.UNId_ARG + "`, " + sql_item +
                        "`" + F.CREATE_TIME + "`, `" + F.UPDATE_TIME + "`) VALUES (" + f.getUniqueId() + "," + sql_value + "NOW(), NOW());";
                QLog.i(this, QLOG_KEY, "测试输出语句：" + write_sql);
                if (j == 0) throw new Exception("该对象没有设置参数！！！数据库会插入空值");
                return write_sql;
            }

            @Override
            public void responseResult(Mode mode, ResultSet resultSet, boolean b, int i) throws SQLException {

            }
        });
    }

    @Override
    public void deleteData() {
        if (!this.f.getUniqueId().isEmpty()) {
            SQLRequest(Mode.UPDATE, new OnResponseListener() {
                @Override
                public String onError(String error) {
                    return error;
                }

                @Override
                public String toSQLInstruct() throws Exception {
                    return "DELETE FROM `" + f.getTableName() + "` WHERE `" + F.UNId_ARG + "`='" + f.getUniqueId() + "';";
                }

                @Override
                public void responseResult(Mode mode, ResultSet resultSet, boolean b, int i) throws SQLException {
                    QLog.i(this, QLOG_KEY, "共删掉了 " + i + " 条数据！");
                }
            });
        } else {
            QLog.w(this, QLOG_KEY, "警告！该删除对象没有指定主键！");
        }
    }

    /**
     * 查询数据
     * 当未设置该对象的参数值，则查询该对象所对应数据表的所有数据
     * 当设置参数后会精确查询所有符合的值
     */
    @Override
    public void selectData(final SelectMode mode, final OnResultListener<F> listener) {
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
                    where = "`" + F.UNId_ARG + "`='" + f.getUniqueId() + "' ";
                    i++;
                }
                List<HashMap<String, String>> classArgs = getClassArgs();
                for (HashMap<String, String> map : classArgs) {
                    String value = map.get("value");
                    if (value != null && !value.equals("0")) {
                        if (i != 0) where += way;
                        where += " `" + map.get("name") + "`='" + map.get("value") + "' ";
                        i++;
                    }
                }
                if (f.getCreateTime() != null) {
                    if (i != 0) where += way;
                    where += " `" + F.CREATE_TIME + "`='" + f.getCreateTime() + "' ";
                    i++;
                }
                if (f.getUpdateTime() != null) {
                    if (i != 0) where += way;
                    where += " `" + F.UPDATE_TIME + "`='" + f.getUpdateTime() + "' ";
                    i++;
                }
                if (i == 0) where = "1";
                return sql + where;
            }

            @Override
            public void responseResult(Mode mode, ResultSet resultSet, boolean b, int i) throws Exception {
                List<F> result = new ArrayList<F>();
                List<HashMap<String, String>> classArgs = getClassArgs();
                while (resultSet.next()) {
                    F fs;
                    fs = (F) Class.forName(f.getClass().getName()).newInstance();
                    for (HashMap<String, String> item : classArgs) {
                        String name = resultSet.getString(resultSet.findColumn(item.get("name")));
                        Field fie = fs.getClass().getDeclaredField(item.get("name"));
                        fie.setAccessible(true);
                        fie.set(fs, name);
                    }
                    result.add(fs);
                }
                listener.result(result);
            }
        });
    }

    public void selectData(final OnResultListener<F> listener) {
        selectData(SelectMode.ADD, listener);
    }

    @Override
    public void updateData() {

        SQLRequest(Mode.UPDATE, new OnResponseListener() {
            @Override
            public String onError(String error) {
                return null;
            }

            @Override
            public String toSQLInstruct() throws Exception {
                String sql_last = "";
                List<HashMap<String, String>> classArgs = getClassArgs();
                int i = 0;
                for (HashMap<String, String> map : classArgs) {
                    String value = map.get("value");
                    if (value != null && !value.equals("0")) {
                        sql_last += "`" + map.get("name") + "`='" + value + "', ";
                        i++;
                    }
                }
                String sql = "UPDATE `" + f.getTableName() + "` SET " + sql_last + "`" + F.UPDATE_TIME + "`= NOW() ;";
                QLog.i(this, QLOG_KEY, "即将执行的sql语句为: " + sql);
                return null;
            }

            @Override
            public void responseResult(Mode mode, ResultSet resultSet, boolean b, int i) throws Exception {

            }
        });
    }

    /**
     * 判断数据表是否存在(开启数据库未关闭)
     *
     * @param <T> 继承与DBListener 接口的类，或者直接继承本类
     * @param t   被操作数据表类
     */
    private <T extends F> boolean isTab(final T t) {
        //查看是否设定数据表名称, 若无,则以类名表述
        if (t.getTableName() == null) {
            t.setTableName(t.getClass().getSimpleName().toLowerCase());
        }
        isTab = false;
//        1. 查询系统数据表列表 sqlList, 检查是否记录该表如若表存在, 则返回参数 isTab = true;
        if (sqlList.size() > 0) {
            for (String s : sqlList) {
                if (t.getTableName().equals(s)) {
                    isTab = true;
                    break;
                }
            }
        }
//        2. 若 isTab = false, 则说明数据表无记录, 读取数据库数据表列表, 查看是否有该表.若有, 则令 isTab = true;
        //打开数据库连接
        if (isTab) {
            QLog.i(this, QLOG_KEY, "系统记录中,数据表 " + t.getTableName() + " 存在");
        } else {
            QLog.i(this, QLOG_KEY, "系统记录中,数据表 " + t.getTableName() + " 不存在");
            QLog.i(this, QLOG_KEY, "查询数据库中是否存在");
            this.SQLRequest(Mode.SELECT, new OnResponseListener() {
                @Override
                public String onError(String error) {
                    return error;
                }

                @Override
                public String toSQLInstruct() {
                    return "SHOW TABLES;";
                }

                @Override
                public void responseResult(Mode mode, ResultSet resultSet, boolean b, int i) throws SQLException {
                    List<String> list = new ArrayList<String>();
                    while (resultSet.next()) {
                        list.add(resultSet.getString(1));
                    }
                    if (list.size() > 0) {
                        for (String item : list) {
                            if (t.getTableName().equals(item)) {
                                MySQLOperation.this.isTab = true;
                                sqlList.add(t.getTableName());
                                QLog.i(this, QLOG_KEY, "数据库中,数据表 " + t.getTableName() + " 存在");
                            }
                        }
                    }
                }
            });
        }
        return isTab;
    }

    /**
     * 获取 sql 中的 属性 字段部分
     */
    private String typeFilter(String type) {
        if (type.equals("string") || type.equals("String")) {
            type = "VARCHAR(255) default 'null'";
        } else if (type.equals("int") || type.equals("Integer")) {
            type = "int default '0'";
        }
        return type;
    }

    /**
     * 获取 sql 中的 id 字段部分
     */
    @Override
    protected String getIdSql(IDMode mode) {
        String result = " `" + F.UNId_ARG + "` VARCHAR(32) NOT NULL COMMENT '唯一ID' ";
        switch (mode) {
            case MODE_AUTO:
                result = "`" + F.UNId_ARG + "` INT NOT NULL AUTO_INCREMENT COMMENT '默认自动增长ID' ";
                break;
            case MODE_MY_ONLY:
                result = " `" + F.UNId_ARG + "` VARCHAR(32) NOT NULL COMMENT '自定义唯一ID' ";
                break;
            case MODE_CUSTOM:
                result = " `" + F.UNId_ARG + "` VARCHAR(32) NOT NULL COMMENT '自定义ID' ";
                break;
            default:
                break;
        }
        return result;
    }

}
