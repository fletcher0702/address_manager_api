package com.fantech.addressmanager.api.dto.visit;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UpdateVisitDto {


    private String userUuid;
    private String teamUuid;
    private String zoneUuid;
    private String visitUuid;
    private String statusUuid;
    private String name;
    @JsonIgnore
    private String address;
    @JsonIgnore
    private String phoneNumber;

    public String getName() {
        return name;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public String getVisitUuid() {
        return visitUuid;
    }

    public String getTeamUuid() {
        return teamUuid;
    }

    public String getStatusUuid() {
        return statusUuid;
    }

    public String getAddress() {
        return address;
    }

    public String getZoneUuid() {
        return zoneUuid;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
