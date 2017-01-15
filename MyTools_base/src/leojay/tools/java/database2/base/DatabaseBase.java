package leojay.tools.java.database2.base;

import leojay.tools.java.QLog;

/**
 * 数据表基础类，某个表的某条数据应有的性质
 * <p>
 * time:16/11/25__16:32
 *
 * @author:leojay
 */
public class DatabaseBase {
    //日志的KEY
    private static final String QLOG_KEY = "DatabaseBase.class";
    //数据表名称
    protected String tableName = null;
    /**
     * 数据表固定字段变量
     */
    private String uniqueId = null;//主键id
    //数据表可选字段变量
    private String createTime = null;//创建时间
    private String updateTime = null;//更新时间
    private boolean isHaveCreateTime = false;
    private boolean isHaveUpdateTime = false;
    //数据字段
    public static final String UNId_ARG = "uniqueId";
    public static final String CREATE_TIME = "createTime";//创建时间
    public static final String UPDATE_TIME = "updateTime";//更新时间
    //顶级前缀
    private String table_prefix = null;
    //二级前缀
    private String second_tab_prefix = null;

    /**
     * 得到数据表名
     *
     * @return 表名
     * @see DatabaseBase#setTableName(String)
     */
    public String getTableName() {
        if (tableName == null) {
            setTableName(this.getClass().getSimpleName());
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
            throw new Exception("必须设置数据表明！");
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

    /**
     * 获得主键
     *
     * @return 主键
     */
    public String getUniqueId() {
        return uniqueId;
    }

    /**
     * 设置主键
     *
     * @param uniqueId 主键
     */
    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    /**
     * 获得创建时间
     *
     * @return 创建时间
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * 获得更新时间
     *
     * @return 更新时间
     */
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isHaveCreateTime() {
        return isHaveCreateTime;
    }

    public void setHaveCreateTime(boolean haveCreateTime) {
        isHaveCreateTime = haveCreateTime;
    }

    public boolean isHaveUpdateTime() {
        return isHaveUpdateTime;
    }

    public void setHaveUpdateTime(boolean haveUpdateTime) {
        isHaveUpdateTime = haveUpdateTime;
    }
}
