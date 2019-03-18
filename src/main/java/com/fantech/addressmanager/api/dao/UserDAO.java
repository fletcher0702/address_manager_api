package com.fantech.addressmanager.api.dao;

import com.fantech.addressmanager.api.entity.User;
import com.fantech.addressmanager.api.helpers.AuthService;
import com.fantech.addressmanager.api.util.HibernateUtilConfiguration;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Repository
public class UserDAO extends DAO<User> {

    @Autowired
    public UserDAO(HibernateUtilConfiguration hibernateUtilConfiguration, EntityManagerFactory entityManagerFactory) {
        super(hibernateUtilConfiguration, entityManagerFactory);
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

    @Override
    public boolean delete(User obj) {
        return false;
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
       return entityManager.find(User.class,uuid);
    }


    public User findByEmail(String email) {

        Session session = this.sessionFactory.openSession();
        String hql = "from User u where u.email=:email";
        Query q = session.createQuery(hql);
        q.setParameter("email", email);
        User u =  getUser(q);
        session.close();
        return u;

    }

    private User getUser(Query q) {
        return (User) q.uniqueResult();
    }
}
