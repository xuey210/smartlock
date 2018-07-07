package com.sectong.domain.objectmodle;

import java.io.Serializable;

/**
 * Created by xueyong on 16/7/14.
 * mobileeasy.
 */
public class MainFrameRequest implements Serializable {

    private String user;
    private String deviceMac;
    private String room;
    private String model;
    private String order;
    private String action;
    private String status;
    private String result;
    private String device;
    private String weatherData;
    private String orderId;
    private String isNeedCallBack;
    private String eq;

    public String getEq() {
        return eq;
    }

    public void setEq(String eq) {
        this.eq = eq;
    }

    public String getIsNeedCallBack() {
        return isNeedCallBack;
    }

    public void setIsNeedCallBack(String isNeedCallBack) {
        this.isNeedCallBack = isNeedCallBack;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getWeatherData() {
        return weatherData;
    }

    public void setWeatherData(String weatherData) {
        this.weatherData = weatherData;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getDeviceMac() {
        return deviceMac;
    }

    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MainFrameRequest)) return false;

        MainFrameRequest that = (MainFrameRequest) o;

        return getRoom().equals(that.getRoom());

    }

    @Override
    public int hashCode() {
        return getRoom().hashCode();
    }
}
