package com.fantech.addressmanager.api.dao;

import com.fantech.addressmanager.api.entity.Team;
import com.fantech.addressmanager.api.entity.User;
import com.fantech.addressmanager.api.util.HibernateUtilConfiguration;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Repository
public class TeamDAO extends DAO<Team> {

    public TeamDAO(HibernateUtilConfiguration hibernateUtilConfiguration) {
        super(hibernateUtilConfiguration);
    }

    @Override
    public boolean create(Team obj) {
        return save(obj);
    }

    @Override
    public boolean delete(Team obj) {
        return false;
    }

    @Override
    public boolean update(Team obj) {
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

        Team t = (Team) q.uniqueResult();
        session.close();
        return t;
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
}
