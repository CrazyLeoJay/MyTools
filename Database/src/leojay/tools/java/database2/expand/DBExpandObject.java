package leojay.tools.java.database2.expand;

import leojay.tools.java.database2.base.DatabaseBase;

/**
 * 这是一个数据表类成立的具体方法，可以继承此类有子继承抽象类<code>DatabaseObject</code>，
 * 这个类是<code>MyOperation</code>的代理，然后可以直接进行数据库操作。另一种方法，直接
 * 实现此类，在构造函数里的参数添加要生成数据表的类的对象，并实现更<code>getOperation</code>
 * 方法，获取当前数据库的操作类，通过操作类进行数据库操作。
 * <p>
 * time: 17/1/13__14:23
 *
 * @author leojay
 */
public final class DBExpandObject<T> extends DatabaseBase {

    private T t;

    /**
     * 构造函数 当t为null时，获取this的class
     * <p>
     * 当t不为null时，获取t的class
     *
     * @param t 被操作的数据库类
     */
    public DBExpandObject(T t) {
        this.t = t;
    }

    public T getTableClass() {
        return t;
    }

    public void setTableClass(T t) {
        this.t = t;
    }

    /**
     * 得到数据表名
     *
     * @return 表名
     * @see #setTableName(String)
     */
    public String getTableName() {
        if (tableName == null) {
            setTableName(t.getClass().getSimpleName());
        }
        return tableName.toLowerCase().trim();
    }

}
