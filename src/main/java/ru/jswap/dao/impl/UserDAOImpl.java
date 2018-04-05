package ru.jswap.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.jswap.dao.intefaces.UserDAO;
import ru.jswap.entities.User;

import javax.transaction.Transactional;
import java.util.List;

@Component
public class UserDAOImpl implements UserDAO {

    @Autowired
    private SessionFactory sessionFactory;

    private List<User> users;


    @Transactional
    @Override
    public List<User> getUsers() {
        users = (List<User>) sessionFactory.getCurrentSession()
                .createCriteria(User.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
        return users;
    }

    @Transactional
    @Override
    public User getUser(Integer id) {
        return sessionFactory.getCurrentSession().get(User.class, id);
    }

    @Transactional
    @Override
    public User getUser(String username){
        users = sessionFactory.getCurrentSession()
                .createQuery("from User where username = :username").setString("username", username).list();
        return users.get(0);
    }

    @Transactional
    @Override
    public void saveUser(User user) {
        sessionFactory.getCurrentSession().save(user);
    }

    @Transactional
    @Override
    public void deleteUser(User user) {
        sessionFactory.getCurrentSession().delete(user);
    }

    @Transactional
    @Override
    public void updateUser(User user) {
        sessionFactory.getCurrentSession().update(user);
    }
}
