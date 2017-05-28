package leojay.tools.java.database5.core.tools;

import leojay.tools.java.class_serialization.Args;
import leojay.tools.java.class_serialization.ClassArgs;
import leojay.tools.java.database5.core.DatabaseBase;
import leojay.tools.java.database5.core.TableClass;
import leojay.tools.java.database5.core.assist_arg.DefaultProperty;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by CrazyLeoJay on 2017/5/26.
 */
public interface SQLOrder<T extends TableClass> {
    String getIdSql(DefaultProperty.IDMode mode);

    String getCreateTableString();

    String getDeleteTableString();

    String getInsertString();

    String getDeleteString();

    String getSelectString(SelectMode mode, String[] whereArgs);

    String getUpdateString();

    enum SelectMode {
        OR, AND;

        public String toString() {
            String s = null;
            switch (this) {
                case AND:
                    s = " AND ";
                    break;
                case OR:
                    s = " OR ";
                    break;
                default:
                    s = "";
                    break;
            }
            return s;
        }
    }

    class Tools<T extends TableClass> {
        private final List<Args> classArgs;
        private final List<Args> defaultArgs;
        private final DatabaseBase<T> tableObject;

        private final DefaultProperty.IDMode idMode;

        public Tools(DatabaseBase<T> tableObject) {
            this.tableObject = tableObject;
            idMode = tableObject.getIdMode();
            Class<?> baseObject = tableObject.getBaseObject();
            String[] strings = new String[]{"$", "this$0"};
            classArgs = ClassArgs.getThisAndSupersClassArgs(tableObject.getTableClass(), baseObject, strings);
            defaultArgs = ClassArgs.getSingleClassArgs(tableObject.getDefaultArgs(), strings);
        }

        /**
         * 获取唯一id, 根据时间计算唯一id 方便移植
         * 唯一id组成: 秒数() +日期(6)+随机数()
         *
         * @return String 转换十六进制
         */
        public static String getOnlyID() {
            Date date = new Date();
            //秒
            String time = "";
            Long time1 = date.getTime();
            char[] chars = (time1 + "").toCharArray();
            for (int i = (chars.length - 5); i < chars.length; i++) {
                time += chars[i];
            }
            //毫秒
            String sss = (new SimpleDateFormat("SSS")).format(date);
            //日期
            String thisData = "";
            SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
            thisData = sdf.format(date);

            //转换36进制数并输出
            Long l = Long.parseLong(sss + time + thisData);
            return "'" + Long.toString(l, 36) + "'";
        }

        public DefaultProperty.IDMode getIdMode() {
            return idMode;
        }

        public List<Args> getClassArgs() {
            return classArgs;
        }

        public List<Args> getDefaultArgs() {
            return defaultArgs;
        }

        public DatabaseBase<T> getTableObject() {
            return tableObject;
        }

        /**
         * 获取 sql 中的 属性 字段部分
         *
         * @param type 文件类型
         * @return 相应的SQL语句
         */
        public String typeFilter(String type) {
            if (type.equals("string") || type.equals("String")) {
                type = "VARCHAR(255) default 'null'";
            } else if (type.equals("int") || type.equals("Integer")) {
                type = "int default '0'";
            }
            return type;
        }
    }
}
