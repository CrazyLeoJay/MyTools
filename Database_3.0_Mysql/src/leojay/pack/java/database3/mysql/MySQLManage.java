package leojay.pack.java.database3.mysql;

import leojay.pack.java.database3.mysql.tools.MySQLConnection;
import leojay.pack.java.database3.mysql.tools.MySQLMyConfig;
import leojay.pack.java.database3.mysql.tools.MySQLOperation;
import leojay.tools.java.QLog;
import leojay.tools.java.database3.DatabaseManage;
import leojay.tools.java.database3.base.DatabaseConfig;
import leojay.tools.java.database3.base.DatabaseConnection;
import leojay.tools.java.database3.base.DatabaseOperation;
import leojay.tools.java.database3.base.tools.DatabaseBase;

/**
 * <p>
 * time: 17/1/24__10:33
 *
 * @author leojay
 */
public final class MySQLManage<T> extends DatabaseManage<T> {
    private DatabaseConfig config = null;
    private Class<?> baseObject = Object.class;

    public MySQLManage(T tableClass) {
        super(tableClass);
    }

    public DatabaseConfig setConfig(String url, String name){
        config = new MySQLMyConfig(url, name);
        return config;
    }

    public void setConfig(DatabaseConfig config) {
        this.config = config;
    }

    @Override
    protected DatabaseConnection getConnection() {
        if (config == null) try {
            throw new Exception("没有配置文件！");
        } catch (Exception e) {
            QLog.e(this, e.getMessage());
        }
        return new MySQLConnection(config);
    }

    public void setBaseObject(Class<?> baseObject) {
        this.baseObject = baseObject;
    }

    @Override
    protected Class<?> getBaseObject() {
        return baseObject;
    }

    @Override
    protected DatabaseOperation setOperation(DatabaseBase base) {
        return new MySQLOperation(getConnection(), getBase());
    }

}
