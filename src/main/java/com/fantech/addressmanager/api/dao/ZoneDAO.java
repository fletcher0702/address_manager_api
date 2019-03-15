package com.fantech.addressmanager.api.dao;

import com.fantech.addressmanager.api.entity.Zone;
import com.fantech.addressmanager.api.util.HibernateUtilConfiguration;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Repository
public class ZoneDAO extends DAO<Zone> {

    @Autowired
    public ZoneDAO(HibernateUtilConfiguration hibernateUtilConfiguration, EntityManagerFactory entityManagerFactory) {
        super(hibernateUtilConfiguration, entityManagerFactory);
    }

    @Override
    public boolean create(Zone zone) {
        return save(zone);
    }

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    @Override
    public boolean delete(Zone z) {

        entityManager.joinTransaction();
        assertNotNull(z);
        Zone zd = entityManager.find(Zone.class,z.getUuid());
        zd.getVisits().clear();
        entityManager.persist(zd);
        entityManager.flush();
        entityManager
                .createNativeQuery("delete from ZONE where uuid = :zoneUuid ")
                .setParameter("zoneUuid", z.getUuid())
                .executeUpdate();
        return true;
    }

    @Override
    public boolean update(Zone obj) {
        Transaction tx = getSession().beginTransaction();
        getSession().update(obj);
        tx.commit();
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

    @Transactional
    @Override
    public Zone findByUuid(UUID uuid) {
        return entityManager.find(Zone.class,uuid);
    }
}
