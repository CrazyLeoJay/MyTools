package leojay.pack.java.database2.mysql;

/**
 * <p>
 * time: 17/1/14__18:11
 *
 * @author leojay
 */
public class AppleTest extends SQLObject {
    private String name;
    String address;
    int age;
    public int number;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
