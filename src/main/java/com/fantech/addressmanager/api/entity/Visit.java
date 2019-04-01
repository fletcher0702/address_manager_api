package com.fantech.addressmanager.api.entity;

import com.fantech.addressmanager.api.entity.common.Resource;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;


@Entity
@Table(name="visit")
public class Visit extends Resource {

    @Column(name="name")
    private String name;
    @Column(name="address")
    private String address;
    @Column(name="phone_number")
    private String phoneNumber;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn
    private Status status;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn
    private History history;

    @Column(name="observation")
    private String observation;

    @Column(name="latitude")
    private double latitude;
    @Column(name="longitude")
    private double longitude;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Zone zone;

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Status getStatus() {
        return status;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public History getHistory() {
        return history;
    }

    public String getObservation() {
        return observation;
    }

    public void setHistory(History history) {
        this.history = history;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
}
