package leojay.pack.java.database3.mysql;

import leojay.pack.java.database3.mysql.tools.MySQLConnection;
import leojay.pack.java.database3.mysql.tools.MySQLMyConfig;
import leojay.pack.java.database3.mysql.tools.MySQLOperation;
import leojay.tools.java.database3.DatabaseObjectFactory;
import leojay.tools.java.database3.base.DatabaseConnection;
import leojay.tools.java.database3.base.DatabaseOperation;
import leojay.tools.java.database3.base.tools.DatabaseBase;

/**
 * <p>
 * time: 17/1/16__12:17
 *
 * @author leojay
 */
public class MySqlObjectFactory extends DatabaseObjectFactory {

    private String url;
    private String configName;

    private MySQLMyConfig config;

    public MySqlObjectFactory(String url, String configName) {
        this.url = url;
        this.configName = configName;
    }

    @Override
    protected DatabaseOperation setOperation(DatabaseBase base) {
        return new MySQLOperation(getConnection(), base);
    }

    private MySQLConnection conn;

    @Override
    protected DatabaseConnection getConnection() {
        if (null == conn) conn = new MySQLConnection(getConfig());
        return conn;
    }

    @Override
    protected Class<?> getBaseObject() {
        return MySqlObjectFactory.class;
    }

    private MySQLMyConfig getConfig() {
        if (null == config) config = new MySQLMyConfig(url, configName);
        return config;
    }


}
