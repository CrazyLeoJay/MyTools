package leojay.tools.java.database4.core.tools;

import leojay.tools.java.QLog;

/**
 * <p>
 * time: 17/2/24__11:04
 *
 * @author leojay
 */
public class TableName {
    private static final String QLOG_KEY = "数据表名称";

    //数据表名称
    private String tableName = null;
    //顶级前缀
    private String tablePrefix = null;
    //二级前缀
    private String tableSecondPrefix = null;

    public TableName(String baseString) {
        this(baseString, null, null);
    }

    public TableName(String baseString, String tablePrefix) {
        this(baseString, tablePrefix, null);
    }

    public TableName(String baseString, String tablePrefix, String tableSecondPrefix) {
        this.tablePrefix = tablePrefix;
        this.tableSecondPrefix = tableSecondPrefix;
        //生成表名
        setTableName(baseString.toLowerCase());
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
     * @param baseString 表名
     */
    private void setTableName(String baseString) {
        if (baseString == null) try {
            throw new Exception("必须设置数据表名  ！");
        } catch (Exception e) {
            QLog.e(this, QLOG_KEY, e.getMessage());
            e.printStackTrace();
        }
        if (tablePrefix != null) {
            this.tableName = tablePrefix + "_" + (tableSecondPrefix.isEmpty() ? "" : tableSecondPrefix + "_") + baseString;
        } else {
            QLog.w(this, QLOG_KEY, "没有设置前缀名！！！");
            this.tableName = baseString;
        }
    }



    /**
     * 得到数据表名,将默认全部转换为小写
     *
     * @return 表名
     * @see #setTableName(String)
     */
    @Override
    public String toString() {
//        return tableName.toLowerCase().trim();
        return tableName;
    }
}
