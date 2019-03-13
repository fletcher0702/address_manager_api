package com.fantech.addressmanager.api.dto.visit;

public class DeleteVisitDto {

    private String userUuid;
    private String zoneUuid;
    private String visitUuid;

    public String getUserUuid() {
        return userUuid;
    }

    public String getZoneUuid() {
        return zoneUuid;
    }

    public String getVisitUuid() {
        return visitUuid;
    }
}
