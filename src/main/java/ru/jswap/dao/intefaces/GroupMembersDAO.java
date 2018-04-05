package ru.jswap.dao.intefaces;

import ru.jswap.entities.GroupMember;

public interface GroupMembersDAO {
    GroupMember getGroupMember(String username);
    void saveUser(Long group_id, String username);
}
