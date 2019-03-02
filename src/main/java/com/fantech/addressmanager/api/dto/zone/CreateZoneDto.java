package com.fantech.addressmanager.api.dto.zone;

public class CreateZoneDto {

    private String teamUuid;
    private String name;
    private String address;

    public CreateZoneDto(String teamUuid, String name, String address) {
        this.teamUuid = teamUuid;
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getTeamUuid() {
        return teamUuid;
    }
}
