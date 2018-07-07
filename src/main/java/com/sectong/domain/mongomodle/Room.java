package com.sectong.domain.mongomodle;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xueyong on 16/7/14.
 * mobileeasy.
 */
public class Room implements Serializable {

    public Room() {

    }
    public Room(String name, String currentModel) {
        this.name = name;
        this.currentModel = currentModel;
    }

    public Room(String name) {
        this.name = name;
    }

    private String name;
    private String currentModel;
    private ConcurrentHashMap<String,EquipMent> equipment;
    private String[] models;

    public String[] getModels() {
        return models;
    }

    public void setModels(String[] models) {
        this.models = models;
    }

    public ConcurrentHashMap<String, EquipMent> getEquipment() {
        return equipment;
    }

    public void setEquipment(ConcurrentHashMap<String, EquipMent> equipment) {
        this.equipment = equipment;
    }

    public String getCurrentModel() {
        return currentModel;
    }

    public void setCurrentModel(String currentModel) {
        this.currentModel = currentModel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room)) return false;

        Room room = (Room) o;

        return getName().equals(room.getName());

    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}
