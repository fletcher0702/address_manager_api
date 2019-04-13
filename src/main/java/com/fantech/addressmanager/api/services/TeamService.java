package com.fantech.addressmanager.api.services;

import com.fantech.addressmanager.api.dao.TeamDAO;
import com.fantech.addressmanager.api.dao.UserDAO;
import com.fantech.addressmanager.api.dto.status.CreateStatusDto;
import com.fantech.addressmanager.api.dto.status.DeleteStatusDto;
import com.fantech.addressmanager.api.dto.status.UpdateStatusDto;
import com.fantech.addressmanager.api.dto.team.*;
import com.fantech.addressmanager.api.entity.Status;
import com.fantech.addressmanager.api.entity.Team;
import com.fantech.addressmanager.api.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Service
public class TeamService {

    private UserDAO userDAO;
    private TeamDAO teamDAO;
    private HashMap<String, Object> response = new HashMap<>();

    @Autowired
    public TeamService(UserDAO userDAO, TeamDAO teamDAO) {
        this.userDAO = userDAO;
        this.teamDAO = teamDAO;
    }

    public Object createTeam(TeamDto teamDto) {

        response.clear();
        try{

            if (!teamDto.getAdminUuid().isEmpty() && !teamDto.getName().isEmpty()) {

                User user = userDAO.findByUuid(UUID.fromString(teamDto.getAdminUuid()));
                if (user != null) {

                    // TODO : findOneByName before saving

                    response.put("created", true);
                    response.put("content", teamDAO.createOne(user.getUuid(), teamDto));
                    return response;

                }

            }

        }catch (Exception e){
            e.printStackTrace();
            response.put("created", false);
            response.put("message", "Something went wrong");

            return response;

        }

        return HttpStatus.BAD_REQUEST;
    }

    public Object findOneByUuid(String userUuid, String teamUuid) {

        Team team = teamDAO.findUserTeamByUuid(UUID.fromString(userUuid), UUID.fromString(teamUuid));
        if (team != null) return team;
        return HttpStatus.NOT_FOUND;
    }

    public Object addUsersInTeam(InviteUsersDto inviteUsersDto) {


        response.clear();
        // Check if we have users
        try{
            if (inviteUsersDto.getEmails().size() > 0) {
                Team team = teamDAO.findByUuid(UUID.fromString(inviteUsersDto.getTeamUuid()));

                // Check if the request is made by the owner of the team
                if (Objects.equals(team.getAdminUuid(), UUID.fromString(inviteUsersDto.getUserUuid()))) {
                    List<String> created = new ArrayList<>();
                    HashMap<String, Object> res = new HashMap<>();
                    ArrayList<UUID> usersUuid = new ArrayList<>();
                    for (String email : inviteUsersDto.getEmails()) {
                        User user = userDAO.findByEmail(email);

                        if (user != null) {

                            usersUuid.add(user.getUuid());
                            created.add(email);
                        }

                    }
                    teamDAO.addUserInTeam(team.getUuid(), usersUuid);

                    res.put("email", created);

                    return res;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            response.put("error", "Something went wrong");
            return response;
        }
        response.put("error", "Enter some email(s)");
        return response;
    }

    public Object removeUserInTeam(UninviteUserDto uninviteUserDto) {

        response.clear();

        try {
            assertNotNull(uninviteUserDto);
            assertNotNull(uninviteUserDto.getEmail());
            assertNotNull(uninviteUserDto.getTeamUuid());
            assertNotNull(uninviteUserDto.getUserUuid());


            Team t = teamDAO.findByUuid(UUID.fromString(uninviteUserDto.getTeamUuid()));

            assertNotNull(t);

            UUID userUuid = UUID.fromString(uninviteUserDto.getUserUuid());

            if (Objects.equals(t.getAdminUuid(), userUuid)) {

                User toRemove = userDAO.findByEmail(uninviteUserDto.getEmail());
                assertNotNull(toRemove);

                System.out.println("User to delete found...");

                response.put("present", teamDAO.removeUserInTeam(t.getUuid(), toRemove.getUuid()));


                return response;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("present", false);
            return response;
        }

        response.put("present", false);

        return response;
    }

    public Object findAllTeamByUserUuid(String userUuid) {

        try {
            return teamDAO.findUserRelatedTeam(UUID.fromString(userUuid));
        } catch (Exception e) {
            e.printStackTrace();
            response.clear();
            response.put("error", "Something went wrong");
            return response;
        }
    }

    public Object deleteByUuid(DeleteTeamDto teamDto) {


        try {
            assertNotNull(teamDto.getTeamUuid());
            assertNotNull(teamDto.getUserUuid());

            Team t = teamDAO.findByUuid(UUID.fromString(teamDto.getTeamUuid()));
            response.clear();
            assertNotNull(t);

            if (Objects.equals(t.getAdminUuid(), UUID.fromString(teamDto.getUserUuid()))) {

                teamDAO.delete(t);
                response.put("deleted", true);

            } else {
                response.put("deleted", false);
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("deleted", false);
            return response;
        }

    }

    public Object createStatus(CreateStatusDto statusDto) {

        try {

            assertNotNull(statusDto.getUserUuid());
            assertNotNull(statusDto.getTeamUuid());
            assertNotNull(statusDto.getStatus());
            response.clear();
            Team t = teamDAO.findByUuid(UUID.fromString(statusDto.getTeamUuid()));
            assertNotNull(t);


            if (Objects.equals(t.getAdminUuid(), UUID.fromString(statusDto.getUserUuid()))) {

                response.put("created", teamDAO.addStatus(t.getUuid(), statusDto.getStatus()));

            } else response.put("created", false);
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            response.put("created", false);
            return response;
        }

    }


    public Object deleteStatus(DeleteStatusDto statusDto) {

        try {
            assertNotNull(statusDto.getUserUuid());
            assertNotNull(statusDto.getTeamUuid());
            assertNotNull(statusDto.getStatusUuid());

            UUID teamUuid = UUID.fromString(statusDto.getTeamUuid());
            Team team = teamDAO.findByUuid(teamUuid);
            response.clear();

            assertNotNull(team);

            if (Objects.equals(team.getAdminUuid(), UUID.fromString(statusDto.getUserUuid()))) {

                response.put("deleted", teamDAO.deleteStatus(teamUuid, UUID.fromString(statusDto.getStatusUuid())));

            } else response.put("deleted", false);

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("deleted", false);
            return response;
        }

    }


    public Object updateStatus(UpdateStatusDto statusDto) {


        try {
            assertNotNull(statusDto);
            assertNotNull(statusDto.getUserUuid());
            assertNotNull(statusDto.getTeamUuid());
            assertNotNull(statusDto.getStatusUuid());
            assertNotNull(statusDto.getStatus());

            response.clear();
            UUID teamUuid = UUID.fromString(statusDto.getTeamUuid());

            Team team = teamDAO.findByUuid(teamUuid);

            assertNotNull(team);

            if (Objects.equals(team.getAdminUuid(), UUID.fromString(statusDto.getUserUuid()))) {

                Status status = teamDAO.findStatusByUuid(teamUuid, UUID.fromString(statusDto.getStatusUuid()));

                assertNotNull(status);
                response.put("updated", teamDAO.updateStatus(status.getUuid(), statusDto.getStatus()));

            } else response.put("updated", false);

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("updated", false);
            return response;
        }


    }

    public Object updateTeam(UpdateTeamDto teamDto) {


        try {

            assertNotNull(teamDto);
            assertNotNull(teamDto.getName());
            assertNotNull(teamDto.getTeamUuid());
            assertNotNull(teamDto.getUserUuid());

            response.clear();

            Team team = teamDAO.findByUuid(UUID.fromString(teamDto.getTeamUuid()));

            assertNotNull(team);

            UUID userUuid = UUID.fromString(teamDto.getUserUuid());
            if (Objects.equals(team.getAdminUuid(), userUuid)) {
                response.put("updated", teamDAO.updateTeam(team.getUuid(), teamDto));

            } else response.put("updated", false);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("updated", false);
            return response;
        }
    }
}
