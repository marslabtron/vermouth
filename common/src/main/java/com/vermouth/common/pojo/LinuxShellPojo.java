package com.vermouth.common.pojo;

/**
 * Created by shenhui.ysh on 2017/6/13 0013.
 */
public class LinuxShellPojo {
    private int mark;
    private String outStr;
    private String outErr;

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public String getOutStr() {
        return outStr;
    }

    public void setOutStr(String outStr) {
        this.outStr = outStr;
    }

    public String getOutErr() {
        return outErr;
    }

    public void setOutErr(String outErr) {
        this.outErr = outErr;
    }

    @Override
    public String toString() {
        return "LinuxShellPojo{" +
                "mark=" + mark +
                ", outStr='" + outStr + '\'' +
                ", outErr='" + outErr + '\'' +
                '}';
    }
}