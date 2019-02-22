package com.fantech.addressmanager.api.controller;

import com.fantech.addressmanager.api.dto.visit.VisitDto;
import com.fantech.addressmanager.api.entity.Visit;
import com.fantech.addressmanager.api.services.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users/{userUuid}/visits")
public class UserVisitController {

    private final VisitService visitService;

    @Autowired
    public UserVisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @GetMapping
    public List findAll(@PathVariable("userUuid") String userUuid) {
        return visitService.findUserVisits(userUuid);
    }

    @PostMapping("/create")
    public Visit createVisit(@RequestBody VisitDto visitDto) throws IOException {
        return visitService.createVisit(visitDto);
    }

    @PatchMapping
    public Visit updateVisit(Visit visit) {
        return null;
    }

    @DeleteMapping("{uuid}")
    public void deleteVisit(@PathVariable("uuid") UUID uuid) {

    }
}
