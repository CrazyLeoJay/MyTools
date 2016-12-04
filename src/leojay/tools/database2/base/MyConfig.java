package leojay.tools.database2.base;

/**
 * 配置类，这个接口里可以配置数据库的基本参数，当然可以按照需求来填写，
 * 已经实现的方法是一般数据库都需要的参数
 * <p>
 * time:16/11/30__12:55
 *
 * @author:leojay
 */
public interface MyConfig {
    /**
     * 获得驱动
     *
     * @return 加载的驱动
     */
    public String getDB_Driver();

    /**
     * 获得数据库名称
     *
     * @return 数据库名称
     */
    public String getDB_name();

    /**
     * 获得数据库链接URL
     *
     * @return 数据库链接URL
     */
    public String getDB_url();

    /**
     * 获得用户名称
     *
     * @return 用户名
     */
    public String getUSER_NAME();

    /**
     * 获得链接密码
     *
     * @return 密码
     */
    public String getPASSWORD();

    /**
     * 是否得到了配置，当使用了 properties 文件时，调用这个方法，以判断时候得到了配置
     *
     * @return 是否得到配置，得到返回<code>True</code>,否则返回<code>False</code>
     */
    boolean isHaveConfig();
}
