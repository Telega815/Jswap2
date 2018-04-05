package ru.jswap.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.jswap.dao.intefaces.GroupMembersDAO;
import ru.jswap.entities.GroupMember;

@Component
public class GroupMembersDAOImpl implements GroupMembersDAO {
    @Autowired
    SessionFactory sessionFactory;

    @Transactional
    @Override
    public GroupMember getGroupMember(String username) {
        return (GroupMember) sessionFactory.getCurrentSession()
                .createQuery("from GroupMember where username = :username").setString("username", username)
                .list().get(0);
    }

    @Transactional
    @Override
    public void saveUser(Long group_id, String username) {
        GroupMember groupMember = new GroupMember();
        groupMember.setUsername(username);
        groupMember.setGroupId(group_id);
        sessionFactory.getCurrentSession().save(groupMember);
    }
}
