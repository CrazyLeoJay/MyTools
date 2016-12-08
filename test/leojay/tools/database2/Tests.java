package leojay.tools.database2;

import leojay.tools.database2.base.MyOperation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * time:16/12/6__10:50
 *
 * @author:leojay
 */
public class Tests extends Assert {
    Apple apple;
    List<Apple> list;

    @Before
    public void setUp() throws Exception {
        apple = new Apple();
    }

    @Test
    public void test() throws Exception {
//        apple.deleteTable();
        apple.createTable();
        apple.setName("apple_good");
        apple.setAge(23);
        for (int k = 0; k < 20; k++) {
            apple.writeData();
        }
        apple = new Apple();
        apple.selectData(Apple.class, new MyOperation.OnResultListener<Apple>() {
            @Override
            public void result(List<Apple> result) {
                list = result;
            }
        });
        for (Apple apple : list) {
            System.out.println(apple.getName() + " -- >" + apple.getAge());
        }
        int i = list.size() - 1;
        for (int j = 1; j < i; j++) {
            apple = new Apple();
            apple.setUniqueId(list.get(j).getUniqueId());
            apple.setAge(500);
            if (j % 2 == 0) {
                apple.setName("apple_bad");
            }
            apple.updateData();
        }
//
        apple = new Apple();
        String uniqueId = list.get(i - 1).getUniqueId();
        apple.setUniqueId(uniqueId);
        apple.deleteData();

    }

    @Test
    public void listTest() {
        List<String> list = new ArrayList<>();
//        list.add("h");
//        list.add("he");
//        list.add("hel");
//        list.add("hell");
//        list.add("hello");
        assertEquals(true, list.contains("hells"));

    }
}
