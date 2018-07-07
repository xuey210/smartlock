package com.sectong.domain.mongomodle;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by xueyong on 16/7/14.
 * mobileeasy.
 */
public class RoomConvter implements Serializable {

    private String name;
    private String currentModel;
    private List<EquipMent> equipMents;
    private String[] models;

    public RoomConvter(Room room) {
        this.name = room.getName();
        this.currentModel = room.getCurrentModel();
        if (room.getEquipment() != null) {
            this.equipMents = room.getEquipment().entrySet().stream().map(m -> m.getValue()).collect(Collectors.toList());
        }
        this.models = room.getModels();
    }

    public String[] getModels() {
        return models;
    }

    public void setModels(String[] models) {
        this.models = models;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrentModel() {
        return currentModel;
    }

    public void setCurrentModel(String currentModel) {
        this.currentModel = currentModel;
    }

    public List<EquipMent> getEquipMents() {
        return equipMents;
    }

    public void setEquipMents(List<EquipMent> equipMents) {
        this.equipMents = equipMents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoomConvter)) return false;

        RoomConvter room = (RoomConvter) o;

        return getName().equals(room.getName());

    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}
