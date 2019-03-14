package com.fantech.addressmanager.api.services;

import com.fantech.addressmanager.api.dao.TeamDAO;
import com.fantech.addressmanager.api.dao.VisitDAO;
import com.fantech.addressmanager.api.dao.ZoneDAO;
import com.fantech.addressmanager.api.dto.visit.DeleteVisitDto;
import com.fantech.addressmanager.api.dto.visit.VisitDto;
import com.fantech.addressmanager.api.entity.Status;
import com.fantech.addressmanager.api.entity.Team;
import com.fantech.addressmanager.api.entity.Visit;
import com.fantech.addressmanager.api.entity.Zone;
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

    private TeamDAO teamDAO;
    private VisitDAO visitDAO;
    private ZoneDAO zoneDAO;
    private AddressHelper addressHelper;

    @Autowired
    public VisitService(TeamDAO teamDAO, VisitDAO visitDAO, ZoneDAO zoneDAO, AddressHelper addressHelper) {
        this.teamDAO = teamDAO;
        this.visitDAO = visitDAO;
        this.zoneDAO = zoneDAO;
        this.addressHelper = addressHelper;
    }

    public Visit createVisit(VisitDto visitDto) throws IOException {


        if (visitDto.getStatusUuid() != null && visitDto.getAddress() != null && visitDto.getZoneUuid() != null && visitDto.getZoneUuid() != null) {

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
                    visit.setName(visitDto.getStatusUuid());
                    visit.setAddress(visitDto.getAddress());
                    visit.setStatus(status);
                    visit.setPhoneNumber(visitDto.getPhoneNumber());
                    zone.setTeam(team);
                    visit.setZone(zone);
                    visit.setLatitude(coordinates.getLat());
                    visit.setLongitude(coordinates.getLng());
                    visitDAO.create(visit);

                    return visit;
                }

            }

        }
        return null;
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
    
}
