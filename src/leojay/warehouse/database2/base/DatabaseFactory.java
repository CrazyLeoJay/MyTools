package leojay.warehouse.database2.base;

/**
 * 数据库工厂类
 * package:leojay.warehouse.database2
 * project: MyTools
 * author:leojay
 * time:16/11/25__16:27
 */
public abstract class DatabaseFactory<F extends DatabaseObject> {
    /**
     * 设置配置文件
     *
     * @return 配置文件
     */
    public abstract MyConfig createConfig();
//    public abstract DBConfig createConfig(String url, String name);

    /**
     * 获得一个数据库连接
     *
     * @return 链接类
     */
    public abstract MyConnection createConnect();

    /**
     * 获得一个操作类
     *
     * @return 操作类
     */
    public abstract MyOperation createOperation(F fClass, Class<F> objectClass);
}
