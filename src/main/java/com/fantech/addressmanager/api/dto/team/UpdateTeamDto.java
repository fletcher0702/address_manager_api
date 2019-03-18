package com.fantech.addressmanager.api.dto.team;

public class UpdateTeamDto {

    private String userUuid;
    private String teamUuid;
    private String name;

    public String getUserUuid() {
        return userUuid;
    }

    public String getTeamUuid() {
        return teamUuid;
    }

    public String getName() {
        return name;
    }
}
