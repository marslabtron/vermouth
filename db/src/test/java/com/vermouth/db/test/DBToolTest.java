package com.vermouth.db.test;

import com.vermouth.db.DBTool;
import com.vermouth.db.DataSource;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by shenhui.ysh on 2017/6/16 0016.
 */
public class DBToolTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(DBToolTest.class);

    @Test
    public void test() {
        try {
            DataSource dataSource = new DataSource("application.properties");
            DBTool dbTool = new DBTool(dataSource);
            Map<String, Object> map = dbTool.queryMap("select * from qx_coupons limit 1", null);
            LOGGER.debug(map.toString());

            List<CouponsDo> couponsDoList = dbTool.queryList("select * from qx_coupons limit 20", null, CouponsDo.class);
            for (CouponsDo couponsDo : couponsDoList) {
                LOGGER.debug(couponsDo.toString());
            }
        }catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
