package com.fantech.addressmanager.api.dto.zone;

public class ZoneDto {

    private String userUuid;
    private String name;
    private String address;

    public ZoneDto(String userUuid,String name, String address) {
        this.userUuid = userUuid;
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getUserUuid() {
        return userUuid;
    }
}
