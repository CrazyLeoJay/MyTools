package leojay.tools.java.database;

/**
 * 设置唯一id mode
 * 0:为默认模式,采用自动增长生成id
 * 1:为生成唯一id,采用类中实现的生成唯一id的方法
 * 2:使用uuid为唯一模式
 * <p>
 * package:pre.cl.quicksend.leojay.tools.java.database
 * project: Quicksend
 * author:leojay
 * time:16/9/1__12:46
 */
public enum IdMode {
    /**自动增长的id(1,2,3……)*/
    MODE_AUTO,
    /**调用方法中实现的方法实现唯一id*/
    MODE_MY_ONLY,
//    /**调用UUID为唯一id*/
//    MODE_UUID,
    /**自定义主键，不设置会报错！！*/
    MODE_CUSTOM,
}
