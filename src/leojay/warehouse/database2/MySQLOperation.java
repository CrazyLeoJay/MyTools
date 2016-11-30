package leojay.warehouse.database2;

import leojay.warehouse.tools.QLog;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * package:leojay.warehouse.database2<br/>
 * project: MyTools<br/>
 * author:leojay<br/>
 * time:16/11/30__13:41<br/>
 * </p>
 */
public class MySQLOperation<F extends DatabaseObject> extends MyOperation {
    private static final String QLOG_KEY = "MySQLOperation.class";

    //判断是否建立数据表
    private boolean isTab = false;
    //判断是否添加创建时间字段
    private boolean isCreateTimeField = true;
    //判断是否添加更新时间字段
    private boolean isUpdateTimeField = true;
    //数据字段
    public static final String UNId_ARG = "uniqueId";
    public static final String CREATE_TIME = "createTime";//创建时间
    public static final String UPDATE_TIME = "updateTime";//更新时间
    //数据库数据表列表
    private static List<String> sqlList = new ArrayList<String>();

    private F fClass;
    private Class<?> objectClass;

    //主键id模式
    public MySQLOperation(MyConnection connect, F fClass, Class<?> objectClass) {
        super(connect);
        this.fClass = fClass;
        this.objectClass = objectClass;
    }

    @Override
    void createTable() {
        final F f = fClass;
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
                        classArgs = getClassArgs(fClass.getClass(), objectClass);
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
                        create_table_sql += ",`" + CREATE_TIME + "` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP";
                    }
                    if (isUpdateTimeField) {
                        create_table_sql += ",`" + UPDATE_TIME + "` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ";
                    }
                    create_table_sql += ", " + "PRIMARY KEY (`" + UNId_ARG + "`)) " +
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

    }

    @Override
    public void deleteData() {

    }

    @Override
    public void selectData() {

    }

    @Override
    public void updateData() {

    }


    /**
     * 判断数据表是否存在(开启数据库未关闭)
     *
     * @param <T> 继承与DBListener 接口的类，或者直接继承本类
     * @param t   被操作数据表类
     */
    private <T extends DatabaseObject> boolean isTab(final T t) {
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
            type = "VARCHAR(255) NOT NULL default 'null'";
        } else if (type.equals("int") || type.equals("Integer")) {
            type = "int NOT NULL default '0'";
        }
        return type;
    }


    /**
     * 获取 sql 中的 id 字段部分
     */
    @Override
    String getIdSql(IDMode mode) {
        String result = " `" + UNId_ARG + "` VARCHAR(32) NOT NULL COMMENT '唯一ID' ";
        switch (mode) {
            case MODE_AUTO:
                result = "`" + UNId_ARG + "` INT NOT NULL AUTO_INCREMENT COMMENT '默认自动增长ID' ";
                break;
            case MODE_MY_ONLY:
                result = " `" + UNId_ARG + "` VARCHAR(32) NOT NULL COMMENT '自定义唯一ID' ";
                break;
            case MODE_CUSTOM:
                result = " `" + UNId_ARG + "` VARCHAR(32) NOT NULL COMMENT '自定义ID' ";
                break;
            default:
                break;
        }
        return result;
    }
}

