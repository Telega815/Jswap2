package ru.jswap.entities;

import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

public class GroupAuthority implements GrantedAuthority{
    private long id;
    private long groupId;
    private String authority;

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupAuthority that = (GroupAuthority) o;
        return groupId == that.groupId &&
                Objects.equals(authority, that.authority);
    }

    @Override
    public int hashCode() {

        return Objects.hash(groupId, authority);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
