package com.fantech.addressmanager.api.services;

import com.fantech.addressmanager.api.dao.TeamDAO;
import com.fantech.addressmanager.api.dao.ZoneDAO;
import com.fantech.addressmanager.api.dto.zone.CreateZoneDto;
import com.fantech.addressmanager.api.entity.Team;
import com.fantech.addressmanager.api.entity.Zone;
import com.fantech.addressmanager.api.entity.common.Coordinates;
import com.fantech.addressmanager.api.helpers.AddressHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class ZoneService {

    private ZoneDAO zoneDAO;
    private TeamDAO teamDAO;
    private AddressHelper addressHelper;

    @Autowired
    public ZoneService(ZoneDAO zoneDAO, TeamDAO teamDAO, AddressHelper addressHelper) {
        this.zoneDAO = zoneDAO;
        this.teamDAO = teamDAO;
        this.addressHelper = addressHelper;
    }

    public Zone createZone(CreateZoneDto createZoneDto) throws IOException {

        if (createZoneDto.getAddress() != null && createZoneDto.getName() != null && createZoneDto.getTeamUuid() != null) {

            System.out.println("All Credentials presents...");
            System.out.println(createZoneDto.getTeamUuid());
            Team team = teamDAO.findByUuid(UUID.fromString(createZoneDto.getTeamUuid()));

            if (team != null) {

                System.out.println("Related Team found...");
                Zone zone = new Zone();
                Coordinates coordinates = addressHelper.getCoordinates(createZoneDto.getAddress());
                zone.setName(createZoneDto.getName());
                zone.setLatitude(coordinates.getLat());
                zone.setLongitude(coordinates.getLng());
                zone.setTeam(team);
                zoneDAO.create(zone);
                return zone;
            }
            System.out.println("Related user not found...");
        }

        return null;
    }

    public List findAll(String userUuid){

        return  zoneDAO.findAll();
    }

    public List findAllByUserUuid(String teamUuid){

        Team team = teamDAO.findByUuid(UUID.fromString(teamUuid));

        if(team!=null){
            System.out.println("Team found, looking for zones...");
            return zoneDAO.findAllByUserUuid(UUID.fromString(teamUuid));
        }

        System.out.println("Zone(s) not found...");
        return null;
    }

    public Zone findUserZoneByUuid(String userUuid, String zoneUuid){

        Team team = teamDAO.findByUuid(UUID.fromString(userUuid));

        if(team!=null){

            return zoneDAO.findUserZoneByUuid(team.getUuid(), UUID.fromString(zoneUuid));
        }
        System.out.println("User not found...");
        return null;
    }
}
