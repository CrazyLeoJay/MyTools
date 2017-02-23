package leojay.tools.java.database3.base;

import leojay.tools.java.database3.base.tools.DatabaseBase;
import leojay.tools.java.database3.base.tools.ResultListener;
import leojay.tools.java.database3.base.tools.StateMode;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * time: 17/1/16__11:07
 *
 * @author leojay
 */
public abstract class DatabaseOperation {

    private DatabaseBase databaseBase;

    public DatabaseOperation(DatabaseBase dbb) {
        this.databaseBase = dbb;
    }

    public abstract boolean isTable();

    public abstract void createTable(final ResultListener<Boolean> listener);

    public void createTable() {
        createTable(nullListener);
    }

    public abstract void deleteTable(final ResultListener<Boolean> listener);

    public void deleteTable() {
        deleteTable(nullListener);
    }

    public abstract void writeData(final ResultListener<Integer> listener);

    public void writeData() {
        writeData(nullListener);
    }

    public abstract void deleteData(final ResultListener<Integer> listener);

    public void deleteData() {
        deleteData(nullListener);
    }

    public abstract void updateData(final ResultListener<Integer> listener);

    public void updateData() {
        updateData(nullListener);
    }

    public abstract void selectData(final SQLString.SelectMode mode, String[] whereArgs, final ResultListener<List<DatabaseBase>> listener);

    public void selectData(String[] whereArgs, final ResultListener<List<DatabaseBase>> listener) {
        selectData(SQLString.SelectMode.OR, whereArgs, listener);
    }

    public void selectData(final SQLString.SelectMode mode, final ResultListener<List<DatabaseBase>> listener) {
        selectData(mode, null, listener);
    }

    public void selectData(final ResultListener<List<DatabaseBase>> listener) {
        selectData(SQLString.SelectMode.OR, null, listener);
    }

    public <T> void selectDataForClass(final Class<T> t, String[] whereArgs, final SQLString.SelectMode mode, final ResultListener<List<DatabaseBase<T>>> listener) {

        selectData(mode, whereArgs, new ResultListener<List<DatabaseBase>>() {
            @Override
            public void after(StateMode mode, List<DatabaseBase> resultArg) {
                List<DatabaseBase<T>> list = new ArrayList<DatabaseBase<T>>();
                for (DatabaseBase base : resultArg) {
                    Object tableClass = base.getTableClass();
                    if (tableClass.getClass().equals(t)) {
                        DatabaseBase<T> databaseBase = (DatabaseBase<T>) base;
                        list.add(databaseBase);
                    } else {
                        mode = StateMode.ERROR;
                        mode.setState("数据中返回的类的数据类型与本方法设置的数据类型不匹配！");
                    }
                }
                listener.after(mode, list);
            }
        });
    }

    public <T> void selectDataForClass(Class<T> t, String[] whereArgs, final ResultListener<List<DatabaseBase<T>>> listener) {
        selectDataForClass(t, whereArgs, SQLString.SelectMode.OR, listener);
    }

    public <T> void selectDataForClass(Class<T> t, final SQLString.SelectMode mode, final ResultListener<List<DatabaseBase<T>>> listener) {
        selectDataForClass(t, null, mode, listener);
    }

    public <T> void selectDataForClass(Class<T> t, final ResultListener<List<DatabaseBase<T>>> listener) {
        selectDataForClass(t, null, SQLString.SelectMode.OR, listener);
    }

    public DatabaseBase getDatabaseBase() {
        return databaseBase;
    }

    private ResultListener nullListener = new ResultListener() {
        @Override
        public void after(StateMode mode, Object resultArg) {

        }
    };
}
