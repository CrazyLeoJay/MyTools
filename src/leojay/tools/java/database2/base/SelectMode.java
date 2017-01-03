package leojay.tools.java.database2.base;

/**
 * 设置查询时的查询模式，精确查找还是模糊查找
 * <p>
 * 事实上是sql语句使用 and 和 or
 * 的区别
 * <p>
 * time:16/12/1__13:56
 *
 * @author leojay
 */
public enum SelectMode {
    ADD, OR
}
