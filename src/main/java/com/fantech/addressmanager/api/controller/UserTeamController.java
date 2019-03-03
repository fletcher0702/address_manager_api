package com.fantech.addressmanager.api.controller;

import com.fantech.addressmanager.api.dto.team.InviteUsersDto;
import com.fantech.addressmanager.api.dto.team.TeamDto;
import com.fantech.addressmanager.api.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserTeamController {

    private TeamService teamService;

    @Autowired
    public UserTeamController(TeamService teamService) {
        this.teamService = teamService;
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

}
