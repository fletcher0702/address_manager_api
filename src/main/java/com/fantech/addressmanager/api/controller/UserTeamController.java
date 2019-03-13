package com.fantech.addressmanager.api.controller;

import com.fantech.addressmanager.api.dto.team.InviteUsersDto;
import com.fantech.addressmanager.api.dto.team.TeamDto;
import com.fantech.addressmanager.api.dto.zone.DeleteZoneDto;
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

        System.out.println(inviteUsersDto);
        return teamService.addUsersInTeam(inviteUsersDto);
    }

    @DeleteMapping("/teams/zones/delete")
    public Object deleteOne(@RequestBody DeleteZoneDto zoneDto){
        return zoneService.deleteByUuid(zoneDto);
    }

}
