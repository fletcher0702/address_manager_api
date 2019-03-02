package com.fantech.addressmanager.api.entity;

import com.fantech.addressmanager.api.entity.common.Resource;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="\"user\"")
public class User extends Resource {

    @JsonIgnore
    @Column(name="email")
    private String email;

    @JsonIgnore
    @Column(name="password")
    private String password;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "user_team",
        joinColumns = {@JoinColumn(name="user_uuid")},
            inverseJoinColumns = {@JoinColumn(name="team_uuid")}
    )
    private Set<Team> teams;

    public User(){

    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @JsonIgnore
    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
