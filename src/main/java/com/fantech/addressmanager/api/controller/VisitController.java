package com.fantech.addressmanager.api.controller;

import com.fantech.addressmanager.api.dto.visit.VisitDto;
import com.fantech.addressmanager.api.entity.Visit;
import com.fantech.addressmanager.api.services.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/visits")
public class VisitController {

    private final VisitService visitService;

    @Autowired
    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @PostMapping("/create")
    public Visit createVisit(@RequestBody VisitDto visitDto) throws IOException {
        return visitService.createVisit(visitDto);
    }

    @GetMapping("/{uuid}")
    public Visit getVisit(@PathVariable("uuid") UUID uuid){
        return null;
    }

    @PatchMapping
    public Visit updateVisit(Visit visit){

        return null;
    }

    @DeleteMapping("{uuid}")
    public void deleteVisit(@PathVariable("uuid") UUID uuid){

    }
}
