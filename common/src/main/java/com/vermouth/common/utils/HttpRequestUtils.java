package com.vermouth.common.utils;

import nl.bitwalker.useragentutils.OperatingSystem;
import nl.bitwalker.useragentutils.UserAgent;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shenhui.ysh on 2017/6/17 0017.
 */
public class HttpRequestUtils {
    private HttpRequestUtils() {}

    /**
     * 判断是否是移动端请求
     *
     * @param request
     * @return true：是 false：否
     */
    public static boolean isMobileDevice(HttpServletRequest request) {
        try {
            UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
            OperatingSystem so = userAgent.getOperatingSystem();

            return so.isMobileDevice();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String GET = "GET";
    public static String POST = "POST";

    public static String get(String url) throws IOException {
        return get(url, null);
    }

    public static String get(String url, Map<String, String> headers) throws IOException {
        return fetch(GET, url, null, headers);
    }

    public static String post(String url, String body, Map<String, String> headers) throws IOException {
        return fetch(POST, url, body, headers);
    }

    public static String post(String url, String body) throws IOException {
        return post(url, body, null);
    }

    public static String postForm(String url, Map<String, String> params) throws IOException {
        return postForm(url, params, null);
    }

    public static String postForm(String url, Map<String, String> params, Map<String, String> headers)
            throws IOException {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        headers.put("Content-Type", "application/x-www-form-urlencoded");

        if (params == null) {
            return post(url, null, headers);
        }

        StringBuilder body = new StringBuilder("");
        if (params != null) {
            boolean first = true;
            for (String param : params.keySet()) {
                if (first) {
                    first = false;
                } else {
                    body.append("&");
                }
                String value = params.get(param);
                body.append(URLEncoder.encode(param, "UTF-8")).append("=").append(URLEncoder.encode(value, "UTF-8"));
            }
        }

        return post(url, body.toString(), headers);
    }

    public static String put(String url, String body, Map<String, String> headers) throws IOException {
        return fetch("PUT", url, body, headers);
    }

    public static String put(String url, String body) throws IOException {
        return put(url, body, null);
    }

    public static String delete(String url, Map<String, String> headers) throws IOException {
        return fetch("DELETE", url, null, headers);
    }

    public static String delete(String url) throws IOException {
        return delete(url, null);
    }

    public static String appendQueryParams(String url, Map<String, String> params) throws IOException {
        String fullUrl = url;
        if (params != null) {
            boolean first = (fullUrl.indexOf('?') == -1);
            for (String param : params.keySet()) {
                if (first) {
                    fullUrl += '?';
                    first = false;
                } else {
                    fullUrl += '&';
                }
                String value = params.get(param);
                fullUrl += URLEncoder.encode(param, "UTF-8") + '=';
                fullUrl += URLEncoder.encode(value, "UTF-8");
            }
        }

        return fullUrl;
    }

    public static Map<String, String> getQueryParams(String url) throws IOException {
        Map<String, String> params = new HashMap<String, String>();

        int start = url.indexOf('?');
        while (start != -1) {
            // read parameter name
            int equals = url.indexOf('=', start);
            String param = "";
            if (equals != -1) {
                param = url.substring(start + 1, equals);
            } else {
                param = url.substring(start + 1);
            }

            String value = "";
            if (equals != -1) {
                start = url.indexOf('&', equals);
                if (start != -1) {
                    value = url.substring(equals + 1, start);
                } else {
                    value = url.substring(equals + 1);
                }
            }

            params.put(URLDecoder.decode(param, "UTF-8"), URLDecoder.decode(value, "UTF-8"));
        }

        return params;
    }

    public static String removeQueryParams(String url) throws IOException {
        int q = url.indexOf('?');
        if (q != -1) {
            return url.substring(0, q);
        } else {
            return url;
        }
    }

    private static String fetch(String method, String url, String body, Map<String, String> headers)
            throws IOException {
        // connection
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);

        if (method != null) {
            conn.setRequestMethod(method);
        }

        if (headers != null) {
            for (String key : headers.keySet()) {
                conn.addRequestProperty(key, headers.get(key));
            }
        }

        if (body != null) {
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            os.write(body.getBytes());
            os.flush();
            os.close();
        }

        InputStream is = conn.getInputStream();
        String response = streamToString(is);
        is.close();

        if (conn.getResponseCode() == 301) {
            String location = conn.getHeaderField("Location");
            return fetch(method, location, body, headers);
        }

        return response;
    }

    private static String streamToString(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }

}
