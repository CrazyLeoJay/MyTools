package leojay.tools.java.database5.core;

/**
 * Created by CrazyLeoJay on 2017/5/25.
 */
public interface DatabaseTableOperation<T> {
    void setTableClass(T tableClass);
    void createTable();
    void deleteTable();
    void insert();
    void delete();
    void update();
    void query();
    void close();
}
