package com.fantech.addressmanager.api.controller;

import com.fantech.addressmanager.api.dto.status.CreateStatusDto;
import com.fantech.addressmanager.api.dto.status.DeleteStatusDto;
import com.fantech.addressmanager.api.dto.status.UpdateStatusDto;
import com.fantech.addressmanager.api.dto.team.*;
import com.fantech.addressmanager.api.dto.zone.DeleteZoneDto;
import com.fantech.addressmanager.api.dto.zone.UpdateZoneDto;
import com.fantech.addressmanager.api.services.TeamService;
import com.fantech.addressmanager.api.services.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserTeamController {

    private TeamService teamService;
    private ZoneService zoneService;

    @Autowired
    public UserTeamController(TeamService teamService,ZoneService zoneService)
    {
        this.teamService = teamService;
        this.zoneService = zoneService;
    }

    @PostMapping("/teams/create")
    public Object createOne(@RequestBody TeamDto teamDto){
        return teamService.createTeam(teamDto);
    }

    @PostMapping("/teams/status/create")
    public Object createTeamStatus(@RequestBody CreateStatusDto statusDto){
        return teamService.createStatus(statusDto);
    }


    @GetMapping("/{userUuid}/teams/{teamUuid}")
    public Object findUserTeam(@PathVariable("userUuid") String userUuid, @PathVariable("teamUuid") String teamUuid){
        return teamService.findOneByUuid(userUuid, teamUuid);
    }

    @GetMapping("/{userUuid}/teams")
    public Object findAllUserTeam(@PathVariable("userUuid") String userUuid){
        return teamService.findAllTeamByUserUuid(userUuid);
    }

    @PostMapping("/teams/invite")
    public Object addUsersInTeam(@RequestBody InviteUsersDto inviteUsersDto){
        return teamService.addUsersInTeam(inviteUsersDto);
    }

    @PostMapping("/teams/members/remove")
    public Object removeUserInTeam(@RequestBody UninviteUserDto uninviteUserDto){
        return teamService.removeUserInTeam(uninviteUserDto);
    }

    @PatchMapping("/teams/update")
    public Object updateTeam(@RequestBody UpdateTeamDto teamDto){
        return teamService.updateTeam(teamDto);
    }

    @PatchMapping("/teams/status/update")
    public Object updateStatus(@RequestBody UpdateStatusDto statusDto){
        return teamService.updateStatus(statusDto);
    }

    @PatchMapping("/teams/zones/update")
    public Object updateZone(@RequestBody UpdateZoneDto zoneDto){
        return zoneService.updateZone(zoneDto);
    }

    @DeleteMapping("/teams/delete")
    public Object deleteTeamByUuid(@RequestBody DeleteTeamDto teamDto){
        return teamService.deleteByUuid(teamDto);
    }

    @DeleteMapping("/teams/zones/delete")
    public Object deleteZoneByUuid(@RequestBody DeleteZoneDto zoneDto){
        return zoneService.deleteByUuid(zoneDto);
    }

    @DeleteMapping("/teams/status/delete")
    public Object deleteStatusByUuid(@RequestBody DeleteStatusDto statusDto){
        return teamService.deleteStatus(statusDto);
    }
}
