package leojay.tools.java.database3.tools;

import leojay.tools.java.class_serialization.ClassArgs;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * time: 17/1/16__13:16
 *
 * @author leojay
 */
public final class SQLString {
    private SQLString sqlString;

    private List<HashMap<String, String>> tableArgs;
    private List<HashMap<String, String>> defaultArgs;

    private SQLString(DatabaseBase tableObject) {
        tableArgs = ClassArgs.getThisAndSupersClassArgs(tableObject.getObjectableClass(), Object.class);
        defaultArgs = ClassArgs.getSingleClassArgs(tableObject.getDefaultArgs());
    }

    public SQLString from(Object tableObject) {
        if (null == sqlString) {
            synchronized (this) {
                DatabaseBase base = new DatabaseBase(tableObject);
                sqlString = new SQLString(base);
            }
        }
        return sqlString;
    }

    public String getSQL(SQLMode mode) {

        return null;
    }

    public enum SQLMode {
        SELECT, INSERT, DELETE, UPDATE, CREATE_TABLE
    }

}
