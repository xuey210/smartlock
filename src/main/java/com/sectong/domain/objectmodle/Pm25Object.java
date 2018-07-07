package com.sectong.domain.objectmodle;

/**
 * Created by xueyong on 16/9/29.
 * mobileeasy.
 */
public class Pm25Object {

    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Pm25Object() {

    }

    public Pm25Object(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
