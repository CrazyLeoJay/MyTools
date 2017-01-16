package leojay.tools.java.database2.expand;

import leojay.tools.java.database2.base.MyConnection;
import leojay.tools.java.database2.base.MyOperation;
import leojay.tools.java.database2.base.OnResponse;

/**
 * 拓展操作类
 * <p>
 * time: 17/1/14__11:18
 *
 * @author leojay
 */
public abstract class DBExpandOperation<T, O extends OnResponse>
        extends MyOperation<DBExpandObject<T>,MyConnection<?>, O> {
    /**
     * 构造函数
     *
     * @param t           数据类，继承于本类
     * @param objectClass 基础类
     */
    public DBExpandOperation(MyConnection<?> connection, DBExpandObject<T> t, Class<?> objectClass) {
        super(connection, t, objectClass);
    }
}
