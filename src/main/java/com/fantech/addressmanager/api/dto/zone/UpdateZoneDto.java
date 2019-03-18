package com.fantech.addressmanager.api.dto.zone;

public class UpdateZoneDto {

    private String userUuid;
    private String teamUuid;
    private String zoneUuid;
    private String name;
    private String address;

    public String getUserUuid() {
        return userUuid;
    }

    public String getTeamUuid() {
        return teamUuid;
    }

    public String getZoneUuid() {
        return zoneUuid;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
