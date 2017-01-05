package leojay.tools.java.class_serialization;

import leojay.tools.java.QLog;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

/**
 * JSON数据转换，可以通过JSON数据得到相应的类的对象，也可以通过对象获得JSON数据
 * <p>
 * time: 16/12/14__15:33
 *
 * @author leojay
 */
public final class JSONClass<T> {
    private Class<T> tClass;
    private static JSONClass<?> jsonClass = null;

    public JSONClass(Class<T> tClass) {
        this.tClass = tClass;
    }

    public static <K> JSONClass<K> create(Class<K> tClass) {
        if (jsonClass == null)
            synchronized (JSONClass.class) {
                jsonClass = new JSONClass<K>(tClass);
            }
        return (JSONClass<K>) jsonClass;
    }

    public String getJSONStringFromClass(T t) {
        StringWriter writer = new StringWriter();
        JsonGenerator jg;
        try {
            jg = (new ObjectMapper()).getJsonFactory().createJsonGenerator(writer);
            jg.writeObject(t);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String s = writer.toString();
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(s);
        return s;
    }

    public T getClassFromJSONString(String jsonString) {
        T t = ClassArgs.newInstance(tClass);
        JSONObject jsonObject = JSONObject.fromObject(jsonString);
        List<HashMap<String, String>> singleClassArgs = ClassArgs.getSingleClassArgs(t);
        if (singleClassArgs != null) {
            for (HashMap<String, String> itemMap : singleClassArgs) {
                String ns = itemMap.get("name");
                Object name = null;
                try {
                    name = jsonObject.get(ns);
                } catch (JSONException e) {
                    QLog.w(this, "异常值！");
                    e.printStackTrace();

                }
                System.out.println("当前值：" + name);
                System.out.println(name.getClass().getName());
                if (name instanceof JSONObject) continue;
                if (name instanceof JSONArray) continue;
                if (name != null && !name.equals("null")) {
                    try {
                        setValue(t, itemMap, name);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return t;
    }

    private void setValue(final T t, HashMap<String, String> map, Object value) throws IllegalAccessException {
        if (value == null) return;
        Field field = ReflectionUtils.getDeclaredField(t, map.get("name"));
        if (field != null) {
            field.setAccessible(true);
            field.set(t, value);
        }
    }
}
