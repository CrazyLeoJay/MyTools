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
public class DatabaseBase {
    private static final String QLOG_KEY = "DatabaseBase.class";
    private DatabaseDefaultArgs defaultArgs = null;
    private Object tableClass;

    private Class<?> baseObject = Object.class;

    //数据表名称
    private String tableName = null;
    //顶级前缀
    private String table_prefix = null;
    //二级前缀
    private String second_tab_prefix = null;

    //主键模式
    private IDMode idMode = IDMode.MODE_AUTO;

    public DatabaseBase(Object tableClass) {
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
        if (table_prefix != null) {
            this.tableName = table_prefix + "_" + (second_tab_prefix == null ? "" : second_tab_prefix + "_") + tableName;
        } else {
            QLog.w(this, QLOG_KEY, "没有设置前缀名！！！");
            this.tableName = tableName;
        }
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

    public DatabaseDefaultArgs getDefaultArgs() {
        if (null == defaultArgs) defaultArgs = new DatabaseDefaultArgs();
        return defaultArgs;
    }

    public void setDefaultArgs(DatabaseDefaultArgs defaultArgs) {
        this.defaultArgs = defaultArgs;
    }

    public Object getTableClass() {
        return tableClass;
    }

    public void setTableClass(Object tableClass) {
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
