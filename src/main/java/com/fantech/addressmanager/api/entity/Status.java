package com.fantech.addressmanager.api.entity;

import com.fantech.addressmanager.api.entity.common.Resource;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Status extends Resource {

    @Column(name="name")
    private String name;

    @Column(name="color")
    private Long color;

    @ManyToOne
    private Team team;

    public String getName() {
        return name;
    }

    public Long getColor() {
        return color;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(Long color) {
        this.color = color;
    }
}
