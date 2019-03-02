package com.fantech.addressmanager.api.controller;

import com.fantech.addressmanager.api.dto.zone.CreateZoneDto;
import com.fantech.addressmanager.api.entity.Zone;
import com.fantech.addressmanager.api.services.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/zones")
public class ZoneController {

    private ZoneService zoneService;

    @Autowired
    public ZoneController(ZoneService zoneService) {
        this.zoneService = zoneService;
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Zone createZone(@RequestBody CreateZoneDto zone) throws IOException {
        return zoneService.createZone(zone);
    }
}
