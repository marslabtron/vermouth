package com.vermouth.common.exception;

/**
 * Created by shenhui.ysh on 2017/6/13 0013.
 */
public class LinuxShellException extends Exception {
    public LinuxShellException() {
    }

    public LinuxShellException(String message) {
        super(message);
    }

    public LinuxShellException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public LinuxShellException(Throwable throwable) {
        super(throwable);
    }

}