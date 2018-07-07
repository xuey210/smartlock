package com.sectong.infrared;

/**
 * Created by xueyong on 16/9/8.
 * mobileeasy.
 */
public enum DeviceEnum {

    TV("TV", 2), AC("AC", 1), STB("STB", 3), DVD("DVD", 4),
    FAN("FAN", 5), ACL("ACL", 6), IPTV("IPTV", 7);

    private String name;
    private int id;

    public static int getIdByName(String name) {
        for (DeviceEnum deviceEnum : DeviceEnum.values()) {
            if (deviceEnum.getName().equals(name)) {
                return deviceEnum.getId();
            }
        }
        return 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    DeviceEnum(String name, int id) {
        this.name = name;
        this.id = id;

    }
}
