package com.fantech.addressmanager.api.entity.common;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@MappedSuperclass
//@JsonIdentityInfo(property = "uuid", generator = ObjectIdGenerators.UUIDGenerator.class)
public class Resource {

    @Id
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @GeneratedValue(generator = "uuid2")
    @Column(name = "uuid", unique = true, updatable = false)
    @JsonInclude
    protected UUID uuid;

    @Column(name="created_at")
    @CreationTimestamp
    protected Date createdAt;

    @Column(name="updated_at")
    @UpdateTimestamp
    protected Date updatedAt;

    public UUID getUuid() {
        return uuid;
    }
}
