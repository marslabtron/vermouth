package com.vermouth.common.utils;

import java.security.MessageDigest;

/**
 * Created by shenhui.ysh on 2017/6/16 0016.
 */
public class MD5Utils {
    private MD5Utils(){}

    /**
     * 把字节数组转换成md5
     *
     * @param bytes
     * @return
     */
    public static String bytes2MD5(byte[] bytes) {
        String md5str = "";
        try {
            // 创建一个提供信息摘要算法的对象,初始化为md5算法对象
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算后获得字节数组
            byte[] temp = md.digest(bytes);
            // 把数组每一字节换成16进制连成md5字符串
            md5str = bytes2Hex(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5str;
    }

    /**
     * 把字符串转换成md5
     *
     * @param s
     * @return
     */
    public static String string2MD5(String s) {
        return bytes2MD5(s.getBytes());
    }

    /**
     * 把字节数组转成16进位制数
     *
     * @param bytes
     * @return
     */
    private static String bytes2Hex(byte[] bytes) {
        StringBuilder md5str = new StringBuilder();
        // 把数组每一字节换成16进制连成md5字符串
        int digital = 0;
        for (int i = 0; i < bytes.length; i++) {
            digital = bytes[i];
            if (digital < 0) {
                digital += 256;
            }
            if (digital < 16) {
                md5str.append("0");
            }
            md5str.append(Integer.toHexString(digital));
        }
        // toUpperCase方法返回一个字符串,该字符串中的所有字母都被转化为大写字母
        return md5str.toString().toUpperCase();
    }

}
