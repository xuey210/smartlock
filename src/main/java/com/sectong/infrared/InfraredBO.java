package com.sectong.infrared;

import java.util.List;

/**
 * Created by xueyong on 16/9/9.
 * mobileeasy.
 */
public class InfraredBO {

    private String brands;
    private String device;
    private List<Codes> models;

    public String getBrands() {
        return brands;
    }

    public void setBrands(String brands) {
        this.brands = brands;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public List<Codes> getModels() {
        return models;
    }

    public void setModels(List<Codes> models) {
        this.models = models;
    }
}