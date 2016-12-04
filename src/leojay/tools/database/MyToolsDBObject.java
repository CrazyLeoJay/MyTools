package leojay.tools.database;

import leojay.tools.MyToolsException;
import leojay.tools.QLog;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 这是一个数据库基础类，让一个类继承与它，在声明时会自动生成数据表，表的字段名为类的参数名。
 * 在这个类里，有增删改的方法，可以直接调用
 * <p>
 * package:leojay.warehouse.database
 * project: MyTools
 * author:leojay
 * time:16/10/11__15:14
 */
public abstract class MyToolsDBObject implements DBListener {

    //QLog 的key
    private static final String QLOG_KEY = "SQL_DB";
    //顶级前缀
    private String table_prefix = null;
    //二级前缀
    private String second_tab_prefix = null;

    //判断是否建立数据表
    private boolean isTab = false;
    //判断是否添加创建时间字段
    private boolean isCreateTimeField = true;
    //判断是否添加更新时间字段
    private boolean isUpdateTimeField = true;

    //数据表名称
    private String tableName = null;

    //数据表固定字段变量
    String uniqueId = null;//主键id
    //数据表可选字段变量
    String createTime = null;//创建时间
    String updateTime = null;//更新时间
    //数据字段
    public static final String UNId_ARG = "uniqueId";
    public static final String CREATE_TIME = "createTime";//创建时间
    public static final String UPDATE_TIME = "updateTime";//更新时间

    //主键id模式
    private IdMode MODE_ID = IdMode.MODE_AUTO;

    //基础类
    private Class objectClass = MyToolsDBObject.class;

    //数据库数据表列表
    private static List<String> sqlList = new ArrayList<String>();


    //数据库链接
    private DBSQLHelp connect;

    /**
     * 初始化
     */
    public interface InitListener {
        /**
         * @return 设置配置文件类
         */
        SQLConfig sqlConfig();

        /**
         * @return 设置一级前缀
         */
        String firstTabPrefix();

        /**
         * @return 设置二级前缀
         */
        String secondTabPrefix();

        /**
         * @return 是否添加创建时间字段
         */
        boolean isCreateTimeField();

        /**
         * @return 是否添加升级时间字段
         */
        boolean isUpdateTimeField();

        /**
         * @return 设置主键id模式
         */
        IdMode setIdMode();

        /**
         * @return 设置基类
         */
        Class setObjectClass();
    }

    private InitListener listener;

    /*
     * 构造函数
     * */
    protected MyToolsDBObject(InitListener listener) {
        this.listener = listener;
        //获取数据库连接
        connect = new DBSQLConnect(listener.sqlConfig());

        this.table_prefix = listener.firstTabPrefix();
        this.second_tab_prefix = listener.secondTabPrefix();
        if (listener.setIdMode() != null) {
            MODE_ID = listener.setIdMode();
        } else {
            MODE_ID = IdMode.MODE_AUTO;
        }
        setCreateTimeField(listener.isCreateTimeField());
        setUpdateTimeField(listener.isUpdateTimeField());
        if (listener.setObjectClass() != null) {
            this.setObjectClass(listener.setObjectClass());
        } else {
            this.setObjectClass(MyToolsDBObject.class);
        }
    }


    /**
     * 创建数据表
     * 在穿件表时会检查 isCreateTimeField 和 isUpdateTimeField 两个参数，判断是否添加创建时间和更新时间两个字段
     *
     * @param <T> 继承与DBListener 接口的类，或者直接继承本类
     * @param t   被操作数据表类
     */
    protected <T extends DBListener> void createTable(final T t) {
        //检查表是否存在
        isTab = isTab(t);
//        3. 若 isTab = false, 则说明数据中没有该表, 则读取该类数据, 创建数据表
        if (!isTab) {
            QLog.i(this, QLOG_KEY, "数据库中,数据表 " + t.getTableName() + " 不存在");
            QLog.i(this, QLOG_KEY, "准备建立新表……");
            connect.connect(new OnDBSQLInsertListener() {
                @Override
                public void onInsert(boolean b) {
                    QLog.i(this, QLOG_KEY, "创建数据表" + t.getTableName() + "成功, 是否有返回结果:" + b);
                    sqlList.add(t.getTableName());
                }

                @Override
                public String setSQLString() {
                    Class<?> aClass = t.getClass();
                    String names;
                    String sql_items = "";
                    do {
                        Field[] declaredFields = aClass.getDeclaredFields();
                        if (declaredFields.length > 0) {
                            String s = "";
                            for (Field f_item : declaredFields) {
                                f_item.setAccessible(false);
//                                System.out.println(f_item.getName());
                                String name = f_item.getName();
                                String type = typeFilter(f_item.getType().getSimpleName());
                                String sql_item = ", `" + name + "` " + type + " NULL ";
                                s += sql_item;
                            }
                            sql_items = s + sql_items;
                        }
                        aClass = aClass.getSuperclass();
                        names = aClass.getName();
                    } while (!names.equals(objectClass.getName()));
                    String create_table_sql = "CREATE TABLE `" + t.getTableName() + "` ( " +
                            getIdSql() +
                            sql_items;

                    if (isCreateTimeField) {
                        create_table_sql += ", `" + CREATE_TIME + "` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP";
                    }
                    if (isUpdateTimeField) {
                        create_table_sql += ", `" + UPDATE_TIME + "` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ";
                    }

                    create_table_sql += ", " + "PRIMARY KEY (`" + UNId_ARG + "`)) " +
                            "ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci";
                    QLog.i(this, QLOG_KEY, "即将执行SQL语句: " + create_table_sql);
                    return create_table_sql;
                }

                @Override
                public String onError(String error) {
                    QLog.e(this, QLOG_KEY, "创建数据表" + t.getTableName() + "失败, 错误:" + error);
                    return error;
                }
            });
        }
    }


    /**
     * 删除数据表
     *
     * @param <T> 继承与DBListener 接口的类，或者直接继承本类
     * @param t   被操作数据表类
     */
    protected <T extends MyToolsDBObject> void deleteTable(final T t) {
        if (isTab(t)) {
            QLog.i(this, QLOG_KEY, "准备删除数据表 " + t.getTableName());
            connect.connect(new OnDBSQLUpdateListener() {
                @Override
                public void onUpdate(int i) {
                    sqlList.remove(t.getTableName());
                    QLog.i(this, QLOG_KEY, "成功删除数据表 " + t.getTableName());
                }

                @Override
                public String setSQLString() {
                    return "DROP TABLE `" + t.getTableName() + "`;";
                }

                @Override
                public String onError(String error) {
                    QLog.i(this, QLOG_KEY, ("删除数据表 " + t.getTableName()) + "失败");
                    return null;
                }
            });
        } else {
            QLog.e(this, QLOG_KEY, "没有数据表！");
        }
    }

    /**
     * 更新字段
     * 只能添加字段而不能删除,若必须删除该字段，只能由管理员在数据库内将数据表备份，然后执行删除表和创建表命令
     *
     * @param <T> 继承与DBListener 接口的类，或者直接继承本类
     * @param t   被操作数据表类
     */
    protected <T extends MyToolsDBObject> void updateTable(final T t) {
        boolean isTab = isTab(t);
        if (isTab) {
            connect.connect(new OnDBSQLQueryListener() {
                @Override
                public void onQuery(ResultSet resultSet) throws SQLException {
                    boolean isNew = false;
                    List<Field> newList = new ArrayList<Field>();
                    List<String> nameList = new ArrayList<String>();
                    while (resultSet.next()) {
                        String field = resultSet.getString("field");
                        if (!field.equals(UNId_ARG) && !field.equals(UPDATE_TIME) && !field.equals(CREATE_TIME)) {
                            nameList.add(field);
                        }
                    }

                    Class<?> aClass = t.getClass();
                    String ns;
                    do {
                        boolean isSame = false;
                        for (Field f_item : aClass.getDeclaredFields()) {
                            for (String s_item : nameList) {
                                if (f_item.getName().equals(s_item)) {
                                    isSame = true;
                                    break;
                                }
                            }
                            if (isSame) {
                                isSame = false;
                                continue;
                            }
                            newList.add(f_item);
                        }
                        aClass = aClass.getSuperclass();
                        ns = aClass.getName();
                    } while (!ns.equals(objectClass.getName()));
                    if (newList.size() > 0) {
                        //构成sql语句
                        String update_tab_args = "";
                        int j = 0;
                        for (Field item : newList) {
                            String name = item.getName();
                            String type = typeFilter(item.getType().getSimpleName());
                            update_tab_args += " ADD `" + name + "` " + type + "";
                            j++;
                            if (j < newList.size()) {
                                update_tab_args += ", ";
                            }
                        }

//                        String s = "ALTER TABLE `il_c` ADD `dos` VARCHAR NOT NULL AFTER `c_1`, ADD `dash` VARCHAR NOT NULL AFTER `dos`, ADD `efasd` INT NOT NULL AFTER `dash`;";
                        final String update_tab = "ALTER TABLE `" + t.getTableName() + "` " +
                                update_tab_args + ";";
                        connect.connect(new OnDBSQLUpdateListener() {
                            @Override
                            public void onUpdate(int i) {
                                if (i > 0) {
                                    QLog.i(this, QLOG_KEY, "更新表成功,返回参数:" + i);
                                }
                            }

                            @Override
                            public String setSQLString() {
                                QLog.i(this, QLOG_KEY, "即将执行的sql语句" + update_tab);
                                return update_tab;
                            }

                            @Override
                            public String onError(String error) {
                                QLog.e(this, QLOG_KEY, "更新表发生错误:" + error);
                                return null;
                            }
                        });
                    } else {
                        QLog.e(this, QLOG_KEY, "数据表没有新字段!");
                    }
                }

                @Override
                public String setSQLString() {
                    return "SHOW COLUMNS FROM `" + t.getTableName() + "`;";
                }

                @Override
                public String onError(String error) {
                    QLog.e(this, QLOG_KEY, "读取数据库发生错误!" + error);
                    return null;
                }
            });
        } else {
            QLog.e(this, QLOG_KEY, "请先创造表!");
        }
    }


    /**
     * 写入数据
     *
     * @param listener 写入数据监听
     */
    public void writeData(final OnDBWriteListener listener) {
        switch (MODE_ID) {
            case MODE_AUTO:
                uniqueId = null;
                break;
            case MODE_MY_ONLY:
                uniqueId = getOnlyID();
                break;
            case MODE_CUSTOM:
                if (uniqueId == null) try {
                    throw new MyToolsException("在 MODE_CUSTOM 自定义模式下，必须手动设置主键 uniqueId");
                } catch (MyToolsException e) {
                    QLog.e(this, QLOG_KEY, e.getMessage());
                    e.printStackTrace();
                }
                break;
            default:
                QLog.e(this, QLOG_KEY, "这种错误不可能发生！");
                break;
        }

        String sql_item = "";
        String sql_value = "";

        Class<?> aClass = this.getClass();
        String sn;
        do {
            Field[] fields = aClass.getDeclaredFields();
            int i = 0;
            for (Field field : fields) {
                String name = field.getName();
                String value = null;
                field.setAccessible(true);
                try {
                    value = (String.valueOf(field.get(this)).isEmpty() ? "null" : field.get(this).toString());
                } catch (IllegalAccessException e) {
                    QLog.w(this, QLOG_KEY, "提取信息 " + name + "错误信息:" + e.getMessage());
                    e.printStackTrace();
                }
                sql_item += "`" + name + "`";
                sql_value += "'" + value + "'";
                if (i < fields.length - 1) {
                    sql_item += ", ";
                    sql_value += ", ";
                }
                i++;
            }
            aClass = aClass.getSuperclass();
            sn = aClass.getName();
            if (!sn.equals(objectClass.getName())) {
                sql_item += ", ";
                sql_value += ", ";
            }
        } while (!sn.equals(objectClass.getName()));

        final String write_sql = "INSERT INTO `" + this.getTableName() + "` (`" + UNId_ARG + "`, " + sql_item +
                ", " + CREATE_TIME + ", " + UPDATE_TIME + ") VALUES (" + uniqueId + "," + sql_value + ", NOW(), NOW());";

        connect.connect(new OnDBSQLUpdateListener() {
            @Override
            public void onUpdate(int i) {
                QLog.i(this, QLOG_KEY, "写入成功，信息：" + i);
                listener.result(i);
            }

            @Override
            public String setSQLString() {
                QLog.i(this, QLOG_KEY, "即将执行的sql语句: " + write_sql);
                return write_sql;
            }

            @Override
            public String onError(String error) {
                QLog.e(this, QLOG_KEY, "写入数据错误,错误信息为: " + error);
                return listener.onError(error);
            }
        });
    }

    /**
     * 删除数据
     *
     * @param listener 删除数据监听
     */
    public void deleteData(final OnDBDeleteListener listener) {
        if (!this.uniqueId.isEmpty()) {
            final String delete_sql = "DELETE FROM `" +
                    this.getTableName() + "` WHERE `" + UNId_ARG + "`='" + uniqueId + "';";
            connect.connect(new OnDBSQLUpdateListener() {
                @Override
                public void onUpdate(int i) {
                    listener.result(i);
                }

                @Override
                public String setSQLString() {
                    QLog.i(this, QLOG_KEY, "即将执行的sql语句: " + delete_sql);
                    return delete_sql;
                }

                @Override
                public String onError(String error) {
                    QLog.e(this, QLOG_KEY, "删除错误！信息：" + error);
                    return null;
                }
            });
        } else {
            QLog.e(this, QLOG_KEY, "没有选择主键！");
        }
    }

    /**
     * 更新数据
     *
     * @param listener 更新数据监听
     */
    public void updateData(final OnDBUpdateListener listener) {
        if (this.uniqueId != null) {
            String sql_last = "";
            Class<?> aClass = this.getClass();
            String ns;
            do {
                Field[] fields = aClass.getDeclaredFields();
                int i = 0;
                for (Field field : fields) {
                    field.setAccessible(true);
                    try {
                        sql_last += "`" + field.getName() + "`='" + field.get(this) + "',";
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    i++;
                }
                aClass = aClass.getSuperclass();
                ns = aClass.getName();
            } while (!ns.equals(objectClass.getName()));
            final String sql_l = sql_last;
            connect.connect(new OnDBSQLUpdateListener() {
                @Override
                public void onUpdate(int i) {
                    listener.result(i);
                }

                @Override
                public String setSQLString() {
                    String sql = "UPDATE `" + getTableName() + "` SET " + sql_l + "`" + UPDATE_TIME + "`= NOW() ;";
                    QLog.i(this, QLOG_KEY, "即将执行的sql语句为: " + sql);
                    return sql;
                }

                @Override
                public String onError(String error) {
                    QLog.e(this, QLOG_KEY, "更新错误！信息：" + error);
                    return null;
                }
            });
        } else {
            QLog.e(this, QLOG_KEY, "主键为空");
        }
    }



    /*
    * 私有方法
    * */

    /**
     * 判断数据表是否存在(开启数据库未关闭)
     *
     * @param <T> 继承与DBListener 接口的类，或者直接继承本类
     * @param t   被操作数据表类
     */
    private <T extends DBListener> boolean isTab(final T t) {
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
            connect.connect(new OnDBSQLQueryListener() {
                @Override
                public void onQuery(ResultSet resultSet) throws SQLException {
                    List<String> list = new ArrayList<String>();
                    while (resultSet.next()) {
                        list.add(resultSet.getString(1));
                    }
                    if (list.size() > 0) {
                        for (String item : list) {
                            if (t.getTableName().equals(item)) {
                                MyToolsDBObject.this.isTab = true;
                                sqlList.add(t.getTableName());
                                QLog.i(this, QLOG_KEY, "数据库中,数据表 " + t.getTableName() + " 存在");
                            }
                        }
                    }
                }

                @Override
                public String setSQLString() {
                    return "SHOW TABLES;";
                }

                @Override
                public String onError(String error) {
                    return error;
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
        } else if (type.equals("int")) {
            type = "int NOT NULL default '0'";
        } else if (type.equals("Integer")) {
            type = "int NOT NULL default '0'";
        }
        return type;
    }

    /**
     * 获取 sql 中的 id 字段部分
     */
    private String getIdSql() {
        String result = " `" + UNId_ARG + "` VARCHAR(32) NOT NULL COMMENT '唯一ID' ";
        switch (MODE_ID) {
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

    /*
    * 共有 set get 方法
    * */

    /**
     * 获取唯一id, 根据时间计算唯一id 方便移植
     * 唯一id组成: 秒数() +日期(6)+随机数()
     *
     * @return String 转换十六进制
     */
    private static String getOnlyID() {
        Date date = new Date();
        //秒
        String time = "";
        Long time1 = date.getTime();
        char[] chars = (time1 + "").toCharArray();
        for (int i = (chars.length - 5); i < chars.length; i++) {
            time += chars[i];
        }
        //毫秒
        String sss = (new SimpleDateFormat("SSS")).format(date);
        //日期
        String thisData = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        thisData = sdf.format(date);

        //转换36进制数并输出
        Long l = Long.parseLong(sss + time + thisData);
        return "'" + Long.toString(l, 36) + "'";
    }

    //获取数据的唯一ID
    public String getUniqueId() {
        return uniqueId;
    }

    //设置数据的唯一ID
    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    //获取数据表名
    public String getTableName() {
        if (tableName == null) {
            setTableName(this.getClass().getSimpleName());
        }
        return tableName.toLowerCase().trim();
    }

    //设置数据表名
    public void setTableName(String tableName) {
        if (table_prefix != null) {
            this.tableName = table_prefix + "_" + (second_tab_prefix == null ? "" : second_tab_prefix + "_") + tableName;
        } else {
            QLog.w(this, QLOG_KEY, "没有设置前缀名！！！");
            this.tableName = tableName;
        }
    }

    public DBSQLHelp getConnect() {
        return connect;
    }

    public boolean isCreateTimeField() {
        return isCreateTimeField;
    }

    public void setCreateTimeField(boolean createTimeField) {
        isCreateTimeField = createTimeField;
    }

    public boolean isUpdateTimeField() {
        return isUpdateTimeField;
    }

    public void setUpdateTimeField(boolean updateTimeField) {
        isUpdateTimeField = updateTimeField;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getTable_prefix() {
        return table_prefix;
    }

    public void setTable_prefix(String table_prefix) {
        this.table_prefix = table_prefix;
    }

    public String getSecond_tab_prefix() {
        return second_tab_prefix;
    }

    public void setSecond_tab_prefix(String second_tab_prefix) {
        this.second_tab_prefix = second_tab_prefix;
    }

//    public Class getObjectClass() {
//        return objectClass;
//    }

    /**
     * 设置这个继承类的基础父类，父类里声明的字段不会被写入数据表
     *
     * @param objectClass 想要设定的基础类，默认为MyToolsDBObject
     */
    public void setObjectClass(Class objectClass) {
        this.objectClass = objectClass;
    }
}
