package com.vermouth.common.test;

import com.vermouth.common.utils.AuthUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shenhui.ysh on 2017/6/30 0030.
 */
public class AuthUtilsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthUtilsTest.class);

    @Test
    public void test(){
        Map<String, String> map = new HashMap<>();
        map.put("a", "a");
        map.put("b", "b");

        String sign = AuthUtils.sign(map, AuthUtils.AuthSecret.V100.getSecretKey());
        LOGGER.debug(sign);
        LOGGER.debug(AuthUtils.verify(map, AuthUtils.AuthSecret.V100.getSecretKey(), sign)+"");
    }

}
