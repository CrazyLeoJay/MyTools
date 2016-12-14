package leojay.tools.json;

import net.sf.json.JSONObject;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * time: 16/12/14__20:38
 *
 * @author leojay
 */
public class JSONTest {
    @Test
    public void name() throws Exception {
        User user = new User();
        user.setName("fujiayu");
        user.setAge(20);
        user.setGroup("keji");
        JSONObject jsonObject = JSONObject.fromObject(user);
        System.out.println(jsonObject);

    }

    @Test
    public void jsonToClass() throws Exception {
        User u = new User();
        u.setName("name1");
        u.setAge(1);
        u.setGroup("man");
        User u2 = new User();
        u2.setName("name2");
        u2.setAge(2);
        u2.setGroup("man");
        List list = new ArrayList();
        list.add(u);
        list.add(u2);
        ObjectMapper obj = new ObjectMapper();
        JsonGenerator jg = null;

        try {
            jg = obj.getJsonFactory().createJsonGenerator(System.out,
                    JsonEncoding.UTF8);
            System.out.println("jsonGenerator");
            // writeObject可以转换java对象，eg:JavaBean/Map/List/Array等
            jg.writeObject(u);
            System.out.println();
            jg.writeObject(list);
            System.out.println();
            System.out.println("ObjectMapper");
            // writeValue具有和writeObject相同的功能
//            obj.writeValue(System.out, list);

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("测试");

    }

    @Test
    public void method2() throws Exception {
        User u = new User();
        u.setName("name1");
        u.setAge(1);
        u.setGroup("man");
        User u2 = new User();
        u2.setName("name2");
        u2.setAge(2);
        u2.setGroup("man");
        List list = new ArrayList();
        list.add(u);
        list.add(u2);
        ObjectMapper obj = new ObjectMapper();
        JsonGenerator jg = null;

        try {
            DataOutputStream dataOutputStream = new DataOutputStream(System.out);
            jg = obj.getJsonFactory().createJsonGenerator(System.out,
                    JsonEncoding.UTF8);

            String s = new String();
            Reader reader = new Reader() {
                @Override
                public int read(char[] cbuf, int off, int len) throws IOException {
                    return 0;
                }

                @Override
                public void close() throws IOException {

                }
            };
            BufferedOutputStream outputStream = new BufferedOutputStream(System.out);
            BufferedInputStream inputStream = new BufferedInputStream(System.in);
            PrintStream printStream = new PrintStream(System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
