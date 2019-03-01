package com.fantech.addressmanager.api.dto.team;

public class TeamDto {

    private String name;
    private String adminUuid;

    public TeamDto(String name, String adminUuid) {
        this.name = name;
        this.adminUuid = adminUuid;
    }

    public String getName() {
        return name;
    }

    public String getAdminUuid() {
        return adminUuid;
    }
}
