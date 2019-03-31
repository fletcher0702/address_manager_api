package com.fantech.addressmanager.api.entity;

import com.fantech.addressmanager.api.entity.common.Resource;

import javax.persistence.Entity;
import java.util.ArrayList;

@Entity
public class History extends Resource {

    private ArrayList<String> history = new ArrayList<>();

    public ArrayList<String> getHistory() {
        return history;
    }
}
