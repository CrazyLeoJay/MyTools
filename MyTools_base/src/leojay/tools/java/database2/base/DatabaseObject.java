package leojay.tools.java.database2.base;

/**
 * 基础类的抽象，数据库的Object类的抽象
 * <p>
 * 在使用时，需要建立一个基础的数据库Object类，并继承与本类。并且，
 * <B>
 * 每一个继承于本类的子类，都将是一个数据表，其参数就是数据表的字段。
 * </B>
 * 通过<code>getOperation</code>方法，可以获得一个继承与抽象类<code>MyOperation</code>的类的一个对象。
 * 本类对其进行了代理，并实现了一系列数据库基本的操作(创建表和数据的增、删、查、改等)。
 * <p>
 * 若需要其他操作，可以调用<code>getOperation</code>方法获得对象后调用<code>SQLRequest</code>方法。例如：
 * <PRE>
 * ------
 * |    Apple apple = new Apple();//最终继承于DatabaseObject
 * |    apple.getOperation().SQLRequest(MyOperation.Mode.COMMON, new OnResponseListener() {
 * |        //省略实现方法
 * |    });
 * |
 * </PRE>
 * <p>
 * time: 17/1/14__16:19
 *
 * @param <Operation> 代理的操作类，继承于<code>MyOperation</code>
 * @author leojay
 * @see MyOperation#SQLRequest(MyOperation.Mode, OnResponse)
 * @see DatabaseBase
 */
public abstract class DatabaseObject<Operation extends MyOperation> extends DatabaseBase {
    /**
     * 这是一个继承方法，在子类里，需要通过工厂实例化对象后再通过<code>createOperation</code>方法获得
     * <br>例如：
     * <PRE>
     * |    public MyOperation getOperation() {
     * |        MySQLFactory factory = new MySQLFactory();
     * |        return factory.createOperation(this, .class);
     * |    }
     * </PRE>
     *
     * @return 一个操作对象
     */
    public abstract Operation getOperation();

    /**
     * 设置id 的模式详细看IDMode枚举类
     *
     * @param mode id模式
     */
    public void setIDMode(MyOperation.IDMode mode) {
        getOperation().setIdMode(mode);
    }

}
