package com.fantech.addressmanager.api.dao;

import com.fantech.addressmanager.api.entity.Visit;
import com.fantech.addressmanager.api.util.HibernateUtilConfiguration;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Repository
public class VisitDAO extends DAO<Visit>{

    @Autowired
    public VisitDAO(HibernateUtilConfiguration hibernateUtilConfiguration, EntityManagerFactory entityManagerFactory) {
        super(hibernateUtilConfiguration, entityManagerFactory);
    }

    @Override
    public boolean create(Visit visit) {
        return save(visit);
    }

    @Override
    public boolean delete(Visit obj) {

        Session session = this.sessionFactory.openSession();
        session.delete(obj);
        return false;
    }

    @Transactional
    public boolean deleteByUuid(UUID visitUuid){

        Visit v = findByUuid(visitUuid);
        assertNotNull(v);
        entityManager.joinTransaction();
        entityManager
                .createNativeQuery("delete from Visit where uuid = :visitUuid ")
                .setParameter("visitUuid", v.getUuid())
                .executeUpdate();
        return true;
    }

    @Override
    public boolean update(Visit obj) {
        return false;
    }

    @Override
    public List<Visit> findAll() {
        return null;
    }

    public List findUserVisits(UUID userUuid){
        Session session = this.sessionFactory.openSession();
        String hql = "from Visit v where v.zone.user.uuid = :userUuid ";
        Query q = session.createQuery(hql);
        q.setParameter("userUuid", userUuid);

        return q.list();
    }

    public List findUserVisitsByZoneUuid(UUID userUuid, UUID zoneUuid){

        Session session = this.sessionFactory.openSession();
        String hql = "from Visit v where v.zone.uuid = :zoneUuid and v.zone.user.uuid = :userUuid ";
        Query q = session.createQuery(hql);
        q.setParameter("userUuid", userUuid);
        q.setParameter("zoneUuid", zoneUuid);

        return q.list();
    }

    @Override
    public Visit findByUuid(UUID uuid) {
        return  entityManager.find(Visit.class,uuid);
    }
}
