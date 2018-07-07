package com.sectong.infrared;

import java.util.List;

/**
 * Created by xueyong on 16/9/19.
 * mobileeasy.
 */
public class DeviceFunction {
    private String code;
    private String name;
    List<FunctionStatus> statusMap;

    public DeviceFunction(String code, String name, List<FunctionStatus> statusMap) {
        this.code = code;
        this.name = name;
        this.statusMap = statusMap;
    }

    public List<FunctionStatus> getStatusMap() {
        return statusMap;
    }

    public void setStatusMap(List<FunctionStatus> statusMap) {
        this.statusMap = statusMap;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
