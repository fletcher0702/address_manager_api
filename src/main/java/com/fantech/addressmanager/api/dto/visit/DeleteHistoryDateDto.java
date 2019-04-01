package com.fantech.addressmanager.api.dto.visit;

public class DeleteHistoryDateDto {

    private String userUuid;
    private String teamUuid;
    private String zoneUuid;
    private String visitUuid;
    private String date;

    public String getUserUuid() {
        return userUuid;
    }

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
}
