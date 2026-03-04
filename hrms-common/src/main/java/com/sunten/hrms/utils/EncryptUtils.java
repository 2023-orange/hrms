package com.sunten.hrms.utils;

import org.springframework.util.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 加密
 *
 * @author batan
 * @since 2018-11-23
 */
public class EncryptUtils {

    private static String strKey = "Passw0rd", strParam = "Passw0rd";
    private final static String ECB_ZERO_PADDING_KEY = "SuntenHr";

    /**
     * 对称加密
     */
    public static String desEncrypt(String source) throws Exception {
        if (source == null || source.length() == 0) {
            return null;
        }
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        DESKeySpec desKeySpec = new DESKeySpec(strKey.getBytes(StandardCharsets.UTF_8));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(strParam.getBytes(StandardCharsets.UTF_8));
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        return byte2hex(
                cipher.doFinal(source.getBytes(StandardCharsets.UTF_8))).toUpperCase();
    }

    private static String byte2hex(byte[] inStr) {
        String stmp;
        StringBuilder out = new StringBuilder(inStr.length * 2);
        for (byte b : inStr) {
            stmp = Integer.toHexString(b & 0xFF);
            if (stmp.length() == 1) {
                // 如果是0至F的单位字符串，则添加0
                out.append("0").append(stmp);
            } else {
                out.append(stmp);
            }
        }
        return out.toString();
    }

    private static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0) {
            throw new IllegalArgumentException("长度不是偶数");
        }
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    /**
     * 对称解密
     */
    public static String desDecrypt(String source) throws Exception {
        if (source == null || source.length() == 0) {
            return null;
        }
        byte[] src = hex2byte(source.getBytes());
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        DESKeySpec desKeySpec = new DESKeySpec(strKey.getBytes(StandardCharsets.UTF_8));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(strParam.getBytes(StandardCharsets.UTF_8));
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        byte[] retByte = cipher.doFinal(src);
        return new String(retByte);
    }

    /**
     * desEcbNoPadding对称加密
     */
    public static String desEcbNoPaddingEncrypt(String source) throws Exception {
        if (source == null || source.length() == 0) {
            return null;
        }
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        DESKeySpec desKeySpec = new DESKeySpec(ECB_ZERO_PADDING_KEY.getBytes(StandardCharsets.UTF_8));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        // 自行补位，达到8字节的倍数
        int remainder = source.length() % 8;
        if (0 != remainder) {
            int oldLength = source.length();
            // 1.扩展自身长度
            byte[] sourceByte = Arrays.copyOf(source.getBytes(), source.length() + 8 - remainder);
            // 2.填充扩展内容为0
            Arrays.fill(sourceByte, oldLength, source.length(), (byte) 0);
            source = new String(sourceByte);
        }
        // 此处不能使用update,自行补位，
        return byte2hex(
                cipher.doFinal(
                        source.getBytes(StandardCharsets.UTF_8)
                )).toUpperCase();
    }


    /**
     * desEcbNoPadding对称解密
     */
    public static String desEcbNoPaddingDecrypt(String source) throws Exception {
        if (source == null || source.length() == 0) {
            return null;
        }
        byte[] src = hex2byte(source.getBytes());
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        DESKeySpec desKeySpec = new DESKeySpec(ECB_ZERO_PADDING_KEY.getBytes(StandardCharsets.UTF_8));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] retByte = cipher.doFinal(src);
        // 去除填充的0,倒数第一个不为0的位置，copy到另一个数组
        int zeroIndex = retByte.length;
        for (int i = retByte.length - 1; i > 0; i--) {
            if (retByte[i] == (byte) 0) {
                zeroIndex = i;
            } else {
                break;
            }
        }
        retByte = Arrays.copyOf(retByte, zeroIndex);
        return new String(retByte);
    }

    /**
     * 密码加密
     */
    public static String encryptPassword(String password) {
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }
}
