package com.sectong.domain.mongomodle;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xueyong on 16/7/21.
 * mobileeasy.
 */
@Document
public class Orders implements Serializable {

    @Id
    private String id;
    private String user;
    private String deviceMac;
    private String order;
    private Date createTime;

    public Orders() {

    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDeviceMac() {
        return deviceMac;
    }

    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String trancefer() {
        return String.format("%s,%s", id, order);
    }
}
