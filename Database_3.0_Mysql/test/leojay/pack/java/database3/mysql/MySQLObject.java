package leojay.pack.java.database3.mysql;

import leojay.pack.java.database3.mysql.tools.MySQLMyConfig;

/**
 * <p>
 * time: 17/1/20__19:14
 *
 * @author leojay
 */
public class MySQLObject extends MySqlObjectFactory {
    public MySQLObject() {
    }

    @Override
    protected MySQLMyConfig getConfig() {
        return new MySQLMyConfig("./test/config", "testSql");
    }
}
