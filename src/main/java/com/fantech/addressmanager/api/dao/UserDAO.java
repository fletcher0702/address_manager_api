package com.fantech.addressmanager.api.dao;

import com.fantech.addressmanager.api.entity.Team;
import com.fantech.addressmanager.api.entity.User;
import com.fantech.addressmanager.api.entity.Zone;
import com.fantech.addressmanager.api.helpers.AuthService;
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
@Transactional
public class UserDAO extends DAO<User> {

    private TeamDAO teamDAO;


    @Autowired
    public UserDAO(TeamDAO teamDAO, HibernateUtilConfiguration hibernateUtilConfiguration, EntityManagerFactory entityManagerFactory) {
        super(hibernateUtilConfiguration, entityManagerFactory);
        this.teamDAO = teamDAO;
    }

    @Override
    public boolean create(User user) {

        User userFound = findByEmail(user.getEmail());
        System.out.println(userFound);
        if (userFound != null) {
            return false;
        }
        user.setPassword(AuthService.hashPassword(user.getPassword()));
        save(user);

        return true;
    }

    @Transactional
    @Override
    public boolean delete(User user) {

        entityManager.joinTransaction();
        User u = entityManager.find(User.class, user.getUuid());
        assertNotNull(u);

        for (Team team : u.getTeams()) {


            teamDAO.delete(team);

        }

        entityManager.persist(u);
        entityManager.flush();

        entityManager.createNativeQuery("delete from \"user\" where uuid = :userUuid")
                .setParameter("userUuid", u.getUuid())
                .executeUpdate();


        return true;
    }

    @Override
    public boolean update(User user) {
        save(user);
        return true;
    }

    @Override
    public List<User> findAll() {

        Session session = this.sessionFactory.openSession();
        String hql = "from User";
        Query q = session.createQuery(hql);
        return q.list();
    }

    @Transactional
    @Override
    public User findByUuid(UUID uuid) {
        return entityManager.find(User.class, uuid);
    }


    public User findByEmail(String email) {

        Session session = this.sessionFactory.openSession();
        String hql = "from User u where u.email=:email";
        Query q = session.createQuery(hql);
        q.setParameter("email", email);
        User u = getUser(q);
        session.close();
        return u;

    }

    @Transactional
    public boolean updateUserPassword(UUID userUuid, String password) {

        entityManager.joinTransaction();
        User u = entityManager.find(User.class, userUuid);

        u.setPassword(password);

        entityManager.persist(u);
        flushAndClear();
        return true;
    }

    private User getUser(Query q) {
        return (User) q.uniqueResult();
    }
}
