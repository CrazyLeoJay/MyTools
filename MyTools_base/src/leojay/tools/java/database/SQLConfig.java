package leojay.tools.java.database;

import leojay.tools.java.ReadProperties;

import java.util.Properties;

/**
 * 这是一个读取数据库配置文件的类，通过构造函数初始化。
 * <p>
 * package:cn.ilinkerstudio.database
 * project: i-LinkerStudio
 * author:leojay
 * time:16/10/6__20:59
 */
public class SQLConfig {

    String DB_Driver;
    String DB_name;
    String DB_url;

    String USER_NAME;
    String PASSWORD;

    private static boolean isHaveConfig = false;

    public SQLConfig(final String url, final String name) {
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
    public static boolean isHaveConfig() {
        return isHaveConfig;
    }
}
