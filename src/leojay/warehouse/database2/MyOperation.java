package leojay.warehouse.database2;

import leojay.warehouse.tools.QLog;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * package:leojay.warehouse.database2 <br>
 * project: MyTools<br>
 * author:leojay<br>
 * time:16/11/29__19:20<br>
 * </p>
 */
public abstract class MyOperation {

    private static final String QLOG_KEY = "MyOperation.class";

    private MyConnection connect;

    private IDMode idMode = IDMode.MODE_AUTO;


    public MyOperation(MyConnection connect) {
        this.connect = connect;
    }

    interface OnResponseListener {

        String onError(String error);

        String toSQLInstruct();

        void responseResult(Mode mode, ResultSet resultSet, boolean b, int i) throws SQLException;
    }

    enum Mode {
        COMMON, SELECT, UPDATE
    }

    public enum IDMode {
        /**
         * 自动增长的id(1,2,3……)
         */
        MODE_AUTO,
        /**
         * 调用方法中实现的方法实现唯一id
         */
        MODE_MY_ONLY,
        /**
         * 自定义主键，不设置会报错！！
         */
        MODE_CUSTOM,
    }

    public void SQLRequest(final Mode mode, final OnResponseListener listener) {
        connect.connect(new MyConnection.OnConnectListener() {
            @Override
            public void onError(String error) {
                listener.onError(error);
            }

            @Override
            public void done(Connection conn) {
                try {
                    String sql = listener.toSQLInstruct();
                    QLog.i(this, QLOG_KEY, "即将执行的sql语句为" + sql);
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ResultSet resultSet = null;
                    boolean b = false;
                    int i = 0;
                    switch (mode) {
                        case COMMON:
                            b = ps.execute();
                            break;
                        case UPDATE:
                            i = ps.executeUpdate();
                            break;
                        case SELECT:
                            resultSet = ps.executeQuery();
                            break;
                        default:
                            throw new Exception("非正常Mode！");
                    }
                    listener.responseResult(mode, resultSet, b, i);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public List<HashMap<String, String>> getClassArgs(final Class<?> obClass, final Class<?> objectClass) throws Exception {
        if (obClass == null) throw new Exception("没有设置正确的数据类！！");
        if (objectClass == null) throw new Exception("没有设置基础类！！");
        List<HashMap<String, String>> results = new ArrayList<>();
        Class<?> aClass = obClass;
        String names = obClass.getName();
        while (!names.equals(objectClass.getName())) {
            Field[] declaredFields = obClass.getDeclaredFields();
            if (declaredFields.length > 0) {
                for (Field f_item : declaredFields) {
                    f_item.setAccessible(false);
                    if (f_item.getName().contains("$"))continue;
                    HashMap<String, String> map = new HashMap<>();
                    map.put("name", f_item.getName());
                    map.put("type", f_item.getType().getSimpleName());
                    results.add(map);
                }
            }
            aClass = aClass.getSuperclass();
            names = aClass.getName();
        }
        return results;
    }

    abstract void createTable();

    abstract String getIdSql(IDMode mode);

    abstract void writeData();

    abstract void deleteData();

    abstract void selectData();

    abstract void updateData();

    public IDMode getIdMode() {
        return idMode;
    }

    public void setIdMode(IDMode idMode) {
        this.idMode = idMode;
    }
}
