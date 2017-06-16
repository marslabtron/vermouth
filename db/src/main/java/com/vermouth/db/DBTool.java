package com.vermouth.db;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by shenhui.ysh on 2017/6/16 0016.
 */
public class DBTool<T> {
    private static Logger LOGGER = LoggerFactory.getLogger(DBTool.class);
    private DataSource dataSource;
    private Class clazz;

    public DBTool(Class clazz, DataSource dataSource) {
        this.clazz = clazz;
        this.dataSource = dataSource;
    }

    public Map<Object, T> queryBeanMap(String sql, Object[] params, int columnIndex) throws SQLException {
        log(sql, params);
        QueryRunner runner = new QueryRunner(dataSource.getDataSource());
        if (columnIndex <= 0) {
            return runner.query(sql, new BeanMapHandler<Object, T>(clazz), params);
        }
        return runner.query(sql, new BeanMapHandler<Object, T>(clazz, columnIndex), params);
    }

    /**
     * 用于获取所有结果集,将每行结果集转换为JavaBean作为value,并指定某列为key,封装到HashMap中,
     * 相当于对每行数据的做BeanHandler一样的处理后,再指定列值为Key封装到HashMap中 BeanMap
     *
     * @param sql
     * @param params
     * @param columnName
     * @return
     * @throws SQLException Map<Object,T>
     * @throws @since       1.0.0
     */
    public Map<Object, T> queryBeanMap(String sql, Object[] params, String columnName) throws SQLException {
        log(sql, params);
        QueryRunner runner = new QueryRunner(dataSource.getDataSource());
        if (StringUtils.isBlank(columnName)) {
            return runner.query(sql, new BeanMapHandler<Object, T>(clazz), params);
        }
        return runner.query(sql, new BeanMapHandler<Object, T>(clazz, columnName), params);
    }

    /**
     * 用于获取所有结果集,将每行结果集转换为Map<String,
     * Object>,并指定某列为key,可以简单的认为是一个双层Map,相当于先对每行数据执行MapHandler,
     * 再为其指定key添加到一个HaspMap中,KeyedHandler<K>中的<K>是指定的列值的类型 queryMapMap
     *
     * @param sql
     * @param params
     * @param columnName
     * @return
     * @throws SQLException Map<Object,Map<String,Object>>
     * @throws @since       1.0.0
     */
    public Map<Object, Map<String, Object>> queryMapMap(String sql, Object[] params, String columnName)
            throws SQLException {
        log(sql, params);
        QueryRunner runner = new QueryRunner(dataSource.getDataSource());
        if (StringUtils.isBlank(columnName)) {
            return runner.query(sql, new KeyedHandler<>(), params);
        }
        return runner.query(sql, new KeyedHandler<>(columnName), params);
    }

    public Map<Object, Map<String, Object>> queryMapMap(String sql, Object[] params, int columnIndex)
            throws SQLException {
        log(sql, params);
        QueryRunner runner = new QueryRunner(dataSource.getDataSource());
        if (columnIndex <= 0) {
            return runner.query(sql, new KeyedHandler<>(), params);
        }
        return runner.query(sql, new KeyedHandler<>(columnIndex), params);
    }

    /**
     * 根据列索引或列名获取结果集中某列的所有数据,并添加到ArrayList中,可以理解为ScalarHandler<T>的加强版
     * queryColumnList
     *
     * @param sql
     * @param params
     * @param columnIndex
     * @return
     * @throws SQLException List<Object>
     * @throws @since       1.0.0
     */
    public List<Object> queryColumnList(String sql, Object[] params, int columnIndex) throws SQLException {
        log(sql, params);
        QueryRunner runner = new QueryRunner(dataSource.getDataSource());
        if (columnIndex <= 0) {
            return runner.query(sql, new ColumnListHandler<>(), params);
        }
        return runner.query(sql, new ColumnListHandler<>(columnIndex), params);
    }

    public List<Object> queryColumnList(String sql, Object[] params, String columnName) throws SQLException {
        log(sql, params);
        QueryRunner runner = new QueryRunner(dataSource.getDataSource());
        if (StringUtils.isBlank(columnName)) {
            return runner.query(sql, new ColumnListHandler<>(), params);
        }
        return runner.query(sql, new ColumnListHandler<>(columnName), params);
    }

    public List<Map<String, Object>> queryMaps(String sql, Object[] params) throws SQLException {
        log(sql, params);
        QueryRunner runner = new QueryRunner(dataSource.getDataSource());
        return runner.query(sql, new MapListHandler(), params);
    }

    /**
     * 用于获取结果集中的第一行数据,并将其封装到一个Map中,Map中key是数据的列别名(as
     * label),如果没有就是列的实际名称,Map中value就是列的值,注意代表列的key不区分大小写 queryMap
     *
     * @param sql
     * @return
     * @throws SQLException Map<String,Object>
     * @throws @since       1.0.0
     */
    public Map<String, Object> queryMap(String sql, Object[] params) throws SQLException {
        log(sql, params);
        QueryRunner runner = new QueryRunner(dataSource.getDataSource());
        return runner.query(sql, new MapHandler(), params);
    }

    public List<Object[]> queryArrays(String sql, Object[] params) throws SQLException {
        log(sql, params);
        QueryRunner runner = new QueryRunner(dataSource.getDataSource());
        return runner.query(sql, new ArrayListHandler(), params);
    }

    /**
     * 用于获取结果集中的第一行数据,并将其封装到一个数组中,一列值对应一个数组元素 queryArray
     *
     * @param sql
     * @return
     * @throws SQLException Object[]
     * @throws @since       1.0.0
     */
    public Object[] queryArray(String sql, Object[] params) throws SQLException {
        log(sql, params);
        QueryRunner runner = new QueryRunner(dataSource.getDataSource());
        return runner.query(sql, new ArrayHandler(), params);
    }

    /**
     * 第一行第columnIndex列的数据 queryColumn
     *
     * @param sql
     * @param params
     * @param columnIndex 从1开始
     * @return
     * @throws SQLException Object
     * @throws @since       1.0.0
     */
    public Object queryColumn(String sql, Object[] params, int columnIndex) throws SQLException {
        log(sql, params);
        QueryRunner runner = new QueryRunner(dataSource.getDataSource());
        if (columnIndex <= 0) {
            return runner.query(sql, new ScalarHandler<>(), params);
        }
        return runner.query(sql, new ScalarHandler<>(columnIndex), params);
    }

    /**
     * 第一行列名为columnName的数据 queryColumn
     *
     * @param sql
     * @param params
     * @param columnName
     * @return
     * @throws SQLException Object
     * @throws @since       1.0.0
     */
    public Object queryColumn(String sql, Object[] params, String columnName) throws SQLException {
        log(sql, params);
        QueryRunner runner = new QueryRunner(dataSource.getDataSource());
        if (StringUtils.isBlank(columnName)) {
            return runner.query(sql, new ScalarHandler<>(), params);
        }
        return runner.query(sql, new ScalarHandler<>(columnName), params);
    }

    /**
     * 查询一行数据 query
     *
     * @param sql
     * @param params
     * @return
     * @throws SQLException T
     * @throws @since       1.0.0
     */
    public T query(String sql, Object[] params) throws SQLException {
        log(sql, params);
        QueryRunner runner = new QueryRunner(dataSource.getDataSource());
        return (T) runner.query(sql, new BeanHandler(clazz), params);
    }

    /**
     * 查询多行数据 queryList
     *
     * @param sql
     * @param params
     * @return
     * @throws SQLException List<T>
     * @throws @since       1.0.0
     */
    public List<T> queryList(String sql, Object[] params) throws SQLException {
        log(sql, params);
        QueryRunner runner = new QueryRunner(dataSource.getDataSource());
        return (List<T>) runner.query(sql, new BeanListHandler(clazz), params);
    }

    /**
     * 插入一行数据 add
     *
     * @param sql
     * @param params
     * @return
     * @throws SQLException int
     * @throws @since       1.0.0
     */
    public int add(String sql, Object[] params) throws SQLException {
        log(sql, params);
        QueryRunner runner = new QueryRunner(dataSource.getDataSource());
        return runner.update(dataSource.getConnection(), sql, params);
    }

    /**
     * 更新数据 update
     *
     * @param sql
     * @param params
     * @return
     * @throws SQLException int
     * @throws @since       1.0.0
     */
    public int update(String sql, Object[] params) throws SQLException {
        log(sql, params);
        QueryRunner runner = new QueryRunner(dataSource.getDataSource());
        return runner.update(dataSource.getConnection(), sql, params);
    }

    /**
     * 删除数据 delete
     *
     * @param sql
     * @param params
     * @return
     * @throws SQLException int
     * @throws @since       1.0.0
     */
    public int delete(String sql, Object[] params) throws SQLException {
        return update(sql, params);
    }

    /**
     * 统计数据行数 count
     *
     * @param sql
     * @param params
     * @return
     * @throws SQLException int
     * @throws @since       1.0.0
     */
    public int count(String sql, Object[] params) throws SQLException {
        log(sql, params);
        QueryRunner runner = new QueryRunner(dataSource.getDataSource());
        int count = runner.query(sql, new ResultSetHandler<Integer>() {
            @Override
            public Integer handle(ResultSet rs) throws SQLException {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        }, params);
        return count;
    }

    private void log(String sql, Object[] params) {
        LOGGER.info("SQL语句:[{}] 检索参数:[{}]", sql, Arrays.toString(params));
    }

}