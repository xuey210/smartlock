package com.sectong.domain.objectmodle;

import java.io.Serializable;

/**
 * Created by xueyong on 16/7/27.
 * mobileeasy.
 */
public class JsonPushObject implements Serializable {

    private String action;
    private String content;

    public JsonPushObject(String action, String content) {
        this.action = action;
        this.content = content;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
