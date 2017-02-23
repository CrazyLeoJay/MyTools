package leojay.tools.java.database3.base.tools;

import leojay.tools.java.QLog;
import leojay.tools.java.class_serialization.Args;
import leojay.tools.java.class_serialization.ClassArgs;

import java.util.List;

/**
 * <p>
 * time: 17/1/16__10:59
 *
 * @author leojay
 */
public class DatabaseBase<T> {
    private static final String QLOG_KEY = "DatabaseBase.class";
    private DatabaseDefaultArgs defaultArgs = null;
    private T tableClass;

    private Class<?> baseObject = Object.class;

    //数据表名称
    private String tableName = null;
    //顶级前缀
    private String tablePrefix = null;
    //二级前缀
    private String tableSecondPrefix = null;

    //主键模式
    private IDMode idMode = IDMode.MODE_AUTO;

    public DatabaseBase(T tableClass) {
        this.tableClass = tableClass;
    }

    /**
     * 得到数据表名
     *
     * @return 表名
     * @see DatabaseBase#setTableName(String)
     */
    public String getTableName() {
        if (tableName == null) {
            setTableName(tableClass.getClass().getSimpleName());
        }
        return tableName.toLowerCase().trim();
    }

    /**
     * 设置数据表名,
     * <p>
     * 在这里，数据表名称将被格式化。
     * 首先，数据表名将全部被转换为小写。
     * 然后，将判断一级前缀是否为空，若为空，则直接返回设置表名，
     * 若不为空，则判断是否有二级前缀。若有，则添加并返回表名
     * <p>
     * 例如：假设数据表明为：tableName
     * <OL>
     * <LI>没有一起前缀：tablename</LI>
     * <LI>有一级前缀：first，没有二级前缀：first_tablename</LI>
     * <LI>没有一级前缀，有二级前缀:second：tablename</LI>
     * <LI>有一级前缀：first，有二级前缀:second：first_second_tablename</LI>
     * </OL>
     *
     * @param tableName 表名
     */
    public void setTableName(String tableName) {
        if (tableName == null) try {
            throw new Exception("必须设置数据表名！");
        } catch (Exception e) {
            QLog.e(this, QLOG_KEY, e.getMessage());
            e.printStackTrace();
        }
        if (tablePrefix != null) {
            this.tableName = tablePrefix + "_" + (tableSecondPrefix == null ? "" : tableSecondPrefix + "_") + tableName;
        } else {
            QLog.w(this, QLOG_KEY, "没有设置前缀名！！！");
            this.tableName = tableName;
        }
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    public String getTableSecondPrefix() {
        return tableSecondPrefix;
    }

    public void setTableSecondPrefix(String tableSecondPrefix) {
        this.tableSecondPrefix = tableSecondPrefix;
    }

    public DatabaseDefaultArgs getDefaultArgs() {
        if (null == defaultArgs) defaultArgs = new DatabaseDefaultArgs();
        return defaultArgs;
    }

    public void setDefaultArgs(DatabaseDefaultArgs defaultArgs) {
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

    public IDMode getIdMode() {
        return idMode;
    }

    public void setIdMode(IDMode idMode) {
        this.idMode = idMode;
    }

    public List<Args> getDefaultArgsList() {
        return ClassArgs.getSingleClassArgs(tableClass);
    }

    public List<Args> getTableClassArgsList() {
        return ClassArgs.getThisAndSupersClassArgs(tableClass, baseObject);
    }
}
