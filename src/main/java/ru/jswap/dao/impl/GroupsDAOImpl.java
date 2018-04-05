package ru.jswap.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.jswap.dao.intefaces.GroupsDAO;
import ru.jswap.entities.Group;
import ru.jswap.entities.User;

import java.util.List;

@Component
public class GroupsDAOImpl implements GroupsDAO {

    @Autowired
    private SessionFactory sessionFactory;


    @Transactional
    @Override
    public List<Group> getGroups() {
        return (List<Group>) sessionFactory.getCurrentSession()
                .createCriteria(User.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
    }

    @Override
    public Group getGroup(Long id) {
        return sessionFactory.getCurrentSession().get(Group.class, id);
    }

    @Transactional
    @Override
    public Group getGroup(String groupName) {
        return (Group) sessionFactory.getCurrentSession()
                .createQuery("from Group where groupName = :groupName").setString("groupName", groupName).list().get(0);
    }
}
