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
 * 通过 <code>getJSONStringFromClass</code> 方法将一个类解析为<code>json</code>数据
 * <p>
 * 通过 <code>getClassFromJSONString</code> 方法将一个<code>json</code>数据转化为类，
 * 存储在类 <code>JSONResponse</code> 中，通过<code>getData</code>方法获得。
 * <p>
 * time: 16/12/14__15:33
 *
 * @author leojay
 */
public final class JSONClass<T> {
    private Class<T> tClass;
    private static JSONClass<?> jsonClass = null;

    private JSONClass(Class<T> tClass) {
        this.tClass = tClass;
    }

    /**
     * 通过此方法获取一个本工具类的对象。
     *
     * @param tClass 定义返回对象所操作的的类
     * @param <K>    需要解析的类的类型
     * @return 一个JSONClass对象
     */
    public static <K> JSONClass<K> create(Class<K> tClass) {
        if (jsonClass == null)
            synchronized (JSONClass.class) {
                jsonClass = new JSONClass<K>(tClass);
            }
        return (JSONClass<K>) jsonClass;
    }

    /**
     * 解析对象<code>t</code>为一个json字符串，
     * 被解析的对象会被放在类<code>JSONResponse</code>中，将这个类转化为字符串
     *
     * @param t 被解析的对象
     * @return json字符串
     * @see JSONResponse
     */
    public String getJSONStringFromClass(T t) {
        String data = null;
        JSONResponse<T> response = new JSONResponse<T>();
        String className = t.getClass().getName();
        response.setData(t);
        response.setClassName(className);

        StringWriter writer = new StringWriter();
        JsonGenerator jg;
        try {
            jg = (new ObjectMapper()).getJsonFactory().createJsonGenerator(writer);
//                jg.writeObject(t);
            jg.writeObject(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

        data = writer.toString();
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(data);
        return data;
    }

    /**
     * 通过解析此类产生的json字符串，产生一个相应操作类的<code>JSONResponse</code>对象，
     * 通过<code>getData</code>方法获取
     *
     * @param jsonString 本类产生的json字符串
     * @return <code>JSONResponse</code>对象
     * @see JSONResponse#getData()
     */
    public JSONResponse<T> getClassFromJSONString(String jsonString) {
        JSONObject jsonObject = JSONObject.fromObject(jsonString);
        JSONResponse<T> response = new JSONResponse<T>();
        response = setValueForClass(response, jsonObject, new OnValueListener<JSONResponse<T>>() {
            @Override
            public JSONResponse<T> onJsonObject(JSONResponse<T> tjsonResponse, JSONObject jsonObject) {
                tjsonResponse.setData(setValueForClass(ClassArgs.newInstance(tClass), jsonObject));
                return tjsonResponse;
            }

            @Override
            public void onJsonArray(JSONArray jsonArray) {

            }
        });
        response.setState(200);
        return response;
    }

    /**
     * 这个方法会对对象<code>t</code>的参数添加值。
     *
     * @param t     被添加的对象
     * @param map   数据源
     * @param value 值
     * @param <K>   返回的数据类型
     * @throws IllegalAccessException 在赋值时候会产生异常
     * @see IllegalAccessException
     */
    private <K> void setValue(final K t, HashMap<String, String> map, Object value) throws IllegalAccessException {
        if (value == null) return;
        Field field = ReflectionUtils.getDeclaredField(t, map.get("name"));
        if (field != null) {
            field.setAccessible(true);
            field.set(t, value);
        }
    }

    /**
     * 这个方法将json里的参数添加到对象<code>k</code>里。
     *
     * @param k          被添加的对象
     * @param jsonObject json数据源
     * @param listener   当数据中有子json数据时，产生<code>JSONObject、JSONArray</code>类时响应
     * @param <K>        操作类的类型
     * @return 添加完数据后的对象k
     */
    private <K> K setValueForClass(K k, JSONObject jsonObject, OnValueListener<K> listener) {
        List<HashMap<String, String>> classArgs = ClassArgs.getSingleClassArgs(k);
        for (HashMap<String, String> itemMap : classArgs) {
            String ns = itemMap.get("name");
            Object name = null;
            try {
                name = jsonObject.get(ns);
            } catch (JSONException e) {
                QLog.w(this, "异常值！");
                e.printStackTrace();

            }
            System.out.println("---------------------------------");
            System.out.println("当前值：" + ns + " = " + name);
            System.out.println(name.getClass().getName());
            if (name instanceof JSONObject) {
                if (null != listener) {
                    k = listener.onJsonObject(k, (JSONObject) name);
                }
                continue;
            }
            if (name instanceof JSONArray) {
                if (null != listener) listener.onJsonArray((JSONArray) name);
                continue;
            }
            if (name != null && !name.equals("null")) {
                try {
                    setValue(k, itemMap, name);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return k;
    }

    /**
     * 监听为空
     *
     * @param k          被添加的对象
     * @param jsonObject json数据源
     * @param <K>        操作类的类型
     * @return 添加完数据后的对象k
     * @see #setValueForClass(Object, JSONObject, OnValueListener)
     */
    private <K> K setValueForClass(K k, JSONObject jsonObject) {
        return setValueForClass(k, jsonObject, null);
    }

    private interface OnValueListener<K> {
        K onJsonObject(K k, JSONObject jsonObject);

        void onJsonArray(JSONArray jsonArray);
    }
}
