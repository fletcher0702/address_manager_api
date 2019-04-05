package com.fantech.addressmanager.api.dto.team;

public class UninviteUserDto {

    private String userUuid;
    private String teamUuid;
    private String email;

    public String getUserUuid() {
        return userUuid;
    }

    public String getTeamUuid() {
        return teamUuid;
    }

    public String getEmail() {
        return email;
    }
}
