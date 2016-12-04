package leojay.tools.database2.base;

import leojay.tools.QLog;

/**
 * 基础类的抽象，数据库的Object类的抽象
 * <p>
 * 在使用时，需要建立一个基础的数据库Object类，并继承与本类。
 * 并且本类只有一个抽象方法<code>getOperation</code>方法。
 * 通过<code>getOperation</code>方法，可以获得一个继承与抽象类<code>MyOperation</code>的类的一个对象。
 * 本类对其进行了代理，并实现了一系列数据库基本的操作。
 *
 * 若需要其他操作，可以调用<code>getOperation</code>方法获得对象后调用<code>SQLRequest</code>方法。例如：
 * <PRE>
 *
 * </PRE>
 *
 * @author:leojay time:16/11/25__16:32
 * @see MyOperation#SQLRequest(MyOperation.Mode, OnResponse)
 */
public abstract class DatabaseObject {
    private static final String QLOG_KEY = "DatabaseObject.class";

    private String tableName = null;
    //数据表固定字段变量
    private String uniqueId = null;//主键id
    //数据表可选字段变量
    private String createTime = null;//创建时间
    private String updateTime = null;//更新时间
    //数据字段
    public static final String UNId_ARG = "uniqueId";
    public static final String CREATE_TIME = "createTime";//创建时间
    public static final String UPDATE_TIME = "updateTime";//更新时间
    //顶级前缀
    private String table_prefix = null;
    //二级前缀
    private String second_tab_prefix = null;

    private MyOperation operation;

    public DatabaseObject() {
        operation = this.getOperation();
    }

    /**
     * 这是一个继承方法，在子类里，需要通过工厂实例化对象后再通过<code>createOperation</code>方法获得
     * <br>例如：
     * <PRE>
     * public MyOperation getOperation() {
     * MySQLFactory<T> factory = new MySQLFactory<>();
     * return factory.createOperation(this, .class);
     * }
     * </PRE>
     * 其中 <code>T</code> 是本类的继承类
     */
    public abstract MyOperation getOperation();

    /**
     * 设置id 的模式详细看IDMode枚举类
     *
     * @param mode id模式
     */
    public void setIDMode(MyOperation.IDMode mode) {
        operation.setIdMode(mode);
    }

    public void createTable() {
        operation.createTable();
    }

    public void writeData() {
        operation.writeData();
    }

    public void deleteData() {
        operation.deleteData();
    }

    public <T extends DatabaseObject> void selectData(Class<T> t, SelectMode mode, MyOperation.OnResultListener<T> listener) {
        operation.selectData(mode, listener);
    }

    public <T extends DatabaseObject> void selectData(Class<T> t, MyOperation.OnResultListener<T> listener) {
        selectData(t, SelectMode.ADD, listener);
    }

    public void updateData() {
        operation.updateData();
    }

    /**
     * 得到数据表名
     *
     * @return 表名
     */
    public String getTableName() {
        if (tableName == null) {
            setTableName(this.getClass().getSimpleName());
        }
        return tableName.toLowerCase().trim();
    }

    /**
     * 设置数据表名
     *
     * @param tableName 表名
     */
    public void setTableName(String tableName) {
        if (table_prefix != null) {
            this.tableName = table_prefix + "_" + (second_tab_prefix == null ? "" : second_tab_prefix + "_") + tableName;
        } else {
            QLog.w(this, QLOG_KEY, "没有设置前缀名！！！");
            this.tableName = tableName;
        }
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
