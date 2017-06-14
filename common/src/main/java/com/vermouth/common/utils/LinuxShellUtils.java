package com.vermouth.common.utils;

import ch.ethz.ssh2.*;
import com.vermouth.common.exception.LinuxShellException;
import com.vermouth.common.pojo.LinuxShellPojo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by shenhui.ysh on 2017/6/13 0013.
 */
public class LinuxShellUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(LinuxShellUtils.class);
    private static final String CHARSET = "UTF-8";
    private int TIME_OUT = 1000 * 60 * 3;
    private Connection connection;
    private String ip;
    private int port;
    private String username;
    private String password;

    private boolean isAuthenticateSuccess;

    public LinuxShellUtils() {}

    public LinuxShellUtils(String ip, String username, String password) {
        this(ip, 0, username, password);
    }

    public LinuxShellUtils(String ip, int port, String username, String password) {
        this.ip = ip;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public void init() {
        try {
            if (port == 0) {
                connection = new Connection(ip);
            } else {
                connection = new Connection(ip, port);
            }
            connection.connect();
            isAuthenticateSuccess = connection.authenticateWithPassword(username, password);
            LOGGER.info("connection to ip:{} port:{} username:{} isAuthenticateSuccess:{}", ip, port, username, isAuthenticateSuccess);
        } catch (Throwable e) {
            LOGGER.error("LinuxShellUtils init error:{}", e.getMessage(), e);
        }
    }

    public LinuxShellPojo execute(String command) throws LinuxShellException {
        LOGGER.info("execute shell command:{}", command);
        if (StringUtils.isBlank(command)) {
            throw new LinuxShellException("command is null");
        }

        InputStream stdOut = null;
        InputStream stdErr = null;
        LinuxShellPojo linuxShellPojo = new LinuxShellPojo();
        int mark = -1;
        try {
            if (isAuthenticateSuccess) {
                Session session = connection.openSession();
                session.execCommand(command);
                stdOut = new StreamGobbler(session.getStdout());
                linuxShellPojo.setOutStr(processStream(stdOut));
                stdErr = new StreamGobbler(session.getStderr());
                linuxShellPojo.setOutErr(processStream(stdErr));
                /**
                 * 一般情况下shell脚本正常执行完毕,getExitStatus方法返回0, 此方法通过远程命令取得Exit
                 * Code/status,但并不是每个server设计时都会返回这个值,如果没有则会返回null,
                 * 在调用getExitStatus时,要先调用WaitForCondition方法,通过ChannelCondition.
                 * java接口的定义可以看到每个条件的具体含义
                 */
                session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);
                mark = session.getExitStatus().intValue();
                session.close();
            } else {
                throw new LinuxShellException("isAuthenticateSuccess is false");
            }
            linuxShellPojo.setMark(mark);
        } catch (Throwable e) {
            throw new LinuxShellException(e);
        } finally {
            close(stdOut, stdErr);
        }
        return linuxShellPojo;
    }

    public LinuxShellPojo executeLocal(String command) throws LinuxShellException {
        LOGGER.info("execute local shell command [" + command + "]");
        if (StringUtils.isEmpty(command)) {
            return null;
        }

        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        InputStream stdOut = null;
        InputStream stdErr = null;
        LinuxShellPojo linuxShellPojo = new LinuxShellPojo();
        int mark = -1;
        try {
            process = runtime.exec(command);
            stdOut = new StreamGobbler(process.getInputStream());
            linuxShellPojo.setOutStr(processStream(stdOut));
            stdErr = new StreamGobbler(process.getErrorStream());
            linuxShellPojo.setOutErr(processStream(stdErr));
            mark = process.exitValue();
            linuxShellPojo.setMark(mark);
        } catch (Throwable e) {
            throw new LinuxShellException(e);
        } finally {
            close(stdOut, stdErr);
        }
        return linuxShellPojo;
    }

    public void executeScpUpload(String[] localFiles, String remoteDirectory) throws LinuxShellException {
        LOGGER.info("scp upload {} to {}:{}", array2String(localFiles), ip, remoteDirectory);
        if (localFiles == null || localFiles.length == 0) {
            throw new LinuxShellException("localFiles is null");
        }
        if (StringUtils.isBlank(remoteDirectory)) {
            throw new LinuxShellException("remoteDirectory is null");
        }

        try {
            if (isAuthenticateSuccess) {
                SCPClient scpClient = new SCPClient(connection);
                scpClient.put(localFiles, remoteDirectory);
            } else {
                throw new LinuxShellException("isAuthenticateSuccess is false");
            }
        } catch (Throwable e) {
            throw new LinuxShellException(e);
        } finally {
            close(null, null);
        }
    }

    public void executeScpUpload(String localFile, String remoteDirectory) throws LinuxShellException {
        executeScpUpload(new String[]{localFile}, remoteDirectory);
    }

    public void executeScpDownload(String[] remoteFiles, String localDirectory) throws LinuxShellException {
        LOGGER.info("scp download {} to {}:{}", array2String(remoteFiles), ip, localDirectory);
        if (remoteFiles == null || remoteFiles.length == 0) {
            throw new LinuxShellException("remoteFiles is null");
        }
        if (StringUtils.isBlank(localDirectory)) {
            throw new LinuxShellException("localDirectory is null");
        }

        try {
            if (isAuthenticateSuccess) {
                SCPClient scpClient = new SCPClient(connection);
                scpClient.get(remoteFiles, localDirectory);
            } else {
                throw new LinuxShellException("isAuthenticateSuccess is false");
            }
        } catch (Throwable e) {
            throw new LinuxShellException(e);
        } finally {
            close(null, null);
        }
    }

    public void executeScpDownload(String remoteFile, String localDirectory) throws LinuxShellException {
        executeScpDownload(new String[]{remoteFile}, localDirectory);
    }

    private void close(InputStream stdOut, InputStream stdErr) throws LinuxShellException {
        try {
            if (stdOut != null) {
                stdOut.close();
            }
            if (stdErr != null) {
                stdErr.close();
            }
        } catch (Throwable e) {
            throw new LinuxShellException(e);
        }
    }

    private String processStream(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }
        byte[] temp = new byte[1024 * 4];
        StringBuilder stringBuilder = new StringBuilder();
        while (inputStream.read(temp) != -1) {
            stringBuilder.append(new String(temp, CHARSET));
        }
        return stringBuilder.toString().trim();
    }

    private String array2String(String[] array) {
        if (array == null || array.length == 0) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder("[");
        for (int i = 0, l = array.length; i < l; ++i) {
            if (i == l - 1) {
                stringBuilder.append(array[i]);
            } else {
                stringBuilder.append(array[i]).append(",");
            }
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTIME_OUT() {
        return TIME_OUT;
    }

    public void setTIME_OUT(int TIME_OUT) {
        this.TIME_OUT = TIME_OUT;
    }

    @Override
    public String toString() {
        return "LinuxShellUtils{" +
                "TIME_OUT=" + TIME_OUT +
                ", connection=" + connection +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", isAuthenticateSuccess=" + isAuthenticateSuccess +
                '}';
    }
}