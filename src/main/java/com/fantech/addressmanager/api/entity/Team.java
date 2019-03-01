package com.fantech.addressmanager.api.entity;

import com.fantech.addressmanager.api.entity.common.Resource;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Team extends Resource {

    @Column(name="name")
    private String name;


}
