package com.fantech.addressmanager.api.dto.team;

import java.util.List;

public class InviteUsersDto {

    private String userUuid;
    private String teamUuid;
    private List<String> emails;

    public String getUserUuid() {
        return userUuid;
    }

    public String getTeamUuid() {
        return teamUuid;
    }

    public List<String> getEmails() {
        return emails;
    }
}
