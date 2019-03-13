package com.fantech.addressmanager.api.services;

import com.fantech.addressmanager.api.dao.TeamDAO;
import com.fantech.addressmanager.api.dao.UserDAO;
import com.fantech.addressmanager.api.dto.team.DeleteTeamDto;
import com.fantech.addressmanager.api.dto.team.InviteUsersDto;
import com.fantech.addressmanager.api.dto.team.TeamDto;
import com.fantech.addressmanager.api.entity.Team;
import com.fantech.addressmanager.api.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
                user.getTeams().add(team);
                userDAO.updateObj(user);
                return team;

            }

        }

        return HttpStatus.BAD_REQUEST;
    }

    public Object findOneByUuid(String userUuid, String teamUuid) {

        Team team = teamDAO.findUserTeamByUuid(UUID.fromString(userUuid), UUID.fromString(teamUuid));
        if (team != null) return team;
        return HttpStatus.NOT_FOUND;
    }

    public Object addUsersInTeam(InviteUsersDto inviteUsersDto) {


        // Check if we have users
        if (inviteUsersDto.getEmails().size() > 0) {
            Team team = teamDAO.findByUuid(UUID.fromString(inviteUsersDto.getTeamUuid()));

            // Check if the request is made by the owner of the team
            if (Objects.equals(team.getAdminUuid(), UUID.fromString(inviteUsersDto.getUserUuid()))) {
                List<String> created = new ArrayList<>();
                HashMap<String, Object> res = new HashMap<>();
                for (String email : inviteUsersDto.getEmails()) {
                    User user = userDAO.findByEmail(email);

                    if (user != null) {

                        user.getTeams().add(team);
                        userDAO.updateObj(user);
                        created.add(email);
                    }

                }

                res.put("email", created);

                return res;
            }
        }

        return null;
    }

    public Object findAllTeamByUserUuid(String userUuid){

        return teamDAO.findUserRelatedTeam(UUID.fromString(userUuid));
    }

    private boolean elligible() {

        return false;
    }

    public boolean deleteByUuid(DeleteTeamDto teamDto){

        assertNotNull(teamDto.getTeamUuid());
        assertNotNull(teamDto.getUserUuid());

        Team t = teamDAO.findByUuid(UUID.fromString(teamDto.getTeamUuid()));

        assertNotNull(t);

        if(Objects.equals(t.getAdminUuid(),UUID.fromString(teamDto.getUserUuid()))){

            teamDAO.delete(t);
            return true;
        }
        return false;
    }

}
