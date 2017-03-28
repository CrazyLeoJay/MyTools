package leojay.tools.java.database4.core;

import leojay.tools.java.class_serialization.Args;
import leojay.tools.java.class_serialization.ClassArgs;
import leojay.tools.java.database4.core.bean.DefaultArgs;
import leojay.tools.java.database4.core.assist_arg.DefaultProperty;
import leojay.tools.java.database4.core.tools.TableName;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * time: 17/1/16__10:59
 *
 * @author leojay
 */
public class DatabaseBase<T extends TableClass> {
    private static final String QLOG_KEY = "DatabaseBase.class";
    private DefaultArgs defaultArgs = null;
    private T tableClass;
    //基础类类型
    private Class<?> baseObject = Object.class;

    //数据表名称
    private TableName tableName = null;

    //主键模式
    private DefaultProperty.IDMode idMode = DefaultProperty.IDMode.MODE_AUTO;

    private List<DefaultProperty.DefaultMode> defaultModes;

    /**
     * 这个构造函数用于初始化此数据类，tableClass是数据源Javabean，
     *
     * @param tableClass Javabean
     */
    public DatabaseBase(T tableClass) {
        this.tableClass = tableClass;
        //默认设置其类的类名为数据表名称
        tableName = new TableName(tableClass.getClass().getSimpleName());
        //默认空的默认参数
        defaultArgs = new DefaultArgs();
        //默认参数模式
        defaultModes = new ArrayList<DefaultProperty.DefaultMode>();
        //设置默认基础默类类型
        baseObject = Object.class;
    }

    public void addDefaultMode(DefaultProperty.DefaultMode mode) {
        if (defaultModes != null) {
            if (!defaultModes.contains(mode)) defaultModes.add(mode);
        }
    }

    public void removeDefaultmode(DefaultProperty.DefaultMode mode) {
        if (defaultModes != null) {
            if (defaultModes.contains(mode)) defaultModes.remove(mode);
        }
    }

    public void setTableDefaultArgsMode(List<DefaultProperty.DefaultMode> defaultArgsMode) {
        DefaultProperty.DefaultMode defaultMode;
        if (defaultArgsMode != null && defaultArgsMode.size() > 0) {
            defaultMode = DefaultProperty.DefaultMode.UNIQUE_ID;
            if (defaultArgsMode.contains(defaultMode)) {
                this.addDefaultMode(defaultMode);
            }
            defaultMode = DefaultProperty.DefaultMode.CREATE_TIME;
            if (defaultArgsMode.contains(defaultMode)) {
                this.addDefaultMode(defaultMode);
            }
            defaultMode = DefaultProperty.DefaultMode.UPDATE_TIME;
            if (defaultArgsMode.contains(defaultMode)) {
                this.addDefaultMode(defaultMode);
            }

        }
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

    public List<DefaultProperty.DefaultMode> getDefaultModes() {
        return defaultModes;
    }

    public void setDefaultModes(List<DefaultProperty.DefaultMode> defaultModes) {
        this.defaultModes = defaultModes;
    }

    public List<Args> getDefaultArgsList() {
        return ClassArgs.getSingleClassArgs(defaultArgs);
    }

    public List<Args> getTableClassArgsList() {
        return ClassArgs.getThisAndSupersClassArgs(tableClass, baseObject);
    }

}
