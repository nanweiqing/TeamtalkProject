package com.mogujie.tt.net.request;

/**
 * 网络请求的头部
 * Created by gy on 2016/6/8.
 */
public class MsgHeader {

    private String serviceCode;
    private String version;
    private String sysPlatCode;
    private String sentTime;
    private String expTime;
    private String sMessageNo;

    public MsgHeader(String serviceCode, String version, String sysPlatCode, String sentTime, String expTime, String sMessageNo) {
        this.serviceCode = serviceCode;
        this.version = version;
        this.sysPlatCode = sysPlatCode;
        this.sentTime = sentTime;
        this.expTime = expTime;
        this.sMessageNo = sMessageNo;
    }

    public MsgHeader() {
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSysPlatCode() {
        return sysPlatCode;
    }

    public void setSysPlatCode(String sysPlatCode) {
        this.sysPlatCode = sysPlatCode;
    }

    public String getSentTime() {
        return sentTime;
    }

    public void setSentTime(String sentTime) {
        this.sentTime = sentTime;
    }

    public String getExpTime() {
        return expTime;
    }

    public void setExpTime(String expTime) {
        this.expTime = expTime;
    }

    public String getsMessageNo() {
        return sMessageNo;
    }

    public void setsMessageNo(String sMessageNo) {
        this.sMessageNo = sMessageNo;
    }
}
