package leojay.tools.java.database3.base;

import leojay.tools.java.class_serialization.ClassArgs;
import leojay.tools.java.class_serialization.ReflectionUtils;
import leojay.tools.java.database3.DatabaseObjectFactory;
import leojay.tools.java.database3.base.tools.DatabaseBase;
import leojay.tools.java.database3.base.tools.ResultListener;
import leojay.tools.java.database3.base.tools.StateMode;

import java.lang.reflect.Field;
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

    public abstract void selectData(final SQLString.SelectMode mode, final ResultListener<List<DatabaseBase>> listener);

    public void selectData(final ResultListener<List<DatabaseBase>> listener) {
        selectData(SQLString.SelectMode.OR, listener);
    }

    public <T extends DatabaseObjectFactory> void selectDataForClass(T t, final SQLString.SelectMode mode, final ResultListener<List<T>> listener) {

        selectData(mode, new ResultListener<List<DatabaseBase>>() {
            @Override
            public void after(StateMode mode, List<DatabaseBase> resultArg) {
                List<T> list = new ArrayList<T>();

                for (DatabaseBase base : resultArg) {
                    //将要被添加数据的类
                    T t = (T) ClassArgs.newInstance(getDatabaseBase().getTableClass().getClass());
                    //数据源
                    Object tableClass = base.getTableClass();

                    //两种方式
                    // 1、通过自定义类获取该类的参数集合。
//                    List<Args> tableClassArgsList = getDatabaseBase().getTableClassArgsList();
//                    for (Args args : tableClassArgsList) {
//                        Object fieldValue = ReflectionUtils.getFieldValue(tableClass, args.getName());
//                        ReflectionUtils.setFieldValue(t, args.getName(), fieldValue);
//                    }
                    // 2、获取其参数的对象组
                    Field[] declaredFields = t.getClass().getDeclaredFields();
                    //设置基础数据，主键、写入和更改时间
                    for (Field field : declaredFields) {
                        Object fieldValue = ReflectionUtils.getFieldValue(tableClass, field.getName());
                        ReflectionUtils.setFieldValue(t, field.getName(), fieldValue);
                    }
                    // 3、直接获取（不知是否可行）
//                    t = (T) base.getTableClass();

                    t.getOperation().getDatabaseBase().setDefaultArgs(base.getDefaultArgs());
                    list.add(t);
                }
                listener.after(mode, list);
            }
        });
    }

    public <T extends DatabaseObjectFactory> void selectDataForClass(T t, final ResultListener<List<T>> listener) {
        selectDataForClass(t, SQLString.SelectMode.OR, listener);
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
