package com.vermouth.common.utils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by shenhui.ysh on 2017/6/14 0014.
 */
public class AESUtils {
    private AESUtils() {}

    public static byte[] decrypt128(byte[] content, String seed) {
        return decrypt(content, 128, seed);
    }

    public static byte[] encrypt128(String content, String seed) {
        return encrypt(content, 128, seed);
    }

    /**
     * 解密AES加密过的字符串
     *
     * @param content AES加密过过的内容
     * @param keysize AES可以使用128、192、和256位密钥
     * @param seed    种子
     * @return
     */
    public static byte[] decrypt(byte[] content, int keysize, String seed) {
        try {
            // 创建AES的Key生产者
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            // 防止linux下随机生成key
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(seed.getBytes());
            kgen.init(keysize, secureRandom);
            // 生成一个密钥
            SecretKey secretKey = kgen.generateKey();
            // 返回基本编码格式的密钥
            byte[] enCodeFormat = secretKey.getEncoded();
            // 转换为AES专用密钥
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            // 创建密码器
            Cipher cipher = Cipher.getInstance("AES");
            // 初始化为解密模式的密码器
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] result = cipher.doFinal(content);
            // 明文
            return result;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES加密字符串
     *
     * @param content 需要被加密的字符串
     * @param keysize AES可以使用128、192、和256位密钥
     * @param seed    种子
     * @return
     */
    public static byte[] encrypt(String content, int keysize, String seed) {
        try {
            // 创建AES的Key生产者
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            // 防止linux下随机生成key
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(seed.getBytes());
            // key生产者
            kgen.init(keysize, secureRandom);
            // SecureRandom是生成安全随机数序列，seed.getBytes()是种子，只要种子相同，序列就一样，所以解密只要有seed就行
            // 生成一个密钥
            SecretKey secretKey = kgen.generateKey();
            // 返回基本编码格式的密钥，如果此密钥不支持编码，则返回null
            byte[] enCodeFormat = secretKey.getEncoded();
            // 转换为AES专用密钥
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            // 创建密码器
            Cipher cipher = Cipher.getInstance("AES");
            byte[] byteContent = content.getBytes("utf-8");
            // 初始化为加密模式的密码器
            cipher.init(Cipher.ENCRYPT_MODE, key);
            // 加密
            byte[] result = cipher.doFinal(byteContent);
            return result;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 将二进制转换成16进制 **/
    public static String parseByte2HexStr(byte[] buf) {
        if (buf == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /** 将16进制转换为二进制 **/
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

}