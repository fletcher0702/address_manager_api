package com.fantech.addressmanager.api.dto.visit;

public class VisitDto {

    private String teamUuid;
    private String statusUuid;
    private String name;
    private String address;
    private String zoneUuid;
    private String date;
    private String observation;
    private String phoneNumber;

    public String getDate() {
        return date;
    }

    public String getObservation() {
        return observation;
    }

    public String getName() {
        return name;
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
