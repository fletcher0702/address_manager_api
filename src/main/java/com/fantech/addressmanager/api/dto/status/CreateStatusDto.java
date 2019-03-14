package com.fantech.addressmanager.api.dto.status;

import java.util.List;

public class CreateStatusDto {

    private String userUuid;
    private String teamUuid;
    private List<StatusDto> status;

    public String getUserUuid() {
        return userUuid;
    }

    public String getTeamUuid() {
        return teamUuid;
    }

    public List<StatusDto> getStatus() {
        return status;
    }
}
