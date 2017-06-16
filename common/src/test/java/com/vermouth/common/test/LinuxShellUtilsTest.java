package com.vermouth.common.test;

import com.vermouth.common.pojo.LinuxShellPojo;
import com.vermouth.common.utils.LinuxShellUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by shenhui.ysh on 2017/6/13 0013.
 */
public class LinuxShellUtilsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LinuxShellUtilsTest.class);

    @Test
    public void executeLocal() {
        try {
            long stt = System.currentTimeMillis();
            long st = System.currentTimeMillis();
            LinuxShellUtils shellUtils = new LinuxShellUtils();
            for (int i = 0; i < 1; ++i) {
                LinuxShellPojo pojo = shellUtils.executeLocal("java -version");
                LOGGER.debug("cost:{}, {}",System.currentTimeMillis() - st , pojo != null ? pojo.toString() : null);
                st = System.currentTimeMillis();
            }
            LOGGER.debug("total cost:{}", (System.currentTimeMillis() - stt));
        }catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Test
    public void execute() {
        try {
            long stt = System.currentTimeMillis();
            long st = System.currentTimeMillis();
            LinuxShellUtils shellUtils = new LinuxShellUtils("ubuntu-1", "root", "admin");
            // 超时设置为1秒
            // shellUtils.setTIME_OUT(1000);
            // shellUtils.init();
            for (int i = 0; i < 1; ++i) {
                LinuxShellPojo pojo = shellUtils.execute("ps -ef | grep 22");
                LOGGER.debug("cost:{}, {}",System.currentTimeMillis() - st , pojo != null ? pojo.toString() : null);
                st = System.currentTimeMillis();
            }
            LOGGER.debug("total cost:{}", (System.currentTimeMillis() - stt));
        }catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
