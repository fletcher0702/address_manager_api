package com.fantech.addressmanager.api.dao;

import com.fantech.addressmanager.api.util.HibernateUtilConfiguration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;
import java.util.UUID;

public abstract class DAO<T> {

    SessionFactory sessionFactory;

    @Autowired
    @Lazy
    public DAO(HibernateUtilConfiguration hibernateUtilConfiguration) {
        this.sessionFactory = hibernateUtilConfiguration.getSessionFactory();
    }

    /**
     * Create method
     * @param obj
     * @return boolean
     */
    public abstract boolean create(T obj);

    public boolean save(T obj){
        Session session = this.sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.save(obj);
        tx.commit();
        session.close();
        return true;
    }

    public boolean updateObj(T obj){
        Session session = this.sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.update(obj);
        tx.commit();
        session.close();
        return true;
    }

    /**
     * Delete method
     * @param obj
     * @return boolean
     */
    public abstract boolean delete(T obj);

    /**
     * Update method
     * @param obj
     * @return boolean
     */
    public abstract boolean update(T obj);

    /**
     *  Method for finding related informations
     * @return boolean
     */
    public abstract List<T> findAll();

    /**
     *  Method for finding related informations
     * @param uuid
     * @return boolean
     */
    public abstract T findByUuid(UUID uuid);

}
