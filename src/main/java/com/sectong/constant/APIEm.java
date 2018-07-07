package com.sectong.constant;

/**
 * Created by xueyong on 16/6/27.
 * demo.
 */
public enum APIEm {

    SUCCESS(2000,"success"),
    NOTBINDING(2002, "user has not yet binding mac"),
    ALLREADYBIND(2003, "allready bind"),
    SUBUSERFILE(2004, "invaild user, not register or allready bind"),
    NOTFOUNDUSERMAC(2005, "can not matching user and deviceMac"),
    INVALIDCODE(2006, "verfication code is invalid."),
    VERIFICATIONCODEINCORRECT(2006, "verfication code incorrect."),
    FAIL(2001, "failed");

    private int code;
    private String message;

    APIEm(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
