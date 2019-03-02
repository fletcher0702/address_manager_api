package com.fantech.addressmanager.api.entity;

import com.fantech.addressmanager.api.entity.common.Resource;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
public class Team extends Resource {

    @Column(name="name")
    private String name;

    @JsonIgnore
    @Column(name="adminUuid",unique = true, nullable = false,updatable = false)
    private UUID adminUuid;

    @OneToMany(mappedBy = "team")
    private Set<Zone> zones;

    @ManyToMany(mappedBy = "teams",fetch = FetchType.EAGER)
    private Set<User> users;

    public String getName() {
        return name;
    }

    public UUID getAdminUuid() {
        return adminUuid;
    }

    public Set<User> getUsers() {
        return users;
    }

    public Set<Zone> getZones() {
        return zones;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAdminUuid(UUID adminUuid) {
        this.adminUuid = adminUuid;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
