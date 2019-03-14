package com.fantech.addressmanager.api.dao;

import com.fantech.addressmanager.api.dto.status.StatusDto;
import com.fantech.addressmanager.api.entity.Status;
import com.fantech.addressmanager.api.entity.Team;
import com.fantech.addressmanager.api.entity.User;
import com.fantech.addressmanager.api.entity.Zone;
import com.fantech.addressmanager.api.util.HibernateUtilConfiguration;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Repository
public class TeamDAO extends DAO<Team> {

    private ZoneDAO zoneDAO;
    @Autowired
    public TeamDAO(HibernateUtilConfiguration hibernateUtilConfiguration, EntityManagerFactory entityManagerFactory,ZoneDAO zoneDAO) {
        super(hibernateUtilConfiguration, entityManagerFactory);
        this.zoneDAO = zoneDAO;
    }

    @Override
    public boolean create(Team obj) {
        return save(obj);
    }

    @Transactional
    @Override
    public boolean delete(Team t) {

        entityManager.joinTransaction();
        assertNotNull(t);
        Team td = entityManager.find(Team.class,t.getUuid());

        entityManager
                .createNativeQuery("delete from user_team where team_uuid = :teamUuid")
                .setParameter("teamUuid", t.getUuid())
                .executeUpdate();


        for(Zone zone : td.getZones()){
            zoneDAO.delete(zone);
        }

        td.getUsers().clear();
        entityManager.persist(td);
        entityManager.flush();

        entityManager
                .createNativeQuery("delete from team where uuid = :teamUuid")
                .setParameter("teamUuid", t.getUuid())
                .executeUpdate();


        return true;
    }

    @Override
    public boolean update(Team obj) {
        return false;
    }

    @Transactional
    public boolean addStatus(UUID teamUuid, List<StatusDto> status){
        entityManager.joinTransaction();
        Team t = findByUuid(teamUuid);

        assertNotNull(t);

        for(StatusDto st: status){
            Status tmp = new Status();

            assertNotNull(st.getName());
            assertNotNull(st.getColor());

            tmp.setTeam(t);
            tmp.setName(st.getName());
            tmp.setColor(st.getColor());
            t.getStatus().add(tmp);
        }

        Session session = getSession();
        Transaction tx = session.beginTransaction();
        session.update(t);
        tx.commit();
        return true;

    }

    @Override
    public List findAll() {
        return null;
    }

    @Transactional
    @Override
    public Team findByUuid(UUID uuid) {
        return entityManager.find(Team.class, uuid);
    }

    public Team findUserTeamByUuid(UUID userUuid, UUID teamUuid) {
        Session session = this.sessionFactory.openSession();
        String hql = "from Team t where t.adminUuid =:userUuid and t.uuid =:teamUuid";
        Query q = session.createQuery(hql);
        q.setParameter("userUuid", userUuid);
        q.setParameter("teamUuid", teamUuid);

        return (Team) q.uniqueResult();

    }

    @Transactional
    public List findUserRelatedTeam(UUID userUuid) {

        Session session = this.sessionFactory.openSession();
        String hql = "from Team t";
        Query q = session.createQuery(hql);

        List<Team> res = q.list();
        List<Team> toReturn = new ArrayList<>();
        for (Team team : res) {
            for (User user : team.getUsers()) {
                if (Objects.equals(user.getUuid(), userUuid)) toReturn.add(team);
            }
            if (!Objects.equals(team.getAdminUuid(), userUuid)) team.getUsers().clear();
        }

        return toReturn;
    }

    @Transactional
    public Status findStatusByUuid(UUID statusUuid){
        return entityManager.find(Status.class,statusUuid);

    }
}
