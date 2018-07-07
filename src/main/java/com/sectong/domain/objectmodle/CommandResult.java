package com.sectong.domain.objectmodle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xueyong on 16/12/8.
 * mobileeasy-referal.
 */
public class CommandResult implements Serializable{

    private static final long serialVersionUID = 8906668583331354801L;

    public CommandResult() {

    }

    public CommandResult(List<Object> remoteCMDS, List<Object> lazyCMDS, String deviceMac) {
        this.remoteCMDS = remoteCMDS;
        this.lazyCMDS = lazyCMDS;
        this.deviceMac = deviceMac;
    }

    public List<Object> getRemoteCMDS() {
        return remoteCMDS;
    }

    public void setRemoteCMDS(List<Object> remoteCMDS) {
        this.remoteCMDS = remoteCMDS;
    }

    public List<Object> getLazyCMDS() {
        return lazyCMDS;
    }

    public void setLazyCMDS(List<Object> lazyCMDS) {
        this.lazyCMDS = lazyCMDS;
    }

    public String getDeviceMac() {
        return deviceMac;
    }

    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }

    private List<Object> remoteCMDS;
    private List<Object> lazyCMDS;
    private String deviceMac;
}
