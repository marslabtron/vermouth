package com.vermouth.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 使用枚举方式实现单例模式
 *
 * Created by shenhui.ysh on 2017/6/16 0016.
 */
public enum  PropertiesUtils {
    INSTANCE();

    /**
     * 只能读取classPath下配置文件
     *
     * @param file
     * @return Properties
     */
    public Properties getProperties(String file) {
        Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = PropertiesUtils.class.getClassLoader().getResourceAsStream(file);
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return properties;
    }

}