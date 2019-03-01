package com.fantech.addressmanager.api.controller;

import com.fantech.addressmanager.api.dto.team.TeamDto;
import com.fantech.addressmanager.api.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
