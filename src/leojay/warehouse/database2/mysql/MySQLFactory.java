package leojay.warehouse.database2.mysql;

import leojay.warehouse.database2.base.*;

/**
 * package:leojay.warehouse.database2
 * project: MyTools
 * author:leojay
 * time:16/11/25__16:33
 */
class MySQLFactory<F extends DatabaseObject> extends DatabaseFactory<F> {

    @Override
    public MyConfig createConfig() {
        return new MySQLMyConfig("./src/config", "dbconfig");
    }

    @Override
    public MyConnection createConnect() {
        return new MySQLConnect(createConfig());
    }

    @Override
    public MyOperation createOperation(F fClass, Class<F> objectClass) {
        return new MySQLOperation<>(createConnect(), fClass, objectClass);
    }

}
