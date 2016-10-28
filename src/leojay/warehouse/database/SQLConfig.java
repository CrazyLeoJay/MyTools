package leojay.warehouse.database;

import leojay.warehouse.tools.FileException;
import leojay.warehouse.tools.MyToolsException;
import leojay.warehouse.tools.QLog;
import leojay.warehouse.tools.ReadProperties;

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
        try {
            ReadProperties config = new ReadProperties(new ReadProperties.InitConfig() {
                @Override
                public String FileName() {
                    String s = "dbconfig";
                    if (name != null && !name.equals("")) {
                        s = name;
                    }
                    return s;
                }

                @Override
                public String DefaultFileName() {
                    return null;
                }

                @Override
                public String FileUrl() {
                    return url;
                }

                @Override
                public String DefaultFileUrl() {
                    return "/leojay/warehouse/defaultConfig";
                }

            });
            Properties dbconfig = config.initConfig();
            if (dbconfig != null) {
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
            } else {
                throw new FileException("文件没有找到，请重新执行一遍程序");
            }
        } catch (FileException e) {
            QLog.w(this, "文件没有找到，请重新执行一遍程序");
            e.printStackTrace();
        } catch (MyToolsException e) {
            isHaveConfig = false;
            QLog.e(this, "没有读取到配置文件！");
            e.printStackTrace();
        }

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
