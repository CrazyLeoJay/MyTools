package leojay.tools.java.database3;

import leojay.tools.java.database3.tools.DatabaseBase;

/**
 * <p>
 * time: 17/1/16__11:07
 *
 * @author leojay
 */
public abstract class DatabaseOperation {

    private DatabaseBase dbb;

    public DatabaseOperation(DatabaseBase dbb) {
        this.dbb = dbb;
    }

    public abstract void createTable(final ResultListener listener);

    public void createTable() {
        createTable(nullListener);
    }

    public abstract void deleteTable(final ResultListener listener);

    public void deleteTable() {
        deleteTable(nullListener);
    }

    public abstract void writeData(final ResultListener listener);

    public void writeData() {
        writeData(nullListener);
    }

    public abstract void deleteData(final ResultListener listener);

    public void deleteData() {
        deleteData(nullListener);
    }


    private ResultListener nullListener = new ResultListener() {
        @Override
        public void after(Mode mode, Object resultArg) {

        }
    };
}
