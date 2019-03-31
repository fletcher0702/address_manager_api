package com.fantech.addressmanager.api.entity;

import com.fantech.addressmanager.api.entity.common.Resource;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

@Entity
public class Team extends Resource {

    @Column(name="name")
    private String name;

    @JsonIgnore
    @Column(name="adminUuid",nullable = false,updatable = false)
    private UUID adminUuid;

    @OneToMany(mappedBy = "team",fetch = FetchType.EAGER)
    private Set<Zone> zones;

    @JsonIgnore
    @ManyToMany(mappedBy = "teams",fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private Set<User> users;

    @OneToMany(mappedBy = "team",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Status> status;

    @Transient
    private boolean isAdmin;

    @Transient
    private ArrayList<String> emails = new ArrayList<>();

    public String getName() {
        return name;
    }

    public UUID getAdminUuid() {
        return adminUuid;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public Set<User> getUsers() {
        return users;
    }

    public Set<Zone> getZones() {
        return zones;
    }

    public Set<Status> getStatus() {
        return status;
    }

    public ArrayList<String> getEmails() {
        return emails;
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

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
