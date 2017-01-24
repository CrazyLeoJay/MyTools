package leojay.pack.java.database3.mysql.tools;

import leojay.tools.java.MyToolsException;
import leojay.tools.java.QLog;
import leojay.tools.java.class_serialization.Args;
import leojay.tools.java.database3.base.tools.DatabaseBase;
import leojay.tools.java.database3.base.tools.DatabaseDefaultArgs;
import leojay.tools.java.database3.base.tools.IDMode;
import leojay.tools.java.database3.base.SQLString;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static leojay.tools.java.database3.base.tools.DatabaseDefaultArgs.UNId_ARG;
import static leojay.tools.java.database3.base.tools.DatabaseDefaultArgs.UPDATE_TIME;

/**
 * <p>
 * time: 17/1/16__22:45
 *
 * @author leojay
 */
public final class MySQLString extends SQLString {
    private static final String QLOG_KEY = "MySqlString.class";
    private final DatabaseBase tableObject;
    private final DatabaseDefaultArgs defaultArgs;

    public MySQLString(DatabaseBase tableObject) {
        super(tableObject);
        this.tableObject = tableObject;
        this.defaultArgs = tableObject.getDefaultArgs();
    }

    @Override
    protected String getIdSql(IDMode mode) {
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

    @Override
    protected String getCreateTableString() {
        String result = "";
        List<Args> args = getClassArgs();
        for (Args item : args) {
            String name = item.getName();
            String type = typeFilter(item.getType());
            result += ", `" + name + "` " + type;
        }
        String create_table_sql = "CREATE TABLE `" + tableObject.getTableName() + "` ( " +
                getIdSql(getIdMode()) + result;
        if (tableObject.getDefaultArgs().isCreateTimeField()) {
            create_table_sql += ",`" + DatabaseDefaultArgs.CREATE_TIME + "` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP";
        }
        if (tableObject.getDefaultArgs().isUpdateTimeField()) {
            create_table_sql += ",`" + UPDATE_TIME + "` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ";
        }
        create_table_sql += ", " + "PRIMARY KEY (`" + UNId_ARG + "`)) " +
                "ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci";
        QLog.i(this, "SQL语句 ", create_table_sql);
        return create_table_sql;
    }

    @Override
    protected String getDeleteTableString() {
        String s = "DROP TABLE " + tableObject.getTableName() + " ;";
        return s;
    }

    @Override
    protected String getInsertString() {
        DatabaseDefaultArgs defaultArgs = tableObject.getDefaultArgs();
        switch (getIdMode()) {
            case MODE_AUTO:
                defaultArgs.setUniqueId(null);
                break;
            case MODE_MY_ONLY:
                defaultArgs.setUniqueId(getOnlyID());
                break;
            case MODE_CUSTOM:
                if (defaultArgs.getUniqueId() == null) try {
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


        String sql_item = "";
        String sql_value = "";
        List<Args> classArgs = getClassArgs();
        int i = 0;
        int j = 0;
        List<Args> dataBufferd = new ArrayList<Args>();
        for (Args args : classArgs) {
            String value = args.getValue();
            if (value == null || value.equals("0") || value.equals("null")) {
//                i++;
                continue;
            }
            dataBufferd.add(args);
        }
        for (Args args : dataBufferd) {
            String name = args.getName();
            String value = args.getValue();
            sql_item += "`" + name + "`";
            sql_value += "'" + value + "'";
            if (i < dataBufferd.size() - 1) {
                sql_item += ", ";
                sql_value += ", ";
            }
            j++;
            i++;
        }
        if (j == 0) {
            QLog.e(this, "没有需要输入的数据，返回空值");
            return null;
        }
        if (tableObject.getDefaultArgs().isUpdateTimeField()) {
            sql_item += ", `" + DatabaseDefaultArgs.UPDATE_TIME + "`";
            sql_value += ", NOW()";
        }
        if (tableObject.getDefaultArgs().isCreateTimeField()) {
            sql_item += ", `" + DatabaseDefaultArgs.CREATE_TIME + "`";
            sql_value += ", NOW()";
        }

        String write_sql = "INSERT INTO `" + tableObject.getTableName() + "` " +
                "(`" + DatabaseDefaultArgs.UNId_ARG + "`, " + sql_item + ") " +
                "VALUES " +
                "(" + defaultArgs.getUniqueId() + "," + sql_value + " );";
        QLog.i(this, QLOG_KEY, "测试输出语句：" + write_sql);
        return write_sql;
    }

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

    @Override
    protected String getDeleteString() {
        if (tableObject.getDefaultArgs().getUniqueId() == null) return null;
        String sql = "DELETE FROM `" + tableObject.getTableName() +
                "` WHERE `" + DatabaseDefaultArgs.UNId_ARG + "`='" + tableObject.getDefaultArgs().getUniqueId() + "';";
        QLog.i(this, "SQL语句" + sql);
        return sql;
    }


    @Override
    protected String getSelectString(SelectMode mode) {
        String sql = "SELECT * FROM `" + tableObject.getTableName() + "` WHERE ";
        String where = "";
        String way = " AND ";
        switch (mode) {
            case AND:
                way = " AND ";
                break;
            case OR:
                way = " OR ";
                break;
            default:
                break;
        }
        int i = 0;
        if (tableObject.getDefaultArgs().getUniqueId() != null) {
            where = "`" + DatabaseDefaultArgs.UNId_ARG + "`='" + tableObject.getDefaultArgs().getUniqueId() + "' ";
            i++;
        }
        List<Args> classArgs = getClassArgs();
        for (Args args : classArgs) {
            String value = args.getValue();
            if (value != null && !value.equals("0")) {
                if (i != 0) where += way;
                where += " `" + args.getName() + "`='" + args.getValue() + "' ";
                i++;
            }
        }
        if (tableObject.getDefaultArgs().getCreateTime() != null) {
            if (i != 0) where += way;
            where += " `" + DatabaseDefaultArgs.CREATE_TIME + "`='" + tableObject.getDefaultArgs().getCreateTime() + "' ";
            i++;
        }
        if (tableObject.getDefaultArgs().getUpdateTime() != null) {
            if (i != 0) where += way;
            where += " `" + DatabaseDefaultArgs.UPDATE_TIME + "`='" + tableObject.getDefaultArgs().getUpdateTime() + "' ";
            i++;
        }
        if (i == 0) where = "1";
        return sql + where;
    }

    @Override
    protected String getUpdateString() {
        if (tableObject.getDefaultArgs().getUniqueId() == null) return null;
        String sql_last = "";
        List<Args> classArgs = getClassArgs();
        int i = 0;
        for (Args args : classArgs) {
            String value = args.getValue();
            if (value != null && !value.equals("0")) {
                sql_last += "`" + args.getName() + "`='" + value + "', ";
                i++;
            }
        }
        String sql = "UPDATE `" + tableObject.getTableName() + "` SET " + sql_last + "`" + DatabaseDefaultArgs.UPDATE_TIME + "`= NOW() " +
                "Where `" + DatabaseDefaultArgs.UNId_ARG + "`='" + tableObject.getDefaultArgs().getUniqueId() + "';";
        QLog.i(this, QLOG_KEY, "即将执行的sql语句为: " + sql);
        return sql;
    }
}
