package com.qf.v13.common.pojo;

import java.io.Serializable;

/**
 * @author blxf
 * @Date ${Date}
 */
public class WangResultBean implements Serializable {
    private String errno;
    private  String[] data;

    public String getErrno() {
        return errno;
    }

    public void setErrno(String errno) {
        this.errno = errno;
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    public WangResultBean(String errno, String[] data) {
        this.errno = errno;
        this.data = data;
    }

    public WangResultBean() {
    }
}
