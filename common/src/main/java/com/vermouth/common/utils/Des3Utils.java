package com.vermouth.common.utils;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by shenhui.ysh on 2017/6/16 0016.
 */
public class Des3Utils {
    private Des3Utils(){}

    // 24字节的密钥
    private static final byte[] keyBytes = { 0x11, 0x22, 0x4F, 0x58, (byte) 0x88, 0x10, 0x40, 0x38, 0x28, 0x25, 0x79,
            0x51, (byte) 0xCB, (byte) 0xDD, 0x55, 0x66, 0x77, 0x29, 0x74, (byte) 0x98, 0x30, 0x40, 0x36, (byte) 0xE2 };
    // 定义加密算法,可用DES,DESede,Blowfish
    private static final String Algorithm = "DESede";

    /**
     * 3des加密
     *
     * @param src
     * @return
     */
    public static byte[] des3Crypt(String src) {
        if (StringUtils.isBlank(src)) {
            return null;
        }
        try {
            // 生成密钥
            SecretKey desKey = new SecretKeySpec(keyBytes, Algorithm);
            // 加密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, desKey);
            return c1.doFinal(src.getBytes());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 3des解密
     *
     * @param src
     * @return
     */
    public static byte[] des3Decrypt(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try {
            // 生成密钥
            SecretKey desKey = new SecretKeySpec(keyBytes, Algorithm);
            // 解密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, desKey);
            return c1.doFinal(bytes);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

}
