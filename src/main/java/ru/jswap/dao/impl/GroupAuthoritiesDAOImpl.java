package ru.jswap.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.jswap.dao.intefaces.GroupAuthoritiesDAO;
import ru.jswap.dao.intefaces.GroupMembersDAO;
import ru.jswap.entities.GroupAuthority;

import java.util.List;

@Component
public class GroupAuthoritiesDAOImpl implements GroupAuthoritiesDAO {
    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    GroupMembersDAO groupMembersDAO;

    @Transactional
    @Override
    public List<GroupAuthority> getAuthorities(long groupId) {
        return (List<GroupAuthority>) sessionFactory.getCurrentSession()
                .createQuery("from GroupAuthority where groupId = :groupId").setLong("groupId", groupId)
                .list();
    }

    @Transactional
    @Override
    public List<GroupAuthority> getAuthorities(String username) {
        Long groupId = groupMembersDAO.getGroupMember(username).getGroupId();
        return (List<GroupAuthority>)sessionFactory.getCurrentSession()
                .createQuery("from GroupAuthority where groupId = :groupId").setLong("groupId", groupId).list();
    }
}
