package ru.jswap.dao.intefaces;

import ru.jswap.entities.Group;

import java.util.List;

public interface GroupsDAO {

    List<Group> getGroups();
    Group getGroup(Long id);
    Group getGroup(String groupName);

}
