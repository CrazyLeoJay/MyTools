package leojay.tools.java.database4.mysql;

import leojay.tools.java.MyToolsException;
import leojay.tools.java.QLog;
import leojay.tools.java.class_serialization.Args;
import leojay.tools.java.database4.core.DatabaseBase;
import leojay.tools.java.database4.core.TableClass;
import leojay.tools.java.database4.core.assist_arg.DefaultProperty;
import leojay.tools.java.database4.core.bean.DefaultArgs;
import leojay.tools.java.database4.core.tools.SQLOrder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * time: 17/3/12__22:42
 *
 * @author leojay
 */
public class MySQLOrder<T extends TableClass> extends SQLOrder<T> {

    public MySQLOrder(DatabaseBase<T> tableObject) {
        super(tableObject);
    }

    @Override
    public String getIdSql(DefaultProperty.IDMode mode) {
        String result = " `" + DefaultProperty.UNId_ARG + "` VARCHAR(32) NOT NULL COMMENT '唯一ID' ";
        switch (mode) {
            case MODE_AUTO:
                result = "`" + DefaultProperty.UNId_ARG + "` INT NOT NULL AUTO_INCREMENT COMMENT '默认自动增长ID' ";
                break;
            case MODE_MY_ONLY:
                result = " `" + DefaultProperty.UNId_ARG + "` VARCHAR(32) NOT NULL COMMENT '自定义唯一ID' ";
                break;
            case MODE_CUSTOM:
                result = " `" + DefaultProperty.UNId_ARG + "` VARCHAR(32) NOT NULL COMMENT '自定义ID' ";
                break;
            default:
                break;
        }
        return result;
    }

    @Override
    public String getCreateTableString() {
        String result = "";
        List<Args> args = getClassArgs();
        for (Args item : args) {
            String name = item.getName();
            String type = typeFilter(item.getType());
            result += ", `" + name + "` " + type;
        }
        String create_table_sql = "CREATE TABLE `" + getTableObject().getTableName() + "` ( " +
                getIdSql(getIdMode()) + result;

        List<DefaultProperty.DefaultMode> defaultModes = getTableObject().getDefaultModes();
        for (DefaultProperty.DefaultMode mode : defaultModes) {
            switch (mode) {
                case UNIQUE_ID:
                    break;
                case CREATE_TIME:
                    create_table_sql += ",`" + DefaultProperty.CREATE_TIME + "` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP";
                    break;
                case UPDATE_TIME:
                    create_table_sql += ",`" + DefaultProperty.UPDATE_TIME + "` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ";
                    break;
            }
        }
        create_table_sql += ", " + "PRIMARY KEY (`" + DefaultProperty.UNId_ARG + "`)) " +
                "ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci";
//        QLog.i(this, "即将执行的SQL语句 ", create_table_sql);
        return create_table_sql;
    }

    @Override
    public String getDeleteTableString() {
        return "DROP TABLE " + getTableObject().getTableName() + " ;";
    }

    @Override
    public String getInsertString() {
        DefaultArgs defaultArgs = getTableObject().getDefaultArgs();
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
                    e.printStackTrace();
                }
                break;
            default:
                QLog.e(this, "这种错误不可能发生！");
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


        List<DefaultProperty.DefaultMode> defaultModes = getTableObject().getDefaultModes();
        for (DefaultProperty.DefaultMode mode : defaultModes) {
            switch (mode) {
                case UNIQUE_ID:
                    break;
                case CREATE_TIME:
                    sql_item += ", `" + DefaultProperty.CREATE_TIME + "`";
                    sql_value += ", NOW()";
                    break;
                case UPDATE_TIME:
                    sql_item += ", `" + DefaultProperty.UPDATE_TIME + "`";
                    sql_value += ", NOW()";
                    break;
            }
        }

        String write_sql = "INSERT INTO `" + getTableObject().getTableName() + "` " +
                "(`" + DefaultProperty.UNId_ARG + "`, " + sql_item + ") " +
                "VALUES " +
                "(" + defaultArgs.getUniqueId() + "," + sql_value + " );";
//        QLog.i(this, "测试输出语句：" + write_sql);
        return write_sql;
    }

    @Override
    public String getDeleteString() {
        if (getTableObject().getDefaultArgs().getUniqueId() == null) return null;
        String sql = "DELETE FROM `" + getTableObject().getTableName() +
                "` WHERE `" + DefaultProperty.UNId_ARG + "`='" + getTableObject().getDefaultArgs().getUniqueId() + "';";
//        QLog.i(this, "SQL语句" + sql);
        return sql;
    }

    @Override
    public String getSelectString(SelectMode mode, String[] whereArgs) {
        String sql = "SELECT * FROM `" + getTableObject().getTableName() + "` WHERE ";
        String where = "";
        String way = mode.toString();
        int i = 0;
        if (getTableObject().getDefaultArgs().getUniqueId() != null) {
            where = "`" + DefaultProperty.UNId_ARG + "`='" + getTableObject().getDefaultArgs().getUniqueId() + "' ";
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
        if (getTableObject().getDefaultArgs().getCreateTime() != null) {
            if (i != 0) where += way;
            where += " `" + DefaultProperty.CREATE_TIME + "`='" + getTableObject().getDefaultArgs().getCreateTime() + "' ";
            i++;
        }
        if (getTableObject().getDefaultArgs().getUpdateTime() != null) {
            if (i != 0) where += way;
            where += " `" + DefaultProperty.UPDATE_TIME + "`='" + getTableObject().getDefaultArgs().getUpdateTime() + "' ";
            i++;
        }

        if (whereArgs != null) for (String item : whereArgs) {
            String arg = mode.toString();
            if (i != 0) {
                where += arg + item;
            } else {
                where += item;
            }
            i++;
        }
        if (i == 0) where = "1";
//        QLog.i(this, "即将执行的SQL语句 ： " + sql + where);
        return sql + where;
    }

    @Override
    public String getUpdateString() {
        if (getTableObject().getDefaultArgs().getUniqueId() == null) return null;
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
        String sql = "UPDATE `" + getTableObject().getTableName() + "` SET " + sql_last + "`" + DefaultProperty.UPDATE_TIME + "`= NOW() " +
                "Where `" + DefaultProperty.UNId_ARG + "`='" + getTableObject().getDefaultArgs().getUniqueId() + "';";
        QLog.i(this, "即将执行的sql语句为: " + sql);
        return sql;
    }

    /**
     * 获取 sql 中的 属性 字段部分
     *
     * @param type 文件类型
     * @return 相应的SQL语句
     */
    protected String typeFilter(String type) {
        if (type.equals("string") || type.equals("String")) {
            type = "VARCHAR(255) default 'null'";
        } else if (type.equals("int") || type.equals("Integer")) {
            type = "int default '0'";
        }
        return type;
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
}
