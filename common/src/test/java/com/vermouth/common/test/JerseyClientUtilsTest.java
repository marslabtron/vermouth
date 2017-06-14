package com.vermouth.common.test;

import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.vermouth.common.utils.JerseyClientUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MultivaluedMap;

/**
 * Created by shenhui.ysh on 2017/6/14 0014.
 */
public class JerseyClientUtilsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(JerseyClientUtilsTest.class);

    @Test
    public void test() {
        try {
            JerseyClientUtils clientUtils = new JerseyClientUtils();
            clientUtils.init();

            for (int i = 0; i < 1; ++i) {
                MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
                formData.add("appKey", "app");
                formData.add("data", "{\"1\":\"1\"}");
                formData.add("domain", "");
                formData.add("secondsExpired", "3600");

                long st = System.currentTimeMillis();
                String response = clientUtils.post("http://flyway-test.wz-inc.com/rest/mediator/applyData", formData);
                LOGGER.debug("response:{}, cost:{}", response, (System.currentTimeMillis() - st));
            }
        }catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
