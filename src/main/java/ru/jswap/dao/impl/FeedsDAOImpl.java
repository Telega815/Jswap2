package ru.jswap.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.jswap.dao.intefaces.FeedsDAO;
import ru.jswap.entities.Feeds;
import ru.jswap.entities.User;

import java.util.List;

@Component
public class FeedsDAOImpl implements FeedsDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    @Override
    public Feeds getFeed(int id) {
        return sessionFactory.getCurrentSession().get(Feeds.class, id);
    }

    @Transactional
    @Override
    public Feeds getFeed(String feedname, User user) {
        return (Feeds) sessionFactory.getCurrentSession()
                .createQuery("from Feeds where feedname = :feedname and user = :user")
                .setParameter("feedname", feedname)
                .setParameter("user", user).list().get(0);
    }

    @Transactional
    @Override
    public List<Feeds> getFeeds(User user) {
        return sessionFactory.getCurrentSession()
                .createQuery("from Feeds where user = :user")
                .setParameter("user", user).list();
    }

    @Transactional
    @Override
    public long saveFeed(Feeds feed) {
        return (long)sessionFactory.getCurrentSession().save(feed);
    }

    @Transactional
    @Override
    public void deleteFeed(Feeds feed) {
        sessionFactory.getCurrentSession().delete(feed);
    }

    @Transactional
    @Override
    public void updateFeed(Feeds feed) {
        sessionFactory.getCurrentSession().update(feed);
    }
}
