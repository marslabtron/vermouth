package com.vermouth.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.Collection;

/**
 * Created by shenhui.ysh on 2017/6/14 0014.
 */
public class FileUtils {
    private static final String CHARSET = "UTF-8";

    private FileUtils() {}

    /**
     * 创建文件,如果这个文件存在,直接返回这个文件
     *
     * @param fullFilePath 文件的全路径,使用POSIX风格
     * @return 文件
     * @throws IOException
     */
    public static File touch(String fullFilePath) throws IOException {
        File file = new File(fullFilePath);
        file.getParentFile().mkdirs();
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    /**
     * 创建文件夹,如果存在直接返回此文件夹
     *
     * @param dirPath 文件夹路径,使用POSIX格式,无论哪个平台
     * @return 创建的目录
     */
    public static File mkDir(String dirPath) {
        File dir = new File(dirPath);
        dir.mkdirs();
        return dir;
    }

    /**
     * 文件是否存在
     *
     * @param path 文件路径
     * @return 是否存在
     */
    public static boolean isExist(String path) {
        return new File(path).exists();
    }

    /**
     * 获得一个带缓存的写入对象
     *
     * @param path     输出路径,绝对路径
     * @param charset  字符集
     * @param isAppend 是否追加
     * @return BufferedReader对象
     * @throws IOException
     */
    public static BufferedWriter getBufferedWriter(String path, String charset,
                                                   boolean isAppend) throws IOException {
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                touch(path), isAppend), charset));
    }

    /**
     * 清空一个目录
     *
     * @param dirPath 需要删除的文件夹路径
     */
    public static void cleanDir(String dirPath) {
        File dir = new File(dirPath);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    cleanDir(file.getAbsolutePath());
                }
                file.delete();
            }
        }
    }

    /**
     * 获得一个文件读取器
     *
     * @param path    绝对路径
     * @param charset 字符集
     * @return BufferedReader对象
     * @throws IOException
     */
    public static BufferedReader getBufferedReader(String path, String charset)
            throws IOException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(
                path), charset));
    }

    /**
     * 获得一个文件读取器
     *
     * @param path classpath下的文件
     * @return
     * @throws IOException
     */
    public static BufferedReader getBufferedReaderClasspath(String path)
            throws IOException {
        return new BufferedReader(new InputStreamReader(FileUtils.class.getClassLoader().getResourceAsStream(path)));
    }

    /**
     * 从文件中读取每一行数据
     *
     * @param path       文件路径
     * @param charset    字符集
     * @param collection 集合
     * @return 文件中的每行内容的集合
     * @throws IOException
     */
    public static <T extends Collection<String>> T loadFileLines(String path,
                                                                 String charset, T collection) throws IOException {
        BufferedReader reader = getBufferedReader(path, charset);
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            collection.add(line);
        }
        if (reader != null) {
            reader.close();
        }
        return collection;
    }

    public static <T extends Collection<String>> T loadFileLines(String path, T collection) throws IOException {
        return loadFileLines(path, CHARSET, collection);
    }

    public static <T extends Collection<String>> T loadFileLinesClasspath(String path, T collection) throws IOException {
        BufferedReader reader = getBufferedReaderClasspath(path);
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            collection.add(line);
        }
        if (reader != null) {
            reader.close();
        }
        return collection;
    }

    /**
     * 按照给定的readerHandler读取文件中的数据
     *
     * @param readerHandler Reader处理类
     * @param path          文件的绝对路径
     * @param charset       字符集
     * @return 从文件中load出的数据
     * @throws IOException
     */
    public static <T> T loadDataFromFile(ReaderHandler<T> readerHandler,
                                         String path, String charset) throws IOException {
        if (StringUtils.isBlank(charset)) {
            charset = CHARSET;
        }
        BufferedReader reader = null;
        T result = null;
        try {
            reader = getBufferedReader(path, charset);
            result = readerHandler.handle(reader);
        } catch (IOException e) {
            throw new IOException(e);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return result;
    }

    public static <T> T loadDataFromFile(ReaderHandler<T> readerHandler, String path) throws IOException {
        return loadDataFromFile(readerHandler, path, CHARSET);
    }

    /**
     * 获得文件的扩展名
     *
     * @param fileName 文件名
     * @return 扩展名
     */
    public static String getExtension(String fileName) {
        if (fileName == null) {
            return null;
        }
        int index = fileName.lastIndexOf(".");
        if (index == -1) {
            return "";
        } else {
            String ext = fileName.substring(index + 1);
            // 扩展名中不能包含路径相关的符号
            return (ext.contains("/") || ext.contains("\\")) ? "" : ext;
        }
    }

    /**
     * Reader处理接口
     *
     * @author yangshenhui
     */
    public interface ReaderHandler<T> {
        public T handle(BufferedReader reader) throws IOException;
    }

}