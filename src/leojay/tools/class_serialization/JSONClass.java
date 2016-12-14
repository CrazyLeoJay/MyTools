package leojay.tools.class_serialization;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * time: 16/12/14__15:33
 *
 * @author leojay
 */
public class JSONClass<T> {
    public static <T> String toJsonString(List<HashMap<String, String>> datas) {
        String jsonString = "";
        int i = 0;
        for (HashMap<String, String> data : datas) {
            String s = "";
            String name = data.get("name");
            String value = data.get("value");
            if (i > 0) s += ",";
            s = name + ":" + value;
            i++;
            jsonString += s;
        }
        return jsonString;
    }

    public static void main(String[] args) {
    }
}
