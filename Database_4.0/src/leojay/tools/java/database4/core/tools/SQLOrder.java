package leojay.tools.java.database4.core.tools;

import leojay.tools.java.class_serialization.Args;
import leojay.tools.java.class_serialization.ClassArgs;
import leojay.tools.java.database4.core.DatabaseBase;
import leojay.tools.java.database4.core.TableClass;
import leojay.tools.java.database4.core.assist_arg.DefaultProperty;

import java.util.List;

/**
 * <p>
 * time: 17/1/16__13:16
 *
 * @author leojay
 */
public abstract class SQLOrder<T extends TableClass> {
    private final List<Args> classArgs;
    private final List<Args> defaultArgs;
    private final DatabaseBase<T> tableObject;

    private final DefaultProperty.IDMode idMode;

    public SQLOrder(DatabaseBase<T> tableObject) {
        this.tableObject = tableObject;
        idMode = tableObject.getIdMode();
        Class<?> baseObject = tableObject.getBaseObject();
        classArgs = ClassArgs.getThisAndSupersClassArgs(tableObject.getTableClass(), baseObject);
        defaultArgs = ClassArgs.getSingleClassArgs(tableObject.getDefaultArgs());
    }

    public DefaultProperty.IDMode getIdMode() {
        return idMode;
    }

    public List<Args> getClassArgs() {
        return classArgs;
    }

    public List<Args> getDefaultArgs() {
        return defaultArgs;
    }

    public DatabaseBase<T> getTableObject() {
        return tableObject;
    }

    public abstract String getIdSql(DefaultProperty.IDMode mode);

    public abstract String getCreateTableString();

    public abstract String getDeleteTableString();

    public abstract String getInsertString();

    public abstract String getDeleteString();

    public abstract String getSelectString(SelectMode mode, String[] whereArgs);

    public abstract String getUpdateString();

    /**
     * 获取 sql 中的 属性 字段部分
     *
     * @param type 文件类型
     * @return 相应的SQL语句
     */
    protected String argsConvertSQLString(String type) {
        if (type.equals("string") || type.equals("String")) {
            type = "VARCHAR(255) default 'null'";
        } else if (type.equals("int") || type.equals("Integer")) {
            type = "int default '0'";
        }
        return type;
    }


    public enum SelectMode {
        OR, AND;

        public String toString() {
            String s = null;
            switch (this) {
                case AND:
                    s = " AND ";
                    break;
                case OR:
                    s = " OR ";
                    break;
                default:
                    s = "";
                    break;
            }
            return s;
        }
    }

}
