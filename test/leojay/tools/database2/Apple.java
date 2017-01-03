package leojay.tools.database2;

import leojay.tools.java.database2.mysql.MySQLObject;
import leojay.tools.java.database2.base.MyOperation;

/**
 * <p>
 * package:leojay.warehouse.database2<br>
 * project: MyTools<br>
 * author:leojay<br>
 * time:16/11/30__20:06<br>
 * </p>
 */
public class Apple extends MySQLObject {
    private String name;
    private int age;

    public Apple() {
        setIDMode(MyOperation.IDMode.MODE_AUTO);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
