package com.fantech.addressmanager.api.dto.visit;

public class UpdateVisitHistoryDto {

    private String teamUuid;
    private String zoneUuid;
    private String userUuid;
    private String visitUuid;
    private String date;

    public String getTeamUuid() {
        return teamUuid;
    }

    public String getZoneUuid() {
        return zoneUuid;
    }

    public String getVisitUuid() {
        return visitUuid;
    }

    public String getDate() {
        return date;
    }

    public String getUserUuid() {
        return userUuid;
    }
}
