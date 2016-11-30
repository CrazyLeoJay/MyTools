package leojay.warehouse.database2.base;

/**
 * package:leojay.warehouse.database2
 * project: MyTools
 * author:leojay
 * time:16/11/30__12:55
 */
public interface MyConfig {
    public String getDB_Driver();

    public String getDB_name();

    public String getDB_url();

    public String getUSER_NAME();

    public String getPASSWORD();

    boolean isHaveConfig();
}
