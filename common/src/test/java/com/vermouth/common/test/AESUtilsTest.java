package com.vermouth.common.test;

import com.vermouth.common.utils.AESUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by shenhui.ysh on 2017/6/14 0014.
 */
public class AESUtilsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(AESUtilsTest.class);

    @Test
    public void test() {
        try {
            for (int i = 0; i < 1; ++i) {
                long st = System.currentTimeMillis();
                String s = AESUtils.parseByte2HexStr(AESUtils.encrypt128("admin"+i, "pwd"));
                LOGGER.debug(s);
                LOGGER.debug(new String(AESUtils.decrypt128(AESUtils.parseHexStr2Byte(s), "pwd")));
                LOGGER.debug("cost:{}", (System.currentTimeMillis() - st));
            }
        }catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
