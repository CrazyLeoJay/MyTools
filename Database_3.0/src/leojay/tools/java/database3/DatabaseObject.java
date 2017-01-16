package leojay.tools.java.database3;

import leojay.tools.java.database3.tools.DatabaseBase;

/**
 * <p>
 * time: 17/1/16__11:43
 *
 * @author leojay
 */
public abstract class DatabaseObject {

    private DatabaseOperation operation;

    public DatabaseObject() {
        DatabaseBase base = new DatabaseBase(this);
        this.operation = setOperation(base);
    }

    protected abstract DatabaseOperation setOperation(DatabaseBase base);

    public DatabaseOperation getOperation() {
        return operation;
    }
}
