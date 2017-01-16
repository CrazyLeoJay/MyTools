package leojay.pack.java.database3.mysql;

import leojay.tools.java.database3.tools.DatabaseBase;
import leojay.tools.java.database3.DatabaseObject;
import leojay.tools.java.database3.DatabaseOperation;

/**
 * <p>
 * time: 17/1/16__12:17
 *
 * @author leojay
 */
public class MySqlObjectFactory extends DatabaseObject {
    @Override
    protected DatabaseOperation setOperation(DatabaseBase base) {
        return new MySQLOperation(base);
    }
}
