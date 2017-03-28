package leojay.tools.java.database4.mysql.sqlorder;

import leojay.tools.java.database4.core.DatabaseBase;
import leojay.tools.java.database4.core.connect.OrdinaryOperation;

/**
 * <p>
 * time: 17/3/17__16:32
 *
 * @author leojay
 */
public class WriteData extends OrdinaryOperation {
    public WriteData(DatabaseBase tableClass) {
        super(tableClass);
    }

    @Override
    protected String getSQLOrder() {
        return null;
    }
}
