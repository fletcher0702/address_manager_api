package com.fantech.addressmanager.api.services;

import com.fantech.addressmanager.api.dao.UserDAO;
import com.fantech.addressmanager.api.dao.ZoneDAO;
import com.fantech.addressmanager.api.dto.zone.ZoneDto;
import com.fantech.addressmanager.api.entity.User;
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
    private UserDAO userDAO;
    private AddressHelper addressHelper;

    @Autowired
    public ZoneService(ZoneDAO zoneDAO, UserDAO userDAO, AddressHelper addressHelper) {
        this.zoneDAO = zoneDAO;
        this.userDAO = userDAO;
        this.addressHelper = addressHelper;
    }

    public Zone createZone(ZoneDto zoneDto) throws IOException {

        if (zoneDto.getAddress() != null && zoneDto.getName() != null && zoneDto.getUserUuid() != null) {

            System.out.println("All Credentials presents...");
            System.out.println(zoneDto.getUserUuid());
            User user = userDAO.findByUuid(UUID.fromString(zoneDto.getUserUuid()));

            if (user != null) {

                System.out.println("Related User found...");
                Zone zone = new Zone();
                Coordinates coordinates = addressHelper.getCoordinates(zoneDto.getAddress());
                zone.setName(zoneDto.getName());
                zone.setLatitude(coordinates.getLat());
                zone.setLongitude(coordinates.getLng());
                zone.setUser(user);
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

    public List findAllByUserUuid(String userUuid){

        User user = userDAO.findByUuid(UUID.fromString(userUuid));

        if(user!=null){
            System.out.println("User found, looking for zones...");
            return zoneDAO.findAllByUserUuid(UUID.fromString(userUuid));
        }

        System.out.println("Zone(s) not found...");
        return null;
    }

    public Zone findUserZoneByUuid(String userUuid, String zoneUuid){

        User user = userDAO.findByUuid(UUID.fromString(userUuid));

        if(user!=null){

            return zoneDAO.findUserZoneByUuid(user.getUuid(), UUID.fromString(zoneUuid));
        }
        System.out.println("User not found...");
        return null;
    }
}
