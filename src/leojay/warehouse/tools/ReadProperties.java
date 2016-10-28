package leojay.warehouse.tools;

import java.io.*;
import java.util.Properties;

import static leojay.warehouse.tools.QLog.i;

/**
 * package:leojay.warehouse.tools
 * project: MyTools
 * author:leojay
 * time:16/10/16__17:51
 * 这是一个读取配置文件的类，该类不允许继承，是final类型。
 */
public final class ReadProperties {

    private static final String FileType = ".properties";

    private String fileName;
    private String defaultFileName;
    private String fileUrl;
    private String defaultFileUrl;


    public ReadProperties(InitConfig config) {
        setFileName(config.FileName());
        setFileUrl(config.FileUrl());
        setDefaultFileName(config.DefaultFileName());
        setDefaultFileUrl(config.DefaultFileUrl());
    }

    /**
     * 用于得到配置文件的数据
     * 该方法首先会读取 路径：/config/ 下的文件
     * 若文件存在，则返回一个 properties 对象
     * 若不存在，则会读取 路径：/leojay/warehouse/defaultConfig/ 查看是否有该文件
     * 若该文件存在，则在目录 /config/ 下创建该文件，然后再次读取该文件，并返回一个 properties 对象
     *
     * @return 返回响应的 properties 对象
     * @throws MyToolsException 当 name 为空值时返回异常
     */
    public Properties initConfig() throws MyToolsException {
        String fileName = getFileName();
        String defaultName = getDefaultFileName();
        String fileUrl = getFileUrl();
        String defaultFileUrl = getDefaultFileUrl();
        if (fileName == null) throw new MyToolsException("参数 fileName 传入空值！");
        if (fileUrl == null) throw new MyToolsException("参数 fileUrl 传入空值！");
        Properties properties = readConfig(fileUrl + "/" + fileName + FileType);
        if (properties == null) {
            i(ReadProperties.class, "没有该文件，将创建默认文件!");
            if (defaultName == null) {
                defaultName = fileName;
            }
            if (defaultFileUrl != null) {
                properties = readConfig(defaultFileUrl + "/" + defaultName + FileType);
            }
            writeConfig(properties, fileName, fileUrl);
        }
        return properties;
    }

    /**
     * 读取配置文件
     *
     * @param url 文件路径
     */
    private Properties readConfig(String url) {
        Properties properties = new Properties();
//        InputStream in = Object.class.getResourceAsStream(url);
        InputStream in = null;
        try {
            in = new FileInputStream(url);
        } catch (FileNotFoundException e) {
            QLog.e(this, "打开文件 input 流失败。错误信息： " + e);
            e.printStackTrace();
        }
        if (in == null) {
            properties = null;
            QLog.w(this, "没有找到该路径文件: " + url);
        } else {
            try {
                i(this, "正在读取数据！");
                properties.load(in);
                in.close();
                i(this, "读取文件成功，并已关闭输入流");
            } catch (IOException e) {
                QLog.e(this, "读取文件 " + url + "错误，错误信息为：" + e);
                e.printStackTrace();
            }
        }
        return properties;
    }

    /**
     * 写入文件
     *
     * @param properties 将要写入的 配置文件对象
     * @param configName 将要写入的配置文件的名称
     */
    private void writeConfig(Properties properties, String configName, String url) {
        File f = new File(url);
        boolean mkdirs = true;
        QLog.i(this, "检查配置文件目录");
        if (!f.exists()) {
            QLog.i(this, "配置文件目录不存在……");
            QLog.i(this, "正在创建配置文件文件夹……");
            mkdirs = f.mkdirs();
            if (mkdirs) {
                i(this, "配置文件目录创建成功！");
            } else {
                i(this, "配置文件夹创建失败！");
            }
        } else {
            i(this, "配置文件目录存在。");
        }
        i(this, "正在写入配置文件！");
//        if (properties != null) {
        OutputStream out = null;

        if (properties == null) properties = new Properties();
        try {
            out = new FileOutputStream(url + "/" + configName + FileType);
            properties.store(out, "This is a default file");
            out.close();
            i(this, "写入配置文件成功！并关闭输出流！");
            System.out.println(properties);
        } catch (FileNotFoundException e) {
            QLog.e(this, "打开文件 output 流失败。错误信息： " + e);
            e.printStackTrace();
        } catch (IOException e) {
            QLog.e(this, "写入配置文件失败！错误信息：" + e);
            e.printStackTrace();
        }
//        } else {
//            QLog.e(this, "错误的输入");
//        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDefaultFileName() {
        return defaultFileName;
    }

    public void setDefaultFileName(String defaultFileName) {
        this.defaultFileName = defaultFileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getDefaultFileUrl() {
        return defaultFileUrl;
    }

    public void setDefaultFileUrl(String defaultFileUrl) {
        this.defaultFileUrl = defaultFileUrl;
    }

    public interface InitConfig {
        String FileName();

        String DefaultFileName();

        String FileUrl();

        String DefaultFileUrl();
    }


}
