package com.fantech.addressmanager.api.dto.visit;

public class VisitDto {

    private String teamUuid;
    private String name;
    private String address;
    private String zoneUuid;
    private String status;
    private String phoneNumber;

    public String getTeamUuid() {
        return teamUuid;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getZoneUuid() {
        return zoneUuid;
    }

    public String getStatus() {
        return status;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
