package com.fantech.addressmanager.api.dao;

import com.fantech.addressmanager.api.util.HibernateUtilConfiguration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("ALL")
public abstract class DAO<T> {

    protected EntityManager entityManager;
    SessionFactory sessionFactory;
    protected Session currentSession;

    @Autowired
    @Lazy
    public DAO(HibernateUtilConfiguration hibernateUtilConfiguration,EntityManagerFactory entityManagerFactory) {
        this.sessionFactory = hibernateUtilConfiguration.getSessionFactory();
        this.entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Create method
     * @param obj
     * @return boolean
     */
    public abstract boolean create(T obj);

    public boolean save(T obj){
        Session session = getSession();
        Transaction tx = session.beginTransaction();
        session.save(obj);
        tx.commit();
        session.close();
        return true;
    }

    @Transactional
    public boolean updateObj(T obj){
        Session session = getSession();
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

    public Session getSession(){

        Session session;
        if (currentSession!=null){
            if(currentSession.isOpen()) session = currentSession;
            else {
                session = this.sessionFactory.openSession();
                currentSession = session;
            }
        }else{
            session = this.sessionFactory.openSession();
            currentSession = session;
        }

        return session;
    }

    public void flushAndClear(){
        entityManager.flush();
        entityManager.clear();
    }

}
