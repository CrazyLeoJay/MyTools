package leojay.warehouse.tools;

import java.io.*;
import java.util.Properties;

import static leojay.warehouse.tools.QLog.*;

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
    private String fileUrl;

    private Properties DefaultProperties = null;

    private Properties thisProperties;

    private InputMode urlMode = InputMode.ABS;

    /**
     * 构造函数
     *
     * @param config 配置接口
     */
    public ReadProperties(InitConfig config) {
        setFileName(config.FileName());
        setFileUrl(config.FileUrl());
        if (config.UrlMode() != null) {
            urlMode = config.UrlMode();
        }
        setDefaultProperties(config.setDefaultProperties());
        try {
            thisProperties = initConfig();
        } catch (MyToolsException e) {
            QLog.e(this, "初始化配置失败！参数：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 用于得到配置文件的数据
     * 该方法会先读取 fileUrl 路径下的 filename 文件。
     * 如果有，直接读取并 返回一个 properties 对象。
     * 如果没有，且fileMode 是绝对路径模式的时候，
     * 会读取 /leojay/warehouse/defaultConfig 下的文件，返回一个 properties 对象。
     * 若文件不存在返回 null
     * 并创建文件
     *
     * @return 返回响应的 properties 对象
     * @throws MyToolsException 当 name 为空值时返回异常
     */
    public Properties initConfig() throws MyToolsException {
        String fileName = getFileName();
        String fileUrl = getFileUrl();
        if (fileName == null) throw new MyToolsException("参数 fileName 传入空值！");
        if (fileUrl == null) throw new MyToolsException("参数 fileUrl 传入空值！");
        Properties properties = readConfig(urlMode, fileUrl + "/" + fileName + FileType);
        if (properties == null) {
            if (urlMode == InputMode.ABS) {
                i(ReadProperties.class, "没有该文件，将创建默认文件!");
                properties = getDefaultProperties();
                writeConfig(properties, fileName, fileUrl);
            } else {
                w(this, "非绝对路径不能写入新的文件！如果需要写入请换成绝对路径");
            }
        }
        thisProperties = properties;
        return properties;
    }

    /**
     * 通过一个 key 获得一个值
     *
     * @param key 键
     */
    public String getProperty(String key) {
        return getProperty(thisProperties, key);
    }

    /**
     * 通过一个 key 获得一个值
     *
     * @param properties 一个properties对象
     * @param key        键
     */
    public String getProperty(Properties properties, String key) {
        if (properties == null) {
            QLog.e(this, "请先出示初始化！！");
            return null;
        }
        String value;
        if ((value = properties.getProperty(key)) == null) {
            properties.setProperty(key, "default");
            if (urlMode == InputMode.ABS) {
                writeConfig(properties, fileName, fileUrl);
            } else {
                QLog.e(this, "请设置绝对路径！");
            }
        }
        return value;
    }

    /**
     * 读取配置文件
     *
     * @param mode url模式
     * @param url  文件路径
     */
    private Properties readConfig(InputMode mode, String url) {
        Properties properties = new Properties();
        InputStream in = null;
        switch (mode) {
            case ABS:
                in = setAbsInput(url);
                break;
            case Class:
                in = setClassInput(url);
                break;
            default:
                try {
                    throw new FileException("这不科学！");
                } catch (FileException e) {
                    e.printStackTrace();
                }
                break;
        }
        if (in == null) {
            properties = null;
            w(this, "没有找到该路径文件: " + url);
        } else {
            i(this, "已找到该路径文件: " + url);
            try {
                i(this, "正在读取数据！");
                properties.load(in);
                in.close();
                i(this, "读取文件成功，并已关闭输入流");
            } catch (IOException e) {
                e(this, "读取文件 " + url + "错误，错误信息为：" + e);
                e.printStackTrace();
            }
        }
        return properties;
    }

    private InputStream setAbsInput(String url) {
        InputStream in = null;
        try {
            in = new FileInputStream(url);
        } catch (FileNotFoundException e) {
            e(this, "打开文件 input 流失败。错误信息： " + e);
            e.printStackTrace();
        }
        return in;
    }

    private InputStream setClassInput(String url) {
        InputStream in = Object.class.getResourceAsStream(url);
        return in;
    }

    /**
     * 文件路径的模式
     */
    public enum InputMode {
        /**
         * 绝对路径
         */
        ABS,
        /**
         * 类路径（根目录为 src/）
         */
        Class
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
        i(this, "检查配置文件目录");
        if (!f.exists()) {
            i(this, "配置文件目录不存在……");
            i(this, "正在创建配置文件文件夹……");
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
            e(this, "打开文件 output 流失败。错误信息： " + e);
            e.printStackTrace();
        } catch (IOException e) {
            e(this, "写入配置文件失败！错误信息：" + e);
            e.printStackTrace();
        }finally {
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Properties getDefaultProperties() {
        return DefaultProperties;
    }

    public void setDefaultProperties(Properties defaultProperties) {
        DefaultProperties = defaultProperties;
    }

    /**
     * 配置文档
     */
    public interface InitConfig {
        /**
         * 配置文件名称
         *
         * @return 返回一个文件名
         */
        String FileName();

        /**
         * 配置文件路径
         *
         * @return 返回一个路径
         */
        String FileUrl();

        /**
         * 配置文件路径的模式
         *
         * @return 返回一个 InputMode 值 ，一共两个值， 绝对路径 和 类路径
         */
        InputMode UrlMode();

        /**
         * 设置一个默认的配置文件
         *
         * @return 返回一个Properties对象
         */
        Properties setDefaultProperties();

    }


}
