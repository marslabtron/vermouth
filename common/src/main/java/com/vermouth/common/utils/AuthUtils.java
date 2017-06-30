package com.vermouth.common.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.*;

/**
 * APP-REST接口签名
 * 1、将request中的参数，封装成Map<String, String> map
 * 2、去除map中的空值（value为空的key-value去除）
 * 3、把map中的所有key，取出保存在List<String> keys，对keys进行Collections.sort(keys)排序（Ascii 字符串 升序），排序后按照“参数=参数值”的模式用“&”字符拼接成字符串
 * 4、拼接好的字符串+密钥（V1("1.0.0", "j2uCBoXEEnUDTwKz");），用SHA384加密后生成签名
 *
 * Created by shenhui.ysh on 2017/6/26 0026.
 */
public class AuthUtils {
    public static final String SIGN = "sign";
    public static final String TIMESTAMP = "timestamp";
    /**
     * 签名有效时间1800秒
     **/
    private static final long SIGN_INVALID_TIME = 1800 * 1000;

    private AuthUtils() {}

    /**
     * 判断签名是否失效
     *
     * @param timestamp 时间戳（单位毫秒）
     * @return true：已失效 false：未失效
     */
    public static boolean isSignInvalid(String timestamp) {
        try {
            return System.currentTimeMillis() - Long.valueOf(timestamp) > SIGN_INVALID_TIME;
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return true;
    }

    public static String sign(Map<String, String> sParaTemp, String secretKey) {
        // 除去数组中的空值和签名参数
        Map<String, String> sPara = paraFilter(sParaTemp);
        // 生成签名结果
        return buildRequestMysign(sPara, secretKey);
    }

    public static boolean verify(Map<String, String> sParaTemp, String secretKey, String sign) {
        if (sign == null) {
            sign = "";
        }
        return sign.equals(sign(sParaTemp, secretKey));
    }

    private static Map<String, String> paraFilter(Map<String, String> sArray) {
        Map<String, String> result = new HashMap<String, String>();
        if (sArray == null || sArray.size() <= 0) {
            return result;
        }
        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("")) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }

    private static String buildRequestMysign(Map<String, String> sPara, String secretKey) {
        // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String prestr = createLinkString(sPara);
        if (prestr == null || "".equals(prestr)) {
            return "";
        }
        String mysign = DigestUtils.sha384Hex(prestr + secretKey);
        return mysign;
    }

    private static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            // 拼接时，不包括最后一个&字符
            if (i == keys.size() - 1) {
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }

    public enum AuthSecret {
        V100("1.0.0", "j2uCBoXEEnUDTwKz");

        private String version;
        private String secretKey;

        private AuthSecret(String version, String secretKey) {
            this.version = version;
            this.secretKey = secretKey;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

    }

}
