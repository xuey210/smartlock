package com.sectong.domain.mongomodle;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by xueyong on 16/12/13.
 * mobileeasy-referal.
 */
@Entity
public class RoomAndEq implements Serializable {



    private static final long serialVersionUID = 2692865444862278348L;

    public RoomAndEq() {

    }
    @Id
    private String id;
    private String user;
    private String room;
    private String eq;
    private Date createTime;
    private String model;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getEq() {
        return eq;
    }

    public void setEq(String eq) {
        this.eq = eq;
    }
}
