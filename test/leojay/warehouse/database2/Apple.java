package leojay.warehouse.database2;

import leojay.warehouse.database2.mysql.MySQLObject;
import leojay.warehouse.database2.base.MyOperation;

/**
 * <p>
 * package:leojay.warehouse.database2<br/>
 * project: MyTools<br/>
 * author:leojay<br/>
 * time:16/11/30__20:06<br/>
 * </p>
 */
public class Apple extends MySQLObject {
    private String name;
    private String age;

    Apple() {
        setIDMode(MyOperation.IDMode.MODE_AUTO);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
