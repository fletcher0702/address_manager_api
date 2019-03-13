package com.fantech.addressmanager.api.controller;

import com.fantech.addressmanager.api.dto.visit.DeleteVisitDto;
import com.fantech.addressmanager.api.dto.visit.VisitDto;
import com.fantech.addressmanager.api.entity.Visit;
import com.fantech.addressmanager.api.services.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserVisitController {

    private final VisitService visitService;

    @Autowired
    public UserVisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @GetMapping("/{teamUuid}/visits")
    public List findAll(@PathVariable("teamUuid") String userUuid) {
        return visitService.findTeamVisits(userUuid);
    }

    @PostMapping("/teams/visits/create")
    public Visit createVisit(@RequestBody VisitDto visitDto) throws IOException {
        return visitService.createVisit(visitDto);
    }

    @PatchMapping
    public Visit updateVisit(Visit visit) {
        return null;
    }

    @DeleteMapping("/teams/zones/visits/delete")
    public Object deleteVisit(@RequestBody DeleteVisitDto visitDto) {
        return visitService.deleteVisitByUuid(visitDto);
    }
}
