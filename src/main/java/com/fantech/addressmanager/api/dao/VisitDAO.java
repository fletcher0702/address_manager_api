package com.fantech.addressmanager.api.dao;

import com.fantech.addressmanager.api.entity.Visit;
import com.fantech.addressmanager.api.util.HibernateUtilConfiguration;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class VisitDAO extends DAO<Visit>{

    @Autowired
    public VisitDAO(HibernateUtilConfiguration hibernateUtilConfiguration) {
        super(hibernateUtilConfiguration);
    }

    @Override
    public boolean create(Visit visit) {
        return save(visit);
    }

    @Override
    public boolean delete(Visit obj) {
        return false;
    }

    @Override
    public boolean update(Visit obj) {
        return false;
    }

    @Override
    public List<Visit> findAll() {
        return null;
    }

    @Override
    public Visit findByUuid(UUID uuid) {
        return null;
    }
}
