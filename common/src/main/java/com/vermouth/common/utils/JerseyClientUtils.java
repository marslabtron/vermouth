package com.vermouth.common.utils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

/**
 * Created by shenhui.ysh on 2017/6/14 0014.
 */
public class JerseyClientUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JerseyClientUtils.class);
    private static final int READ_TIME_OUT = 5000;
    private static final int CONNECT_TIME_OUT = 5000;
    private Client client;

    public void init() {
        client = Client.create();
        client.setReadTimeout(READ_TIME_OUT);
        client.setConnectTimeout(CONNECT_TIME_OUT);
    }

    public void destroy() {
        client.destroy();
    }

    public String post(String url, MultivaluedMap<String, String> formData) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        long sd = System.currentTimeMillis();
        WebResource resource = client.resource(url);
        ClientResponse response = null;
        try {
            response = resource.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse.class, formData);
            if (response.getStatus() != 200) {
                throw new Exception("Failed : url [" + url + "] HTTP error code : " + response.getStatus());
            }
            String result = response.getEntity(String.class);
            long ed = System.currentTimeMillis();
            LOGGER.info("url:[{}] ---sendData:{} ---cost:{}ms", url, formData, (ed - sd));
            return result;
        } catch (Throwable e) {
            LOGGER.error("post error:{}", e.getMessage(), e);
            throw new RuntimeException("Failed : url [" + url + "] error msg : " + e.getMessage());
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    public String get(String url) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        WebResource resource = client.resource(url);
        ClientResponse response = null;
        long sd = System.currentTimeMillis();
        try {
            response = resource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : url [" + url + "] HTTP error code : " + response.getStatus());
            }
            String result = response.getEntity(String.class);
            long ed = System.currentTimeMillis();
            LOGGER.info("url:[{}] ---cost:{}ms", url, (ed - sd));
            return result;
        } catch (Exception e) {
            LOGGER.error("get error:{}", e.getMessage(), e);
            throw new RuntimeException("Failed : url [" + url + "] error msg : " + e.getMessage());
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

}