package com.fantech.addressmanager.api.services;

import com.fantech.addressmanager.api.dao.UserDAO;
import com.fantech.addressmanager.api.dao.VisitDAO;
import com.fantech.addressmanager.api.dao.ZoneDAO;
import com.fantech.addressmanager.api.dto.visit.VisitDto;
import com.fantech.addressmanager.api.entity.User;
import com.fantech.addressmanager.api.entity.Visit;
import com.fantech.addressmanager.api.entity.Zone;
import com.fantech.addressmanager.api.entity.common.Coordinates;
import com.fantech.addressmanager.api.helpers.AddressHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("Duplicates")
@Service
public class VisitService {

    private UserDAO userDAO;
    private VisitDAO visitDAO;
    private ZoneDAO zoneDAO;
    private AddressHelper addressHelper;

    @Autowired
    public VisitService(UserDAO userDAO, VisitDAO visitDAO, ZoneDAO zoneDAO, AddressHelper addressHelper) {
        this.userDAO = userDAO;
        this.visitDAO = visitDAO;
        this.zoneDAO = zoneDAO;
        this.addressHelper = addressHelper;
    }

    public Visit createVisit(VisitDto visitDto) throws IOException {


        if (visitDto.getName() != null && visitDto.getAddress() != null && visitDto.getStatus() != null && visitDto.getZoneUuid() != null && visitDto.getZoneUuid() != null) {

            User user = userDAO.findByUuid(UUID.fromString(visitDto.getUserUuid()));

            if (user != null) {

                Zone zone = zoneDAO.findByUuid(UUID.fromString(visitDto.getZoneUuid()));

                if (zone != null) {

                    Visit visit = new Visit();
                    Coordinates coordinates = addressHelper.getCoordinates(visitDto.getAddress());
                    visit.setName(visitDto.getName());
                    visit.setAddress(visitDto.getAddress());
                    visit.setStatus(visitDto.getStatus());
                    visit.setPhoneNumber(visitDto.getPhoneNumber());
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

    public List findUserVisits(String userUuid){

        UUID uuid = UUID.fromString(userUuid);
        User user = userDAO.findByUuid(uuid);

        if(user!=null){
            return visitDAO.findUserVisits(uuid);
        }
        return null;
    }

//    public Visit findVisitByVis
}
