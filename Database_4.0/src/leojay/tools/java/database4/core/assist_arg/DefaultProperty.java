package leojay.tools.java.database4.core.assist_arg;

/**
 * <p>
 * time: 17/2/23__20:59
 *
 * @author leojay
 */
public interface DefaultProperty {
    //数据字段
    String UNId_ARG = "uniqueId";
    String CREATE_TIME = "createTime";//创建时间
    String UPDATE_TIME = "updateTime";//更新时间

    enum DefaultMode {
        UNIQUE_ID, UPDATE_TIME, CREATE_TIME;
    }
    enum IDMode {

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
}
