package com.vermouth.common.test;

import com.vermouth.common.utils.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by shenhui.ysh on 2017/6/16 0016.
 */
public class MD5UtilsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MD5UtilsTest.class);

    @Test
    public void test(){
        LOGGER.debug(StringUtils.equals(MD5Utils.string2MD5("admin"), "21232F297A57A5A743894A0E4A801FC3")+"");
    }

}
