package com.fantech.addressmanager.api.entity;

import com.fantech.addressmanager.api.entity.common.Resource;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "zone")
public class Zone extends Resource {

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @Column(name = "adminUuid",nullable = false, updatable = false)
    private UUID adminUuid;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @ManyToOne
    private Team team;

    @OneToMany(mappedBy = "zone",orphanRemoval = true,cascade =CascadeType.ALL,fetch = FetchType.EAGER)
    private Set<Visit> visits;

    public Zone() {
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Set<Visit> getVisits() {
        return visits;
    }

    public UUID getAdminUuid() {
        return adminUuid;
    }

    public void setAdminUuid(UUID adminUuid) {
        this.adminUuid = adminUuid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
