package com.sectong.infrared;

/**
 * Created by xueyong on 16/9/9.
 * mobileeasy.
 */
public class FormatEntity {

    private String deviceId;
    private String fid;
    private String formatString;
    private String c3rv;

    public FormatEntity() {

    }

    public FormatEntity(String c3rv, String deviceId, String fid, String formatString) {
        this.c3rv = c3rv;
        this.deviceId = deviceId;
        this.fid = fid;
        this.formatString = formatString;
    }

    public String getC3rv() {
        return c3rv;
    }

    public void setC3rv(String c3rv) {
        this.c3rv = c3rv;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getFormatString() {
        return formatString;
    }

    public void setFormatString(String formatString) {
        this.formatString = formatString;
    }
}
