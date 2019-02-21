package com.fantech.addressmanager.api.services;

import com.fantech.addressmanager.api.dao.VisitDAO;
import com.fantech.addressmanager.api.dao.ZoneDAO;
import com.fantech.addressmanager.api.dto.visit.VisitDto;
import com.fantech.addressmanager.api.entity.Visit;
import com.fantech.addressmanager.api.entity.Zone;
import com.fantech.addressmanager.api.entity.common.Coordinates;
import com.fantech.addressmanager.api.helpers.AddressHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class VisitService {

    private VisitDAO visitDAO;
    private ZoneDAO zoneDAO;
    private AddressHelper addressHelper;

    @Autowired
    public VisitService(VisitDAO visitDAO, ZoneDAO zoneDAO,AddressHelper addressHelper) {
        this.visitDAO = visitDAO;
        this.zoneDAO = zoneDAO;
        this.addressHelper = addressHelper;
    }

    public Visit createVisit(VisitDto visitDto) throws IOException {


        if (visitDto.getName() != null && visitDto.getAddress() != null && visitDto.getStatus() != null && visitDto.getZoneUuid() != null) {

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
        return null;
    }
}
