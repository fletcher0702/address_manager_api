package com.fantech.addressmanager.api.dto.zone;

public class CreateZoneDto {

    private String userUuid;
    private String teamUuid;
    private String name;
    private String address;

    public String getUserUuid() {
        return userUuid;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getTeamUuid() {
        return teamUuid;
    }
}
