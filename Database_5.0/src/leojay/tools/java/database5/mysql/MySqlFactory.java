package leojay.tools.java.database5.mysql;

import leojay.tools.java.MyToolsException;
import leojay.tools.java.QLog;
import leojay.tools.java.class_serialization.Args;
import leojay.tools.java.database5.core.DatabaseFactory;
import leojay.tools.java.database5.core.TableClass;
import leojay.tools.java.database5.core.assist_arg.DefaultProperty;
import leojay.tools.java.database5.core.bean.DefaultArgs;
import leojay.tools.java.database5.core.connect.ConnectManager;
import leojay.tools.java.database5.core.connect.Operation;
import leojay.tools.java.database5.core.connect.OrdinaryOperation;
import leojay.tools.java.database5.core.tools.SQLOrder;
import leojay.tools.java.database5.core.sqlconnect.Config;
import leojay.tools.java.database5.core.sqlconnect.UsuallySQLConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by CrazyLeoJay on 2017/5/26.
 */
public class MySqlFactory extends DatabaseFactory<Connection, PreparedStatement> {
    @Override
    protected ConnectManager<Connection> createConnect() {
        return new MyConnect();
    }

    @Override
    protected <T extends TableClass> SQLOrder<T> getSQLOrder(T tableClass) {
        return new MyOrder<T>(tableClass);
    }

    @Override
    protected DefaultArgs setBaseDefaultArgs() {
        return null;
    }

    @Override
    protected Operation<Connection, PreparedStatement> getOperation(String sqlOrder) {
        return OrdinaryOperation.create(sqlOrder);
    }

    private class MyConnect implements ConnectManager<Connection> {

        private Connection connect;

        @Override
        public Connection getConnect() throws ExecutionException, InterruptedException, SQLException {
            if (connect == null || connect.isClosed()) {
                connect = UsuallySQLConnect.getConnect(new MySqlConfig() {
                    @Override
                    public String getPASSWORD() {
                        return "test";
                    }

                    @Override
                    public String getUSER_NAME() {
                        return "db_test";
                    }

                    @Override
                    public String getDBName() {
                        return "leojaytoolstest";
                    }
                });
            }
            return connect;
        }

        @Override
        public void close() {
            UsuallySQLConnect.close(connect);
        }
    }

    private class MyOrder<T extends TableClass> implements SQLOrder<T> {

        T tableClass;
        Tools<T> tools;

        public MyOrder(T tableClass) {
            this.tableClass = tableClass;
            tools = new Tools<T>(createDatabaseBase(tableClass));
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
            StringBuilder result = new StringBuilder();
            List<Args> args = tools.getClassArgs();
            for (Args item : args) {
                String name = item.getName();
                String type = tools.typeFilter(item.getType());
                result.append(", `").append(name).append("` ").append(type);
            }
            String create_table_sql = "CREATE TABLE `" + tools.getTableObject().getTableName() + "` ( " +
                    getIdSql(tools.getIdMode()) + result.toString();
            create_table_sql += ",`" + DefaultProperty.CREATE_TIME + "` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP";
            create_table_sql += ",`" + DefaultProperty.UPDATE_TIME + "` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ";
            create_table_sql += ", " + "PRIMARY KEY (`" + DefaultProperty.UNId_ARG + "`)) " +
                    "ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci";
//        QLog.i(this, "即将执行的SQL语句 ", create_table_sql);
            return create_table_sql;
        }

        @Override
        public String getDeleteTableString() {
            return "DROP TABLE " + tools.getTableObject().getTableName() + " ;";
        }

        @Override
        public String getInsertString() {
            DefaultArgs defaultArgs = tools.getTableObject().getDefaultArgs();
            switch (tools.getIdMode()) {
                case MODE_AUTO:
                    defaultArgs.setUniqueId(null);
                    break;
                case MODE_MY_ONLY:
                    defaultArgs.setUniqueId(tools.getOnlyID());
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

            StringBuilder sql_item = new StringBuilder();
            StringBuilder sql_value = new StringBuilder();
            List<Args> classArgs = tools.getClassArgs();
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
                sql_item.append("`").append(name).append("`");
                sql_value.append("'").append(value).append("'");
                if (i < dataBufferd.size() - 1) {
                    sql_item.append(", ");
                    sql_value.append(", ");
                }
                j++;
                i++;
            }
            if (j == 0) {
                QLog.e(this, "没有需要输入的数据，返回空值");
                return null;
            }

            sql_item.append(", `" + DefaultProperty.CREATE_TIME + "`");
            sql_value.append(", NOW()");
            sql_item.append(", `" + DefaultProperty.UPDATE_TIME + "`");
            sql_value.append(", NOW()");

            String write_sql = "INSERT INTO `" + tools.getTableObject().getTableName() + "` " +
                    "(`" + DefaultProperty.UNId_ARG + "`, " + sql_item.toString() + ") " +
                    "VALUES " +
                    "(" + defaultArgs.getUniqueId() + "," + sql_value.toString() + " );";
//        QLog.i(this, "测试输出语句：" + write_sql);
            return write_sql;
        }

        @Override
        public String getDeleteString() {
            if (tools.getTableObject().getDefaultArgs().getUniqueId() == null) return null;
            String sql = "DELETE FROM `" + tools.getTableObject().getTableName() +
                    "` WHERE `" + DefaultProperty.UNId_ARG + "`='" + tools.getTableObject().getDefaultArgs().getUniqueId() + "';";
//        QLog.i(this, "SQL语句" + sql);
            return sql;
        }

        @Override
        public String getSelectString(SelectMode mode, String[] whereArgs) {
            String sql = "SELECT * FROM `" + tools.getTableObject().getTableName() + "` WHERE ";
            String where = "";
            String way = mode.toString();
            int i = 0;
            if (tools.getTableObject().getDefaultArgs().getUniqueId() != null) {
                where = "`" + DefaultProperty.UNId_ARG + "`='" + tools.getTableObject().getDefaultArgs().getUniqueId() + "' ";
                i++;
            }
            List<Args> classArgs = tools.getClassArgs();
            for (Args args : classArgs) {
                String value = args.getValue();
                if (value != null && !value.equals("0")) {
                    if (i != 0) where += way;
                    where += " `" + args.getName() + "`='" + args.getValue() + "' ";
                    i++;
                }
            }
            if (tools.getTableObject().getDefaultArgs().getCreateTime() != null) {
                if (i != 0) where += way;
                where += " `" + DefaultProperty.CREATE_TIME + "`='" + tools.getTableObject().getDefaultArgs().getCreateTime() + "' ";
                i++;
            }
            if (tools.getTableObject().getDefaultArgs().getUpdateTime() != null) {
                if (i != 0) where += way;
                where += " `" + DefaultProperty.UPDATE_TIME + "`='" + tools.getTableObject().getDefaultArgs().getUpdateTime() + "' ";
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
            if (tools.getTableObject().getDefaultArgs().getUniqueId() == null) return null;
            String sql_last = "";
            List<Args> classArgs = tools.getClassArgs();
            int i = 0;
            for (Args args : classArgs) {
                String value = args.getValue();
                if (value != null && !value.equals("0")) {
                    sql_last += "`" + args.getName() + "`='" + value + "', ";
                    i++;
                }
            }
            String sql = "UPDATE `" + tools.getTableObject().getTableName() + "` SET " + sql_last + "`" + DefaultProperty.UPDATE_TIME + "`= NOW() " +
                    "Where `" + DefaultProperty.UNId_ARG + "`='" + tools.getTableObject().getDefaultArgs().getUniqueId() + "';";
//            QLog.i(this, "即将执行的sql语句为: " + sql);
            return sql;
        }

    }

    private abstract class MySqlConfig implements Config {
        private int PORT = 3306;
        private String DB_URL = "localhost";
        private String DB_CHARACTER = "utf-8";

        @Override
        public String getDB_Driver() {
            return "com.mysql.jdbc.Driver";
        }

        @Override
        public String getDB_url() {
            return "jdbc:mysql://" + DB_URL + ":" + PORT + "/" + getDBName() +
                    "?useUnicode=true&characterEncoding=" + DB_CHARACTER;
        }

        public void setPORT(int PORT) {
            this.PORT = PORT;
        }

        public void setDB_URL(String DB_URL) {
            this.DB_URL = DB_URL;
        }

        public abstract String getDBName();

        public void setDB_CHARACTER(String DB_CHARACTER) {
            this.DB_CHARACTER = DB_CHARACTER;
        }
    }
}
