package leojay.warehouse.database2.base;

import leojay.warehouse.tools.QLog;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
public abstract class MyOperation<F extends DatabaseObject> {
    private static final String QLOG_KEY = "MyOperation.class";

    private MyConnection connect;

    private IDMode idMode = IDMode.MODE_AUTO;

    public F f;
    private Class<?> objectClass;

    public MyOperation(MyConnection connect, F f, Class<?> objectClass) {
        this.connect = connect;
        this.f = f;
        this.objectClass = objectClass;
    }

    public interface OnResponseListener {

        String onError(String error);

        String toSQLInstruct() throws Exception;

        void responseResult(Mode mode, ResultSet resultSet, boolean b, int i) throws Exception;
    }

    public interface OnResultListener<F> {
        void result(List<F> result);
    }

    public enum Mode {
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

    protected void SQLRequest(final Mode mode, final OnResponseListener listener) {
        connect.connect(new MyConnection.OnConnectListener() {
            @Override
            public void onError(String error) {
                listener.onError(error);
            }

            @Override
            public void done(Connection conn) {
                try {
                    String sql = listener.toSQLInstruct();
                    QLog.i(this, QLOG_KEY, "即将执行的sql语句为: " + sql);
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ResultSet resultSet = null;
                    boolean b = false;
                    int i = 0;
                    switch (mode) {
                        case COMMON:
                            b = ps.execute();
                            if (b) {
                                QLog.i(this, QLOG_KEY + "_COMMON", "此次sql语句执行成功！并且有返回值！");
                                resultSet = ps.getResultSet();
                            } else {
                                QLog.w(this, QLOG_KEY + "_COMMON", "此次sql语句执行成功！但没有任何返回值");
                            }
                            break;
                        case UPDATE:
                            QLog.i(this, QLOG_KEY + "_UPDATE", "此次执行的SQL语句成功执行 " + i + " 个");
                            i = ps.executeUpdate();
                            break;
                        case SELECT:
                            QLog.i(this, QLOG_KEY + "_SELECT", "此次SQL语句执行有返回的值，使用 resultSet 具体查看。");
                            resultSet = ps.executeQuery();
                            break;
                        default:
                            throw new Exception("非正常Mode！");
                    }
                    listener.responseResult(mode, resultSet, b, i);
                } catch (SQLException e) {
                    QLog.w(this, QLOG_KEY, "发生数据库访问错误！:" + e.getMessage());
                    e.printStackTrace();
                    listener.onError(e.getMessage());
                } catch (Exception e) {
                    QLog.w(this, QLOG_KEY, "此次sql语句执行失败！:" + e.getMessage());
                    e.printStackTrace();
                    listener.onError(e.getMessage());
                }

            }
        });
    }

    public List<HashMap<String, String>> getClassArgs() throws Exception {
        F obClass = f;
        if (obClass == null) throw new Exception("没有设置正确的数据类！！");
        if (objectClass == null) throw new Exception("没有设置基础类！！");
        List<HashMap<String, String>> results = new ArrayList<>();
        Class<?> aClass = obClass.getClass();
        String names = aClass.getName();
        while (!names.equals(objectClass.getName())) {
            Field[] declaredFields = aClass.getDeclaredFields();
            if (declaredFields.length > 0) {
                for (Field f_item : declaredFields) {
                    f_item.setAccessible(true);
                    if (f_item.getName().contains("$")) continue;
                    Object o = f_item.get(obClass);
                    String value = (o == null ? null : o.toString());

                    HashMap<String, String> map = new HashMap<>();
                    map.put("name", f_item.getName());
                    map.put("type", f_item.getType().getSimpleName());
                    map.put("value", value);
                    results.add(map);
                }
            }
            aClass = aClass.getSuperclass();
            names = aClass.getName();
        }
        return results;
    }

    protected abstract String getIdSql(IDMode mode);

    public abstract void createTable();

    public abstract void writeData();

    public abstract void deleteData();

    /**
     * 查询数据
     */
    public abstract void selectData(SelectMode mode, OnResultListener<F> listener);

    public abstract void updateData();

    public IDMode getIdMode() {
        return idMode;
    }

    protected void setIdMode(IDMode idMode) {
        this.idMode = idMode;
    }

    /**
     * 获取唯一id, 根据时间计算唯一id 方便移植
     * 唯一id组成: 秒数() +日期(6)+随机数()
     *
     * @return String 转换十六进制
     */
    protected static String getOnlyID() {
        Date date = new Date();
        //秒
        String time = "";
        Long time1 = date.getTime();
        char[] chars = (time1 + "").toCharArray();
        for (int i = (chars.length - 5); i < chars.length; i++) {
            time += chars[i];
        }
        //毫秒
        String sss = (new SimpleDateFormat("SSS")).format(date);
        //日期
        String thisData = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        thisData = sdf.format(date);

        //转换36进制数并输出
        Long l = Long.parseLong(sss + time + thisData);
        return "'" + Long.toString(l, 36) + "'";
    }

}
