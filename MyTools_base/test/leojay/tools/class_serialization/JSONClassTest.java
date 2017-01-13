package leojay.tools.class_serialization;

import leojay.tools.java.class_serialization.JSONClass;
import leojay.tools.java.class_serialization.JSONResponse;
import leojay.tools.json.User;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 * time: 16/12/17__18:33
 *
 * @author leojay
 */
public class JSONClassTest {
    private User user;
    @Before
    public void setUp() throws Exception {
        user = new User();
        user.setName("fujiayu");
        user.setAge(20);
//        user.setGroup("keji");

    }

    @Test
    public void create() throws Exception {
        String stringFromClass = JSONClass.create(User.class).getJSONStringFromClass(user);
        JSONResponse<User> jsonString = JSONClass.create(User.class).getClassFromJSONString(stringFromClass);
        User user2 = jsonString.getData();
        System.out.println("_____测试");
        System.out.println(user2.getName());
        System.out.println(user2.getGroup());
        System.out.println(user2.getAge() + "");

    }

    @Test
    public void onClassToJsonString() throws Exception {
        JSONClass<User> jsonClass = JSONClass.create(User.class);
        String s = jsonClass.getJSONStringFromClass(user);
        System.out.println("json string"+s);
        JSONResponse<User> jsonString = jsonClass.getClassFromJSONString(s);
        System.out.println("_____测试");
        System.out.println(jsonString.getData().getName());
        System.out.println(jsonString.getData().getGroup());
        System.out.println(jsonString.getData().getAge() + "");

    }

    @Test
    public void getClassFromJosnString() throws Exception {

    }

}