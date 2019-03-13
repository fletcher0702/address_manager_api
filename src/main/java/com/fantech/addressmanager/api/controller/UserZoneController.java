package com.fantech.addressmanager.api.controller;

import com.fantech.addressmanager.api.dto.zone.DeleteZoneDto;
import com.fantech.addressmanager.api.entity.Zone;
import com.fantech.addressmanager.api.services.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users/{userUuid}/zones")
public class UserZoneController {


    private ZoneService zoneService;

    @Autowired
    public UserZoneController(ZoneService zoneService) {
        this.zoneService = zoneService;
    }

    @GetMapping(value = "")
    public List getAll(@PathVariable("userUuid") String userUuid) {

        return zoneService.findAllByUserUuid(userUuid);
    }

    @GetMapping("/{zoneUuid}")
    public Zone findOne(@PathVariable("userUuid") String userUuid, @PathVariable("zoneUuid") String zoneUuid) {
        return zoneService.findUserZoneByUuid(userUuid, zoneUuid);
    }
}
