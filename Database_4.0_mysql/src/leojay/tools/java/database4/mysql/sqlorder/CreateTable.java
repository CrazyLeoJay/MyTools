package leojay.tools.java.database4.mysql.sqlorder;

import leojay.tools.java.database4.core.DatabaseBase;
import leojay.tools.java.database4.core.TableClass;
import leojay.tools.java.database4.core.connect.OrdinaryOperation;

/**
 * <p>
 * time: 17/3/17__16:24
 *
 * @author leojay
 */
public class CreateTable<C extends TableClass> extends OrdinaryOperation<C> {

    public CreateTable(DatabaseBase<C> tableClass) {
        super(tableClass);
    }

    @Override
    protected String getSQLOrder() {

        return "";
    }
}
