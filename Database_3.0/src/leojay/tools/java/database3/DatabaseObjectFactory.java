package leojay.tools.java.database3;

import leojay.tools.java.database3.base.DatabaseConnection;
import leojay.tools.java.database3.base.DatabaseOperation;
import leojay.tools.java.database3.base.tools.DatabaseBase;
import leojay.tools.java.database3.base.tools.DatabaseDefaultArgs;

/**
 * 数据库工厂，继承与此类，通过<code>getOperation</code>来操作数据库操作
 * <p>
 * time: 17/1/16__11:43
 *
 * @author leojay
 */
public abstract class DatabaseObjectFactory {

    private DatabaseOperation operation;

    public DatabaseObjectFactory() {
    }

    /**
     * @return 获取链接类，数据库的链接
     */
    protected abstract DatabaseConnection getConnection();

    /**
     * @return 获取基础类的类型，指获取参数时的截止类
     */
    protected abstract Class<?> getBaseObject();

    /**
     * @param base 被操作的数据库对象
     * @return 操作类
     */
    protected abstract DatabaseOperation setOperation(DatabaseBase base);

    /**
     * 主要方法
     *
     * @return 获取操作类
     */
    public DatabaseOperation getOperation() {
        start();
        return operation;
    }

    private DatabaseBase base;

    /**
     * 进行初始化
     */
    public void start() {
        if (base == null) {
            base = new DatabaseBase(this);
            base.setBaseObject(getBaseObject());
        }
        if (operation == null) {
            this.operation = setOperation(base);
        }
    }

    public DatabaseDefaultArgs getDefaultArgs() {
        return this.getOperation().getDatabaseBase().getDefaultArgs();
    }

    public void setDefaultArgs(DatabaseDefaultArgs defaultArgs){
        this.getOperation().getDatabaseBase().setDefaultArgs(defaultArgs);
    }
}
