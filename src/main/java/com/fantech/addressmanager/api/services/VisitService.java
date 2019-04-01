package com.fantech.addressmanager.api.services;

import com.fantech.addressmanager.api.dao.TeamDAO;
import com.fantech.addressmanager.api.dao.UserDAO;
import com.fantech.addressmanager.api.dao.VisitDAO;
import com.fantech.addressmanager.api.dao.ZoneDAO;
import com.fantech.addressmanager.api.dto.visit.DeleteVisitDto;
import com.fantech.addressmanager.api.dto.visit.UpdateVisitDto;
import com.fantech.addressmanager.api.dto.visit.UpdateVisitHistoryDto;
import com.fantech.addressmanager.api.dto.visit.VisitDto;
import com.fantech.addressmanager.api.entity.*;
import com.fantech.addressmanager.api.entity.common.Coordinates;
import com.fantech.addressmanager.api.helpers.AddressHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings("Duplicates")
@Service
public class VisitService {

    private UserDAO userDAO;
    private TeamDAO teamDAO;
    private VisitDAO visitDAO;
    private ZoneDAO zoneDAO;
    private AddressHelper addressHelper;
    private HashMap<String,Object> response = new HashMap<>();

    @Autowired
    public VisitService(UserDAO userDAO,TeamDAO teamDAO, VisitDAO visitDAO, ZoneDAO zoneDAO, AddressHelper addressHelper) {
        this.userDAO = userDAO;
        this.teamDAO = teamDAO;
        this.visitDAO = visitDAO;
        this.zoneDAO = zoneDAO;
        this.addressHelper = addressHelper;
    }

    public Object createVisit(VisitDto visitDto) throws IOException {

        response.clear();

        if (visitDto.getStatusUuid() != null && visitDto.getAddress() != null && visitDto.getZoneUuid() != null && visitDto.getZoneUuid() != null && visitDto.getDate()!=null) {

            System.out.println("Credentials controls passed...");
            Team team = teamDAO.findByUuid(UUID.fromString(visitDto.getTeamUuid()));

            if (team != null) {

                System.out.println("Team Found...");
                Zone zone = zoneDAO.findByUuid(UUID.fromString(visitDto.getZoneUuid()));

                if (zone != null) {

                    System.out.println("Zone Found...");
                    Status status = teamDAO.findStatusByUuid(UUID.fromString(visitDto.getStatusUuid()));
                    assertNotNull(status);
                    System.out.println("Status found...");
                    Visit visit = new Visit();
                    Coordinates coordinates = addressHelper.getCoordinates(visitDto.getAddress());
                    History history = new History();
                    history.getDates().add(visitDto.getDate());
                    visit.setName(visitDto.getName());
                    visit.setAddress(visitDto.getAddress());
                    visit.setStatus(status);
                    visit.setPhoneNumber(visitDto.getPhoneNumber());
                    zone.setTeam(team);
                    visit.setZone(zone);
                    visit.setLatitude(coordinates.getLat());
                    visit.setLongitude(coordinates.getLng());
                    visit.setHistory(history);
                    if(visitDto.getObservation()!=null) visit.setObservation(visitDto.getObservation());
                    visitDAO.create(visit);
                    response.put("created", true);
                    response.put("content",visit);
                    return response;
                }

            }

        }
        response.put("created",false);
        return response;
    }

    public List findTeamVisits(String teamUuid) {

        UUID uuid = UUID.fromString(teamUuid);
        Team team = teamDAO.findByUuid(uuid);

        if (team != null) {
            return visitDAO.findUserVisits(uuid);
        }
        return null;
    }

    public Object deleteVisitByUuid(DeleteVisitDto visitDto){

        HashMap<String,Boolean> res = new HashMap<>();

        if(!visitDto.getUserUuid().isEmpty()&&!visitDto.getZoneUuid().isEmpty()&&!visitDto.getVisitUuid().isEmpty()){

            System.out.println("mapped data presents...");
            Zone zone = zoneDAO.findByUuid(UUID.fromString(visitDto.getZoneUuid()));

            if(zone!=null){

                System.out.println("Zone found...");

                if(Objects.equals(zone.getAdminUuid(),UUID.fromString(visitDto.getUserUuid()))){

                    System.out.println("Right owner...");

                    for(Visit v : zone.getVisits()){

                        if(Objects.equals(v.getUuid(),UUID.fromString(visitDto.getVisitUuid()))){
                            System.out.println("Find candidate for deletion...");
                            res.put("deleted",visitDAO.deleteByUuid(UUID.fromString(visitDto.getVisitUuid())));
                            break;
                        }
                    }

                    return res;

                }
            }

        }
        res.put("deleted",false);
        return res;
    }

    public Object updateVisitByUuid(UpdateVisitDto visitDto){

        HashMap<String,Object> res = new HashMap<>();
        try{

            assertNotNull(visitDto);
            assertNotNull(visitDto.getUserUuid());
            assertNotNull(visitDto.getTeamUuid());
            assertNotNull(visitDto.getVisitUuid());
            assertNotNull(visitDto.getZoneUuid());

            Zone zone = zoneDAO.findZoneByTeamUuid(UUID.fromString(visitDto.getTeamUuid()),UUID.fromString(visitDto.getZoneUuid()));

            assertNotNull(zone);

            UUID visitUuid = UUID.fromString(visitDto.getVisitUuid());
            if(Objects.equals(zone.getAdminUuid(),UUID.fromString(visitDto.getUserUuid()))){

                System.out.println("In the if condition of administrator !");
                Coordinates coordinates = null;

                if(visitDto.getAddress()!=null){
                    if(!visitDto.getAddress().isEmpty()) coordinates = addressHelper.getCoordinates(visitDto.getAddress());
                }
                System.out.println("Good owner requester !");
                res.put("updated",visitDAO.updateVisitByUuidAdmin(visitUuid,visitDto,coordinates));

            }else {
                System.out.println("Not an admin...");
                User user = userDAO.findByUuid(UUID.fromString(visitDto.getUserUuid()));
                assertNotNull(user);
                Team team = teamDAO.findByUuid(UUID.fromString(visitDto.getTeamUuid()));
                assertNotNull(team);
                assertNotNull(visitDto.getStatusUuid());
                if(teamDAO.userBelongsToTeam(team.getUuid(),user.getUuid())) {
                    visitDAO.updateVisitHistory(visitUuid,visitDto.getDate());
                    res.put("updated",visitDAO.updateVisitStatus(visitUuid,UUID.fromString(visitDto.getStatusUuid())));
                }else{

                    res.put("update",false);
                    res.put("message","User not found");
                }

            }

            return res;

        }catch(Exception e){

            res.put("updated",false);
            res.put("message", "Bad credentials send or invalid user");
            return res;
        }
    }

    public Object updateVisitHistory(UpdateVisitHistoryDto visitDto){

        HashMap<String,Object> res = new HashMap<>();
        try{

            assertNotNull(visitDto);
            assertNotNull(visitDto.getUserUuid());
            assertNotNull(visitDto.getTeamUuid());
            assertNotNull(visitDto.getVisitUuid());
            assertNotNull(visitDto.getZoneUuid());

            Zone zone = zoneDAO.findZoneByTeamUuid(UUID.fromString(visitDto.getTeamUuid()),UUID.fromString(visitDto.getZoneUuid()));

            assertNotNull(zone);

            UUID visitUuid = UUID.fromString(visitDto.getVisitUuid());
            User user = userDAO.findByUuid(UUID.fromString(visitDto.getUserUuid()));
            assertNotNull(user);
            Team team = teamDAO.findByUuid(UUID.fromString(visitDto.getTeamUuid()));
            assertNotNull(team);
            if(teamDAO.userBelongsToTeam(team.getUuid(),user.getUuid())) {
                res.put("updated",visitDAO.updateVisitHistory(visitUuid,visitDto.getDate()));
            }else{
                res.put("update",false);
                res.put("message","User not found");
            }



            return res;

        }catch(Exception e){

            res.put("updated",false);
            res.put("message", "Bad credentials send or invalid user");
            return res;
        }
    }
    
}
