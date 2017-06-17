package com.vermouth.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.vermouth.common.utils.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.Properties;

/**
 * Created by shenhui.ysh on 2017/6/16 0016.
 */
public class DataSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSource.class);
    private Properties properties;
    private DruidDataSource dataSource;
    private ThreadLocal<Connection> container;

    /** 关闭连接 **/
    public void closeConnection() {
        try {
            Connection conn = container.get();
            if (conn != null) {
                conn.close();
            }
        } catch (Throwable e) {
            LOGGER.error("close error:{}", e.getMessage(), e);
        } finally {
            container.remove();
        }
    }

    /** 回滚事务 **/
    public void rollback() {
        try {
            Connection conn = container.get();
            if (conn != null) {
                conn.rollback();
            }
        } catch (Throwable e) {
            LOGGER.error("rollback error:{}", e.getMessage(), e);
        }
    }

    /** 提交事务 **/
    public void commit() {
        try {
            Connection conn = container.get();
            if (conn != null) {
                conn.commit();
            }
        } catch (Throwable e) {
            LOGGER.error("commit error:{}", e.getMessage(), e);
        }
    }

    /** 获取当前线程上的连接开启事务 **/
    public void startTransaction() {
        try {
            Connection conn = container.get();
            if (conn == null) {
                conn = getConnection();
            }
            conn.setAutoCommit(false);
        } catch (Throwable e) {
            LOGGER.error("startTransaction error:{}", e.getMessage(), e);
        }
    }

    /** 获取数据库连接 **/
    public Connection getConnection() {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            container.set(conn);
        } catch (Throwable e) {
            LOGGER.error("getConnection error:{}", e.getMessage(), e);
        }
        return conn;
    }

    private void init() {
        if (dataSource == null) {
            dataSource = new DruidDataSource();
        }
        if (container == null) {
            container = new ThreadLocal<Connection>();
        }
        dataSource.setDriverClassName(properties.getProperty("mysql.connection.driver.class"));
        dataSource.setUrl(properties.getProperty("mysql.connection.test.url"));
        dataSource.setUsername(properties.getProperty("mysql.connection.test.username"));
        dataSource.setPassword(properties.getProperty("mysql.connection.test.password"));
    }

    private void loadProperties(String prop) {
        LOGGER.info("loadProperties prop:{}", prop);
        if (properties == null) {
            properties = PropertiesUtils.INSTANCE.getProperties(prop);
            init();
        }
    }

    public DataSource(String prop) {
        loadProperties(prop);
    }

    public DruidDataSource getDataSource() {
        return dataSource;
    }

}