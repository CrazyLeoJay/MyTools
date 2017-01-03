package leojay.tools;

import leojay.tools.java.MD5;
import org.junit.Test;

/**
 * <p>
 * time: 16/12/18__09:52
 *
 * @author leojay
 */
public class MD5Test {
    @Test
    public void toBig32MD5() throws Exception {
        String 我爱你 = MD5.toBig32MD5("我爱你");
        System.out.println(我爱你);
    }

    @Test
    public void toSmall32MD5() throws Exception {

    }

    @Test
    public void toBig16MD5() throws Exception {
//        String 我爱你 = MD5.toBig16MD5("我爱你");
//        System.out.println(我爱你);
//        我爱你 = MD5.toBig16MD5("我爱你,");
//        System.out.println(我爱你);
        String xampp = MD5.toBig16MD5("xampp");
        System.out.println(xampp);
    }

    @Test
    public void toSmall16MD5() throws Exception {

    }

}