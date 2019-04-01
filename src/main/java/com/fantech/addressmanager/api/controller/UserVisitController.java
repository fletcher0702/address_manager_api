package com.fantech.addressmanager.api.controller;

import com.fantech.addressmanager.api.dto.visit.DeleteVisitDto;
import com.fantech.addressmanager.api.dto.visit.UpdateVisitDto;
import com.fantech.addressmanager.api.dto.visit.UpdateVisitHistoryDto;
import com.fantech.addressmanager.api.dto.visit.VisitDto;
import com.fantech.addressmanager.api.entity.Visit;
import com.fantech.addressmanager.api.services.VisitService;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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

    @PostMapping("/teams/zones/visits/create")
    public Object createVisit(@RequestBody VisitDto visitDto) throws IOException {
        return visitService.createVisit(visitDto);
    }

    @PatchMapping("/teams/zones/visits/update")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Object updateVisit(@RequestBody UpdateVisitDto visitDto) {
        return visitService.updateVisitByUuid(visitDto);
    }

    @PatchMapping("/teams/zones/visits/update/history")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Object updateVisitHistory(@RequestBody UpdateVisitHistoryDto visitDto) {
        return visitService.updateVisitHistory(visitDto);
    }

    @DeleteMapping("/teams/zones/visits/delete")
    public Object deleteVisit(@RequestBody DeleteVisitDto visitDto) {
        return visitService.deleteVisitByUuid(visitDto);
    }
}
