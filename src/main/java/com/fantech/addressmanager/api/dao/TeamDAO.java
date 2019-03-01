package com.fantech.addressmanager.api.dao;

import com.fantech.addressmanager.api.util.HibernateUtilConfiguration;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class TeamDAO extends DAO {

    public TeamDAO(HibernateUtilConfiguration hibernateUtilConfiguration) {
        super(hibernateUtilConfiguration);
    }

    @Override
    public boolean create(Object obj) {
        return save(obj);
    }

    @Override
    public boolean delete(Object obj) {
        return false;
    }

    @Override
    public boolean update(Object obj) {
        return false;
    }

    @Override
    public List findAll() {
        return null;
    }

    @Override
    public Object findByUuid(UUID uuid) {
        return null;
    }
}
