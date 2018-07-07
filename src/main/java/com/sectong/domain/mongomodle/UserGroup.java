package com.sectong.domain.mongomodle;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * Created by xueyong on 16/7/14.
 * mobileeasy.
 */

@Document
public class UserGroup implements Serializable {

    @Id
    private String id;
    private String username;
    private String subUsername;

    public UserGroup() {

    }

    public UserGroup(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubUsername() {
        return subUsername;
    }

    public void setSubUsername(String subUsername) {
        this.subUsername = subUsername;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
