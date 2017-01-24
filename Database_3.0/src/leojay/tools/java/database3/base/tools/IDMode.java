package leojay.tools.java.database3.base.tools;

/**
 * 主键模式
 * <p>
 * time: 17/1/23__18:44
 *
 * @author leojay
 */
public enum IDMode {

    /**
     * 自动增长的id(1,2,3……)
     */
    MODE_AUTO,
    /**
     * 调用方法中实现的方法实现唯一id
     */
    MODE_MY_ONLY,
    /**
     * 自定义主键，不设置会报错！！
     */
    MODE_CUSTOM,
}
