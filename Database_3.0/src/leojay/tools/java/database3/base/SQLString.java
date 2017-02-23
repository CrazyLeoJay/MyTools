package leojay.tools.java.database3.base;

import leojay.tools.java.class_serialization.Args;
import leojay.tools.java.class_serialization.ClassArgs;
import leojay.tools.java.database3.base.tools.DatabaseBase;
import leojay.tools.java.database3.base.tools.IDMode;

import java.util.List;

/**
 * <p>
 * time: 17/1/16__13:16
 *
 * @author leojay
 */
public abstract class SQLString {
    private List<Args> classArgs;
    private List<Args> defaultArgs;
    private DatabaseBase tableObject;

    private final IDMode idMode;

    public SQLString(DatabaseBase tableObject) {
        this.tableObject = tableObject;
        idMode = tableObject.getIdMode();
        Class<?> baseObject = tableObject.getBaseObject();
        classArgs = ClassArgs.getThisAndSupersClassArgs(tableObject.getTableClass(), baseObject);
        defaultArgs = ClassArgs.getSingleClassArgs(tableObject.getDefaultArgs());
    }

    public String getSQL(CMDMode mode) {
        String result = null;
        switch (mode) {
            case CREATE_TABLE:
                result = getCreateTableString();
                break;
            case DELETE_TABLE:
                result = getDeleteTableString();
                break;
            case INSERT:
                result = getInsertString();
                break;
            case DELETE:
                result = getDeleteString();
                break;
            case SELECT_OR:
                result = getSelectString(SelectMode.OR);
                break;
            case SELECT_AND:
                result = getSelectString(SelectMode.AND);
                break;
            case UPDATE:
                result = getUpdateString();
                break;
            default:
                result = null;
                break;
        }
        return result;
    }

    public List<Args> getClassArgs() {
        return classArgs;
    }

    public List<Args> getDefaultArgs() {
        return defaultArgs;
    }

    public DatabaseBase getTableObject() {
        return tableObject;
    }

    public IDMode getIdMode() {
        return idMode;
    }

    protected abstract String getIdSql(IDMode mode);

    protected abstract String getCreateTableString();

    protected abstract String getDeleteTableString();

    protected abstract String getInsertString();

    protected abstract String getDeleteString();

    protected abstract String getSelectString(SelectMode mode, String[] args);

    public String getSelectString(SelectMode mode){
        return getSelectString(mode, null);
    }

    protected abstract String getUpdateString();

    public enum CMDMode {
        SELECT_OR, INSERT, DELETE, UPDATE, CREATE_TABLE, SELECT_AND, DELETE_TABLE
    }

    public enum SelectMode {
        OR, AND;
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


}
