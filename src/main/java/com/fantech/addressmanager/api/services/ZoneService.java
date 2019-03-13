package com.fantech.addressmanager.api.services;

import com.fantech.addressmanager.api.dao.TeamDAO;
import com.fantech.addressmanager.api.dao.UserDAO;
import com.fantech.addressmanager.api.dao.ZoneDAO;
import com.fantech.addressmanager.api.dto.zone.CreateZoneDto;
import com.fantech.addressmanager.api.dto.zone.DeleteZoneDto;
import com.fantech.addressmanager.api.entity.Team;
import com.fantech.addressmanager.api.entity.User;
import com.fantech.addressmanager.api.entity.Zone;
import com.fantech.addressmanager.api.entity.common.Coordinates;
import com.fantech.addressmanager.api.helpers.AddressHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Service
public class ZoneService {

    private UserDAO userDAO;
    private ZoneDAO zoneDAO;
    private TeamDAO teamDAO;
    private AddressHelper addressHelper;

    @Autowired
    public ZoneService(UserDAO userDAO,ZoneDAO zoneDAO, TeamDAO teamDAO, AddressHelper addressHelper) {
        this.userDAO = userDAO;
        this.zoneDAO = zoneDAO;
        this.teamDAO = teamDAO;
        this.addressHelper = addressHelper;
    }

    public Zone createZone(CreateZoneDto createZoneDto) throws IOException {

        if (createZoneDto.getAddress() != null && createZoneDto.getName() != null && createZoneDto.getTeamUuid() != null && createZoneDto.getUserUuid()!=null) {

            System.out.println("All Credentials presents...");
            System.out.println(createZoneDto.getTeamUuid());
            Team team = teamDAO.findByUuid(UUID.fromString(createZoneDto.getTeamUuid()));
            User user = userDAO.findByUuid(UUID.fromString(createZoneDto.getUserUuid()));
            if (team != null && user !=null) {
                System.out.println("Related Team found...");
                Zone zone = new Zone();
                Coordinates coordinates = addressHelper.getCoordinates(createZoneDto.getAddress());
                zone.setAdminUuid(UUID.fromString(createZoneDto.getUserUuid()));
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

    public Object deleteByUuid(DeleteZoneDto zoneDto){
        assertNotNull(zoneDto.getUserUuid());
        assertNotNull(zoneDto.getZoneUuid());
        assertNotNull(zoneDto.getTeamUuid());

        Team team  = teamDAO.findByUuid(UUID.fromString(zoneDto.getTeamUuid()));
        assertNotNull(team);

        Zone z = zoneDAO.findByUuid(UUID.fromString(zoneDto.getZoneUuid()));

        assertNotNull(z);

        for(Zone zone : team.getZones()){

            if(Objects.equals(zone.getUuid(),z.getUuid())){

                if(Objects.equals(z.getAdminUuid(),UUID.fromString(zoneDto.getUserUuid()))){

                    return zoneDAO.delete(z);
                }

            }
        }

        return false;
    }
}
