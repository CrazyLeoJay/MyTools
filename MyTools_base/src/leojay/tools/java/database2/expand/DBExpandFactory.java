package leojay.tools.java.database2.expand;

/**
 * <p>
 * time: 17/1/14__12:44
 *
 * @author leojay
 */
public abstract class DBExpandFactory<T> {
    private final T t;

    public DBExpandFactory(T t) {
        this.t = t;
    }


    private DBExpandObject<T> object;

    public DBExpandObject<T> createExpandObject() {
        if (object == null) {
            object = new DBExpandObject<T>(t);
        }
        return object;
    }

    public void setExpandObject(DBExpandObject<T> object) {
        if (object != null)
            this.object = object;
    }

    protected abstract <F extends DBExpandObject<T>>
    DBExpandOperation createOperation(F f, Class<?> baseObject);

    public <K extends Class<?>> DBExpandOperation createOperation(K baseObject) {
        return createOperation(createExpandObject(), baseObject);
    }

    public DBExpandOperation createOperation() {
        return createOperation(createExpandObject(), Object.class);
    }
}
