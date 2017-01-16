package leojay.tools.java.database3.tools;

/**
 * <p>
 * time: 17/1/16__10:59
 *
 * @author leojay
 */
public class DatabaseBase {
    private DatabaseDefaultArgs defaultArgs = null;
    private Object tableClass;

    public DatabaseBase(Object tableClass) {
        this.tableClass = tableClass;
    }

    public DatabaseDefaultArgs getDefaultArgs() {
        return defaultArgs;
    }

    public void setDefaultArgs(DatabaseDefaultArgs defaultArgs) {
        this.defaultArgs = defaultArgs;
    }

    public Object getObjectableClass() {
        return tableClass;
    }

    public void setObjectableClass(Object tableClass) {
        this.tableClass = tableClass;
    }
}
