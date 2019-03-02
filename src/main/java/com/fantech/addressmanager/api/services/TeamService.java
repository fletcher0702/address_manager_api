package com.fantech.addressmanager.api.services;

import com.fantech.addressmanager.api.dao.TeamDAO;
import com.fantech.addressmanager.api.dao.UserDAO;
import com.fantech.addressmanager.api.dto.team.TeamDto;
import com.fantech.addressmanager.api.entity.Team;
import com.fantech.addressmanager.api.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TeamService {

    private UserDAO userDAO;
    private TeamDAO teamDAO;

    @Autowired
    public TeamService(UserDAO userDAO, TeamDAO teamDAO) {
        this.userDAO = userDAO;
        this.teamDAO = teamDAO;
    }

    public Object createTeam(TeamDto teamDto) {

        if (!teamDto.getAdminUuid().isEmpty() && !teamDto.getName().isEmpty()) {

            User user = userDAO.findByUuid(UUID.fromString(teamDto.getAdminUuid()));
            if (user != null) {

                // TODO : findOneByName before saving
                Team team = new Team();
                team.setName(teamDto.getName());
                team.setAdminUuid(UUID.fromString(teamDto.getAdminUuid()));
                teamDAO.create(team);
                return HttpStatus.CREATED;

            }

        }

        return HttpStatus.BAD_REQUEST;
    }

    public Object findOneByUuid(String userUuid,String teamUuid){

        Team team = teamDAO.findUserTeamByUuid(UUID.fromString(userUuid),UUID.fromString(teamUuid));
        if(team!=null) return team;
        return HttpStatus.NOT_FOUND;
    }

}
