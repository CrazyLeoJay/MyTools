package leojay.tools.java.database3.base.tools;

/**
 * <p>
 * time: 17/1/22__12:03
 *
 * @author leojay
 */
public enum StateMode {
    SUCCESS,ERROR,WARN, NONE, CONNECTION_ERROR;
    private String state = "无";

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
