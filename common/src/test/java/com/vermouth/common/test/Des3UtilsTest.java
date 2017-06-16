package com.vermouth.common.test;

import com.vermouth.common.utils.Des3Utils;
import com.vermouth.common.utils.HexUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by shenhui.ysh on 2017/6/16 0016.
 */
public class Des3UtilsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(Des3UtilsTest.class);

    @Test
    public void test() {
        String s = HexUtils.parseByte2HexStr(Des3Utils.des3Crypt("ADMIN"));
        LOGGER.debug(s);
        LOGGER.debug(new String(Des3Utils.des3Decrypt(HexUtils.parseHexStr2Byte(s))));
    }

}
