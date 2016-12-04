package leojay.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * package:pre.cl.quicksend.tools
 * project: Quicksend
 * author:leojay
 * time:16/9/1__19:56
 */
public final class MD5 {

    private static String toMD5(String token, MODE mode) {
        String result = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = token.getBytes();
            md.update(bytes);
            byte b[] = md.digest();

            int i;

            StringBuilder buf = new StringBuilder("");
            for (byte aB : b) {
                i = aB;
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            switch (mode) {
                case TO16:
                    // 16位的加密
                    result = buf.toString().substring(8, 24);
                    break;
                case TO32:
                    //32位加密
                    result = buf.toString();
                    break;
                default:
                    result = null;
                    break;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            result = null;
        }

        return result;
    }

    public static String toBig32MD5(String token) {
        return toMD5(token,MODE.TO32).toUpperCase();
    }

    public static String toSmall32MD5(String token) {
        return toMD5(token, MODE.TO32).toLowerCase();
    }

    public static String toBig16MD5(String token) {
        return toMD5(token, MODE.TO16).toUpperCase();
    }

    public static String toSmall16MD5(String token) {
        return toMD5(token, MODE.TO32).toLowerCase();
    }

    private enum MODE {
        TO16, TO32
    }
}
