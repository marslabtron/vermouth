package com.vermouth.common.utils;

import nl.bitwalker.useragentutils.OperatingSystem;
import nl.bitwalker.useragentutils.UserAgent;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by shenhui.ysh on 2017/6/17 0017.
 */
public class HttpRequestUtils {
    private HttpRequestUtils(){}

    /**
     * 判断是否是移动端请求
     * @param request
     * @return true：是 false：否
     */
    public static boolean isMobileDevice(HttpServletRequest request){
        try {
            UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
            OperatingSystem so = userAgent.getOperatingSystem();

            return so.isMobileDevice();
        }catch (Throwable e){
            e.printStackTrace();
        }
        return false;
    }

}
