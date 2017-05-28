package leojay.tools.java.database5.core;

import leojay.tools.java.class_serialization.Args;
import leojay.tools.java.class_serialization.ClassArgs;
import leojay.tools.java.database5.core.assist_arg.DefaultProperty;
import leojay.tools.java.database5.core.bean.DefaultArgs;
import leojay.tools.java.database5.core.tools.TableName;

import java.util.List;

/**
 * <p>
 * time: 17/1/16__10:59
 *
 * @author leojay
 */
public class DatabaseBase<T extends TableClass> {
    private static final String QLOG_KEY = "DatabaseBase.class";
    private T tableClass;
    private DefaultArgs defaultArgs = null;
    //基础类类型
    private Class<?> baseObject = Object.class;

    //数据表名称
    private TableName tableName = null;

    //主键模式
    private DefaultProperty.IDMode idMode = DefaultProperty.IDMode.MODE_AUTO;

    public DatabaseBase(T tableClass) {
        this(tableClass, new DefaultArgs());
    }

    /**
     * 这个构造函数用于初始化此数据类，tableClass是数据源Javabean，
     *
     * @param tableClass Javabean
     */
    public DatabaseBase(T tableClass, DefaultArgs defaultArgs) {
        this.tableClass = tableClass;
        //默认设置其类的类名为数据表名称
        if (tableClass.getTableName() == null) {
            tableName = new TableName(tableClass.getClass().getSimpleName());
        }else {
            tableName = tableClass.getTableName();
        }
        //默认空的默认参数
        this.defaultArgs = defaultArgs;
        //设置默认基础默类类型
        baseObject = Object.class;
    }

    public String getTableName() {
        return tableName.toString();
    }

    public void setTableName(TableName tableName) {
        this.tableName = tableName;
    }

    public DefaultArgs getDefaultArgs() {
        return defaultArgs;
    }

    public void setDefaultArgs(DefaultArgs defaultArgs) {
        this.defaultArgs = defaultArgs;
    }

    public T getTableClass() {
        return tableClass;
    }

    public void setTableClass(T tableClass) {
        this.tableClass = tableClass;
    }

    public Class<?> getBaseObject() {
        return baseObject;
    }

    public void setBaseObject(Class<?> baseObject) {
        this.baseObject = baseObject;
    }

    public DefaultProperty.IDMode getIdMode() {
        return idMode;
    }

    public void setIdMode(DefaultProperty.IDMode idMode) {
        this.idMode = idMode;
    }

    public List<Args> getDefaultArgsList() {
        return ClassArgs.getSingleClassArgs(defaultArgs);
    }

    public List<Args> getTableClassArgsList() {
        return ClassArgs.getThisAndSupersClassArgs(tableClass, baseObject);
    }

}
