package com.vermouth.common.test;

import com.vermouth.common.utils.HttpRequestUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by shenhui.ysh on 2017/6/30 0030.
 */
public class HttpRequestUtilsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestUtilsTest.class);

    @Test
    public void test() throws Throwable {
        LOGGER.debug(HttpRequestUtils.get("https://www.baidu.com/"));
    }

}
