package com.vermouth.common.test;

import com.vermouth.common.utils.FileUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenhui.ysh on 2017/6/14 0014.
 */
public class FileUtilsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtilsTest.class);

    @Test
    public void test() {
        try {
            List<String> list = new ArrayList<>();
            FileUtils.loadFileLines("D:\\Users\\shenhui.ysh\\Desktop\\重要.txt", list);
            for (String s : list) {
                LOGGER.debug(s);
            }
            LOGGER.debug("----------------------------------------");
            FileUtils.loadDataFromFile(new FileUtils.ReaderHandler<Boolean>() {
                @Override
                public Boolean handle(BufferedReader reader) throws IOException {
                    String line = reader.readLine();
                    while (line != null) {
                        LOGGER.debug(line);
                        line = reader.readLine();
                    }
                    return Boolean.TRUE;
                }
            }, "D:\\Users\\shenhui.ysh\\Desktop\\重要.txt");
        } catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
