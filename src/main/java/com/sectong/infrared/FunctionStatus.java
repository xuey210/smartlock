package com.sectong.infrared;

/**
 * Created by xueyong on 16/9/19.
 * mobileeasy.
 */
public class FunctionStatus {

    private String status;
    private String rcode;

    public FunctionStatus(String status) {
        this.status = status;
    }

    public String getRcode() {
        return rcode;
    }

    public void setRcode(String rcode) {
        this.rcode = rcode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
