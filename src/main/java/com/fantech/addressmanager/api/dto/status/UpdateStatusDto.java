package com.fantech.addressmanager.api.dto.status;

public class UpdateStatusDto {

    private String userUuid;
    private String teamUuid;
    private String statusUuid;
    private StatusDto status;

    public String getUserUuid() {
        return userUuid;
    }

    public String getTeamUuid() {
        return teamUuid;
    }

    public String getStatusUuid() {
        return statusUuid;
    }

    public StatusDto getStatus() {
        return status;
    }
}
