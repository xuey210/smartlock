package com.sectong.domain.objectmodle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xueyong on 16/12/15.
 * mobileeasy-referal.
 */
public class DevicesOfRoomResult implements Serializable{


    private static final long serialVersionUID = 158482978376187113L;

    public DevicesOfRoomResult() {

    }

    private String roomName;
    private List<String> devices;

    public DevicesOfRoomResult(String roomName, List<String> devices) {
        this.roomName = roomName;
        this.devices = devices;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public List<String> getDevices() {
        return devices;
    }

    public void setDevices(List<String> devices) {
        this.devices = devices;
    }
}
