package leojay.tools.java.class_serialization;

/**
 * 一个接收和存放json数据了类
 * <p>
 * time: 17/1/12__12:44
 *
 * @author leojay
 */
public class JSONResponse<T> {
    //设置当前数据状态参数
    private int state = 0;
    //当前数据的类的类名
    private String className;
    //数据对象
    private T data;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
