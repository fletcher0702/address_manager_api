package com.fantech.addressmanager.api.dao;

import com.fantech.addressmanager.api.entity.User;
import com.fantech.addressmanager.api.helpers.AuthHelper;
import com.fantech.addressmanager.api.util.HibernateUtilConfiguration;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Repository
public class UserDAO extends DAO<User> {

    @Autowired
    public UserDAO(HibernateUtilConfiguration hibernateUtilConfiguration) {
        super(hibernateUtilConfiguration);
    }

    @Override
    public boolean create(User user) {

        User userFound = findByEmail(user.getEmail());
        System.out.println(userFound);
        if (userFound != null) {
            return false;
        }
        user.setPassword(AuthHelper.hashPassword(user.getPassword()));
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

    @Override
    public User findByUuid(UUID uuid) {
        Session session = this.sessionFactory.openSession();
        String hql = "from User u where u.uuid = :uuid";
        Query q = session.createQuery(hql);
        q.setParameter("uuid", uuid);

        return getUser(q);
    }


    public User findByEmail(String email) {

        Session session = this.sessionFactory.openSession();
        String hql = "from User u where u.email=:email";
        Query q = session.createQuery(hql);
        q.setParameter("email", email);
        return getUser(q);

    }

    private User getUser(Query q) {
        return (User) q.uniqueResult();
    }
}
