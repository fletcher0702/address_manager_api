package com.fantech.addressmanager.api.dao;

import com.fantech.addressmanager.api.entity.Team;
import com.fantech.addressmanager.api.util.HibernateUtilConfiguration;
import org.hibernate.Session;
import org.hibernate.query.Query;
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
    public Team findByUuid(UUID uuid) {

        Session session = this.sessionFactory.openSession();
        String hql = "from Team t where t.uuid = :uuid";
        Query q = session.createQuery(hql);
        q.setParameter("uuid", uuid);

        return (Team) q.uniqueResult();
    }

    public Team findUserTeamByUuid(UUID userUuid, UUID teamUuid){
        Session session = this.sessionFactory.openSession();
        String hql = "from Team t where t.adminUuid =: userUuid and t.uuid = :teamUuid";
        Query q = session.createQuery(hql);
        q.setParameter("userUuid", userUuid);
        q.setParameter("teamUuid", teamUuid);

        return (Team) q.uniqueResult();

    }
}
