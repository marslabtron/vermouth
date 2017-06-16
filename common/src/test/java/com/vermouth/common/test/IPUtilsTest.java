package com.vermouth.common.test;

import com.vermouth.common.utils.IPUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by shenhui.ysh on 2017/6/16 0016.
 */
public class IPUtilsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(IPUtilsTest.class);

    @Test
    public void test() {
        long st = System.currentTimeMillis();
        LOGGER.debug(IPUtils.getRandomIp());
        LOGGER.debug("cost:{}", (System.currentTimeMillis() - st));
    }

}
