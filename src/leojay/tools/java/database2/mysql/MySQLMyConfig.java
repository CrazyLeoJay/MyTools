package leojay.tools.java.database2.mysql;

import leojay.tools.java.database2.base.MyConfig;
import leojay.tools.java.ReadProperties;

import java.util.Properties;

/**
 * 配置类，用于数据库链接的基本配置
 * <p>
 * time:16/11/30__13:05
 *
 * @author:leojay
 * @see MyConfig
 */
class MySQLMyConfig implements MyConfig {
    private String DB_Driver;
    private String DB_name;
    private String DB_url;

    private String USER_NAME;
    private String PASSWORD;
    private static boolean isHaveConfig = false;

    /**
     * 构造函数
     *
     * @param url  配置文件地址，绝对地址
     * @param name 配置文件名称，若为空，则使用默认：dbconfig
     */
    MySQLMyConfig(final String url, final String name) {
        ReadProperties dbconfig = new ReadProperties(new ReadProperties.InitConfig() {
            @Override
            public String FileName() {
                String s = "dbconfig";
                if (name != null && !name.equals("")) {
                    s = name;
                }
                return s;
            }

            @Override
            public String FileUrl() {
                return url;
            }

            @Override
            public ReadProperties.InputMode UrlMode() {
                return null;
            }

            @Override
            public Properties setDefaultProperties() {
                Properties properties = new Properties();
                properties.setProperty("DB_USER_NAME", "username");
                properties.setProperty("DB_Driver", "com.mysql.jdbc.Driver");
                properties.setProperty("DB_PASSWORD", "you password");
                properties.setProperty("PORT", "3306");
                properties.setProperty("DB_name", "you database name");
                properties.setProperty("DB_CHARACTER", "UTF-8");
                properties.setProperty("DB_URL", "localhost");
                return properties;
            }

        });
        isHaveConfig = true;
        DB_Driver = dbconfig.getProperty("DB_Driver");
        DB_name = dbconfig.getProperty("DB_name");
        String PORT = dbconfig.getProperty("PORT");
        String DB_CHARACTER = dbconfig.getProperty("DB_CHARACTER");
        String DB_URL = dbconfig.getProperty("DB_URL");
        DB_url = "jdbc:mysql://" + DB_URL + ":" + PORT + "/" + DB_name +
                "?useUnicode=true&characterEncoding=" + DB_CHARACTER;
        USER_NAME = dbconfig.getProperty("DB_USER_NAME");
        PASSWORD = dbconfig.getProperty("DB_PASSWORD");

    }

    /**
     * 判断是否得到配置信息
     *
     * @return 布尔值
     */
    public boolean isHaveConfig() {
        return isHaveConfig;
    }

    public String getDB_Driver() {
        return DB_Driver;
    }

    public String getDB_name() {
        return DB_name;
    }

    public String getDB_url() {
        return DB_url;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }
}
