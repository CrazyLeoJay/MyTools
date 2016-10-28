package leojay.warehouse.test;

import leojay.warehouse.database.IdMode;
import leojay.warehouse.database.MyToolsDBObject;
import leojay.warehouse.database.SQLConfig;

import java.io.File;

/**
 * package:leojay.warehouse.test
 * project: MyTools
 * author:leojay
 * time:16/10/27__18:10
 */
public class Test extends MyToolsDBObject {
    public Test() {
        super(new InitListener() {

            @Override
            public SQLConfig sqlConfig() {
                return new SQLConfig("./src/config", null);
            }

            @Override
            public String firstTabPrefix() {
                return null;
            }

            @Override
            public String secondTabPrefix() {
                return null;
            }

            @Override
            public boolean isCreateTimeField() {
                return false;
            }

            @Override
            public boolean isUpdateTimeField() {
                return false;
            }

            @Override
            public IdMode setIdMode() {
                return null;
            }

            @Override
            public Class setObjectClass() {
                return null;
            }
        });
    }

    public static void main(String[] args) {
        File file = new File("./");
        System.out.println(file.getName());
        if (file.isDirectory()){
            String[] list = file.list();
            for (String item: list){
                System.out.println(item);
            }
        }
        Test test = new Test();
    }
}
