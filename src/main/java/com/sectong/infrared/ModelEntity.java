package com.sectong.infrared;

/**
 * Created by xueyong on 16/9/9.
 * mobileeasy.
 */
public class ModelEntity {
    public ModelEntity() {

    }

    public ModelEntity(String keyFile, String keySquency) {
        this.keyFile = keyFile;
        this.keySquency = keySquency;
    }

    private String keyFile;
    private String keySquency;
    private String formatId;

    public String getFormatId() {
        return formatId;
    }

    public void setFormatId(String formatId) {
        this.formatId = formatId;
    }

    public String getKeyFile() {
        return keyFile;
    }

    public void setKeyFile(String keyFile) {
        this.keyFile = keyFile;
    }

    public String getKeySquency() {
        return keySquency;
    }

    public void setKeySquency(String keySquency) {
        this.keySquency = keySquency;
    }
}
