package com.fantech.addressmanager.api.dto.status;

public class DeleteStatusDto {

    private String userUuid;
    private String teamUuid;
    private String statusUuid;

    public String getUserUuid() {
        return userUuid;
    }

    public String getTeamUuid() {
        return teamUuid;
    }

    public String getStatusUuid() {
        return statusUuid;
    }
}
