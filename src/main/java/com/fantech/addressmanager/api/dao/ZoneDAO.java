package com.fantech.addressmanager.api.dao;

import com.fantech.addressmanager.api.entity.Zone;
import com.fantech.addressmanager.api.util.HibernateUtilConfiguration;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class ZoneDAO extends DAO<Zone> {

    @Autowired
    public ZoneDAO(HibernateUtilConfiguration hibernateUtilConfiguration) {
        super(hibernateUtilConfiguration);
    }

    @Override
    public boolean create(Zone zone) {
        return save(zone);
    }

    @Override
    public boolean delete(Zone obj) {
        return false;
    }

    @Override
    public boolean update(Zone obj) {
        return false;
    }

    @Override
    public List<Zone> findAll() {
        return null;
    }

    public List findAllByUserUuid(UUID userUuid){

        Session session = this.sessionFactory.openSession();
        String hql = "from Zone z where z.user.uuid = :userUuid";
        Query q = session.createQuery(hql);
        q.setParameter("userUuid", userUuid);

        return q.list();
    }

    public Zone findUserZoneByUuid(UUID userUuid, UUID zoneUuid){

        Session session = this.sessionFactory.openSession();
        String hql = "from Zone z where z.user.uuid = :userUuid and z.uuid = :zoneUuid";
        Query q = session.createQuery(hql);
        q.setParameter("userUuid", userUuid);
        q.setParameter("zoneUuid", zoneUuid);

        return (Zone) q.uniqueResult();
    }

    @Override
    public Zone findByUuid(UUID uuid) {

        Session session = this.sessionFactory.openSession();
        String hql = "from Zone z where z.uuid = :uuid";
        Query q = session.createQuery(hql);
        q.setParameter("uuid", uuid);

        return (Zone)q.uniqueResult();
    }
}
