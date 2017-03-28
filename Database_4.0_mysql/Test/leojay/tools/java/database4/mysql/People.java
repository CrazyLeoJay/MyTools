package leojay.tools.java.database4.mysql;

import leojay.tools.java.database4.core.TableClass;

/**
 * <p>
 * time: 17/3/17__16:06
 *
 * @author leojay
 */
public class People implements TableClass {
    String username;
    String address;
    int age;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
