package ru.jswap.dao.intefaces;

import ru.jswap.entities.GroupAuthority;

import java.util.List;

public interface GroupAuthoritiesDAO {

    List<GroupAuthority> getAuthorities(long id);
    List<GroupAuthority> getAuthorities(String username);
}
