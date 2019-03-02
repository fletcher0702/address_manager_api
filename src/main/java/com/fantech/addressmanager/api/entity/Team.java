package com.fantech.addressmanager.api.entity;

import com.fantech.addressmanager.api.entity.common.Resource;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;
import java.util.UUID;

@Entity
public class Team extends Resource {

    @Column(name="name")
    private String name;

    @Column(name="adminUuid",unique = true, nullable = false,updatable = false)
    private UUID adminUuid;

    @ManyToMany(mappedBy = "teams")
    private Set<User> users;

    public String getName() {
        return name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAdminUuid(UUID adminUuid) {
        this.adminUuid = adminUuid;
    }
}
