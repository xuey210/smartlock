package com.sectong.domain.mongomodle;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * Created by xueyong on 16/7/21.
 * mobileeasy.
 */
@Document
public class Version implements Serializable {

    @Id
    private String id;
    private String weatherstation;
    private String device_MAC;
    private String device_software;
    private String version;

    public Version() {

    }

    public String getDevice_MAC() {
        return device_MAC;
    }

    public void setDevice_MAC(String device_MAC) {
        this.device_MAC = device_MAC;
    }

    public String getDevice_software() {
        return device_software;
    }

    public void setDevice_software(String device_software) {
        this.device_software = device_software;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getWeatherstation() {
        return weatherstation;
    }

    public void setWeatherstation(String weatherstation) {
        this.weatherstation = weatherstation;
    }
}
