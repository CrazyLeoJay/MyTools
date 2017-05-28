package leojay.tools.java.database5.core;

import leojay.tools.java.database5.core.tools.TableName;

/**
 * 标记接口
 * <p>
 * time: 17/3/17__16:43
 *
 * @author leojay
 */
public interface TableClass {
    /**
     * @return 返回数据表名，若为空，默认为类名
     * @see TableName#setTableName(String)
     */
    TableName getTableName();
}
