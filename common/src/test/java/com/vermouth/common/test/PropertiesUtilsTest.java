package com.vermouth.common.test;

import com.vermouth.common.utils.PropertiesUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by shenhui.ysh on 2017/6/16 0016.
 */
public class PropertiesUtilsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtilsTest.class);

    @Test
    public void test() {
        try {
            LOGGER.debug(PropertiesUtils.INSTANCE.getProperties("application.properties").toString());
        }catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
