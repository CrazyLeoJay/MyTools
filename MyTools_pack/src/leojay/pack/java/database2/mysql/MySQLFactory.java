package leojay.pack.java.database2.mysql;

import leojay.tools.java.database2.base.DatabaseFactory;
import leojay.tools.java.database2.base.DatabaseObject;

/**
 * MySQL工厂
 * <p>
 * time:16/11/25__16:33
 *
 * @param <F> 数据表基础类
 * @author:leojay
 * @see DatabaseFactory
 */
class MySQLFactory<F extends DatabaseObject> extends DatabaseFactory<F,
        MySQLMyConfig, MySQLConnect, MySQLOperation<F>> {

    private String url = "./src/config";
    private String configName = "dbconfig";

    public MySQLFactory(String url, String configName) {
        if (null != url) this.url = url;
        if (null != configName) this.configName = configName;
    }

    /**
     * 创建一个配置
     *
     * @return 配置类
     */
    @Override
    public MySQLMyConfig createConfig() {
        return new MySQLMyConfig(url, configName);
    }

    /**
     * 创建一个数据库链接
     *
     * @return 返回一个链接对象
     */
    @Override
    public MySQLConnect createConnect() {
        return new MySQLConnect(createConfig());
    }


    /**
     * 创建一个数据库操作类
     *
     * @param DObjectClass 操作类
     * @param objectClass  基础类
     * @return 操作类
     */
    @Override
    public MySQLOperation<F> createOperation(F DObjectClass,
                                             Class<? extends DatabaseObject> objectClass) {
        return new MySQLOperation<F>(createConnect(), DObjectClass, objectClass);
    }

}
