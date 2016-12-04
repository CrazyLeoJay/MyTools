package leojay.tools.database2.base;

/**
 * 数据库工厂类。
 * <p>
 * 这是一个数据库工厂，所有类型的数据库都可以链接这里，例如：MySQL、SQLite等等。
 * 这里，抽象了三个方法，本别获得<B>配置文件、数据库链接、操作类</B>三个类。
 * 最后在DatabaseObject类里代理，在DatabaseObject类里有 <B>getOperation()</B> 方法，
 * 在此方法里，需要先实现一个本工厂的实例，然后调用方法<B>createOperation(DObject fClass, Class objectClass);</B>
 * <br>以MySQL为例：
 * <PRE>
 * |    public MyOperation getOperation() {
 * |        MySQLFactory factory = new MySQLFactory();
 * |        return factory.createOperation(this, MySQLObject.class);
 * |    }
 * </PRE>
 * <p>
 * time:16/11/25__16:27
 *
 * @param <DObject>    一个Object
 * @param <Config>     配置文件
 * @param <Connection> 数据库链接
 * @param <Operation>  数据库操作
 * @author:leojay
 * @see leojay.tools.database2.mysql.MySQLObject#getOperation()
 */
public abstract class DatabaseFactory<DObject extends DatabaseObject,
        Config extends MyConfig, Connection extends MyConnection, Operation extends MyOperation> {
    /**
     * 设置配置文件
     *
     * @return 配置文件
     */
    public abstract Config createConfig();
//    public abstract DBConfig createConfig(String url, String name);

    /**
     * 获得一个数据库连接
     *
     * @return 链接类
     */
    public abstract Connection createConnect();

    /**
     * 获得一个操作类
     *
     * @return 操作类
     */
    public abstract Operation createOperation(DObject DObjectClass, Class<DObject> objectClass);
}
