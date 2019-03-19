package com.fantech.addressmanager.api.dao;

import com.fantech.addressmanager.api.dto.status.StatusDto;
import com.fantech.addressmanager.api.dto.team.TeamDto;
import com.fantech.addressmanager.api.dto.team.UpdateTeamDto;
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
    public Object createOne(UUID adminUuid, TeamDto teamDto){

        entityManager.joinTransaction();
        User u = entityManager.find(User.class,adminUuid);
        assertNotNull(u);

        Team t = new Team();
        t.setName(teamDto.getName());
        t.setAdminUuid(adminUuid);
        u.getTeams().add(t);

        entityManager.persist(u);

        flushAndClear();
        return t;

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
        td.getStatus().clear();
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
    public boolean updateTeam(UUID teamUuid, UpdateTeamDto teamDto){

        entityManager.joinTransaction();
        Team team = entityManager.find(Team.class,teamUuid);

        assertNotNull(team);
        assertNotNull(teamDto);
        assertNotNull(teamDto.getName());

        team.setName(teamDto.getName());

        entityManager.persist(team);
        flushAndClear();
        return true;
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

        entityManager.persist(t);
        flushAndClear();
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

    @Transactional
    public void addUserInTeam(UUID teamUuid, ArrayList<UUID> users){

        entityManager.joinTransaction();
        Team team = entityManager.find(Team.class,teamUuid);
        assertNotNull(team);

        for(UUID userUuid : users){
            User user = entityManager.find(User.class,userUuid);
            user.getTeams().add(team);
            entityManager.persist(team);
        }

        flushAndClear();

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

    @Transactional
    public boolean deleteStatus(UUID teamUuid,UUID statusUuid){
        entityManager.joinTransaction();
        assertNotNull(teamUuid);
        assertNotNull(statusUuid);
        entityManager.createNativeQuery("DELETE FROM status where team_uuid = :teamUuid AND uuid = :statusUuid")
                .setParameter("teamUuid",teamUuid)
                .setParameter("statusUuid",statusUuid)
                .executeUpdate();

        return true;
    }

    @Transactional
    public boolean updateStatus(UUID statusUuid, StatusDto statusDto){

        entityManager.joinTransaction();
        Status status = entityManager.find(Status.class,statusUuid);

        assertNotNull(status);

        status.setName(statusDto.getName());
        status.setColor(statusDto.getColor());

        entityManager.persist(status);
        flushAndClear();

        return true;
    }

    @Transactional
    public Status findStatusByUuid(UUID teamUuid, UUID statusUuid){

        Session session = getSession();
        session.beginTransaction();
        String hql = "from Status s where s.uuid= :statusUuid and s.team.uuid= :teamUuid";
        Query q = session.createQuery(hql);
        q.setParameter("statusUuid",statusUuid);
        q.setParameter("teamUuid",teamUuid);

        return (Status) q.uniqueResult();

    }

    @Transactional
    public boolean userBelongsToTeam(UUID teamUuid, UUID userUuid){

        Team team = entityManager.find(Team.class,teamUuid);

        assertNotNull(team);

        for (User user : team.getUsers()){

            if(Objects.equals(user.getUuid(),userUuid)) return true;
        }

        return false;
    }
}
