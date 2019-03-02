package com.fantech.addressmanager.api.util;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class HibernateUtilConfiguration {

    private EntityManagerFactory entityManagerFactory;

    @Autowired
    public HibernateUtilConfiguration(EntityManagerFactory entityManagerFactory,DataSource dataSource) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public SessionFactory getSessionFactory(){
        return this.entityManagerFactory.unwrap(SessionFactory.class);
    }

}
