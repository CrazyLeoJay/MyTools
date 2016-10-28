package leojay.warehouse.database;

import java.util.List;

/**
 * package:pre.cl.quicksend.database.listener
 * project: Quicksend
 * author:leojay
 * time:16/9/3__17:32
 */
public interface DBReadListener<T extends MyToolsDBObject> extends ReturnError{
    /**
     * @param data 返回对应的数据列
     */
    void result(List<T> data);
}
