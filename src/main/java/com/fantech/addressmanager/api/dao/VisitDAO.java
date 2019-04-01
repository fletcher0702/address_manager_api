package com.fantech.addressmanager.api.dao;

import com.fantech.addressmanager.api.dto.visit.UpdateVisitDto;
import com.fantech.addressmanager.api.entity.History;
import com.fantech.addressmanager.api.entity.Status;
import com.fantech.addressmanager.api.entity.Visit;
import com.fantech.addressmanager.api.entity.Zone;
import com.fantech.addressmanager.api.entity.common.Coordinates;
import com.fantech.addressmanager.api.util.HibernateUtilConfiguration;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
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
        System.out.println(entityManager
                .createNativeQuery("delete from Visit where uuid = :visitUuid ")
                .setParameter("visitUuid", v.getUuid())
                .executeUpdate());
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

    @Transactional
    @Override
    public Visit findByUuid(UUID uuid) {
        return  entityManager.find(Visit.class,uuid);
    }

    @Transactional
    public boolean updateVisitByUuidAdmin(UUID visitUuid, UpdateVisitDto visitDto, Coordinates coordinates){

        entityManager.joinTransaction();
        Visit v = entityManager.find(Visit.class,visitUuid);
        assertNotNull(v);

        System.out.println("Visit found...");

        if(visitDto.getName()!=null){

            if(!visitDto.getName().isEmpty()){
                v.setName(visitDto.getName());
            }
        }
        if(visitDto.getPhoneNumber()!=null){

            if(!visitDto.getPhoneNumber().isEmpty()){
                v.setPhoneNumber(visitDto.getPhoneNumber());
            }

        }
        if(visitDto.getStatusUuid()!=null){
            if(!visitDto.getStatusUuid().isEmpty()){
                Status s = entityManager.find(Status.class,UUID.fromString(visitDto.getStatusUuid()));
                assertNotNull(s);
                System.out.println("Status found...");
                v.setStatus(s);
            }
        }

        if(visitDto.getDate()!=null && !visitDto.getDate().isEmpty()){

            if(v.getHistory() == null){
                v.setHistory(new History());
                v.getHistory().getDates().add(visitDto.getDate());
            }
        }

        if(visitDto.getObservation()!=null && !visitDto.getObservation().isEmpty()){
            v.setObservation(visitDto.getObservation());
        }

        Zone zone = entityManager.find(Zone.class,UUID.fromString(visitDto.getZoneUuid()));

        assertNotNull(zone);

        System.out.println("Good zone for update change...");
        if(Objects.equals(zone.getAdminUuid(),UUID.fromString(visitDto.getUserUuid()))) v.setZone(zone);

        if(coordinates!=null){
            v.setAddress(visitDto.getAddress());
            v.setLatitude(coordinates.getLat());
            v.setLongitude(coordinates.getLng());
        }

        entityManager.persist(v);
        flushAndClear();

        return true;
    }

    @Transactional
    public boolean updateVisitStatus(UUID visitUuid,UUID statusUuid){
        entityManager.joinTransaction();
        Visit v = entityManager.find(Visit.class,visitUuid);
        assertNotNull(v);
        Status s = entityManager.find(Status.class,statusUuid);
        assertNotNull(s);
        v.setStatus(s);

        entityManager.persist(v);
        flushAndClear();

        return true;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public boolean updateVisitHistory(UUID visitUuid,String date){
        entityManager.joinTransaction();
        Visit v = entityManager.find(Visit.class,visitUuid);
        assertNotNull(v);
        assertNotNull(date);
        if(!date.isEmpty()){

            if(v.getHistory() == null){
                v.setHistory(new History());
            }
            boolean present = false;
            for (String s : v.getHistory().getDates()) {
                if(Objects.equals(s,date)) present = true;
            }

            if(!present) v.getHistory().getDates().add(date);
        }

        entityManager.persist(v);
        flushAndClear();

        return true;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public boolean updateVisitObservation(UUID visitUuid,String observation){
        entityManager.joinTransaction();
        Visit v = entityManager.find(Visit.class,visitUuid);
        assertNotNull(v);
        assertNotNull(observation);
        if(!observation.isEmpty()){
            v.setObservation(observation);
        }

        entityManager.persist(v);
        flushAndClear();

        return true;
    }
}
