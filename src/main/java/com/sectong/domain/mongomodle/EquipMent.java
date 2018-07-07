package com.sectong.domain.mongomodle;

import java.io.Serializable;

/**
 * Created by xueyong on 16/7/20.
 * mobileeasy.
 */

public class EquipMent implements Serializable {

    private String name;
    private String status;

    public EquipMent(String name, String status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
