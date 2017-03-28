package leojay.tools.java.database4.mysql;


import leojay.tools.java.sqlconnect.Config;

/**
 * <p>
 * time: 17/3/17__16:11
 *
 * @author leojay
 */
public abstract class MySqlConfig implements Config {
    private int PORT = 3306;
    private String DB_URL = "localhost";
    private String DB_CHARACTER = "utf-8";

    @Override
    public String getDB_Driver() {
        return "com.mysql.jdbc.Driver";
    }

    @Override
    public String getDB_url() {
        return "jdbc:mysql://" + DB_URL + ":" + PORT + "/" + getDBName() +
                "?useUnicode=true&characterEncoding=" + DB_CHARACTER;
    }

    public void setPORT(int PORT) {
        this.PORT = PORT;
    }

    public void setDB_URL(String DB_URL) {
        this.DB_URL = DB_URL;
    }

    public abstract String getDBName();

    public void setDB_CHARACTER(String DB_CHARACTER) {
        this.DB_CHARACTER = DB_CHARACTER;
    }
}
