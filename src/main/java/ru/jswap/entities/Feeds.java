package ru.jswap.entities;

public class Feeds {
    private long id;
    private short accessType;
    private String feedname;
    private User user;

    public Feeds() {
    }

    public Feeds(short accessType, String feedname, User user) {
        this.accessType = accessType;
        this.feedname = feedname;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public short getAccesstype() {
        return accessType;
    }

    public void setAccesstype(short accesstype) {
        this.accessType = accesstype;
    }

    public String getFeedname() {
        return feedname;
    }

    public void setFeedname(String feedname) {
        this.feedname = feedname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Feeds feeds = (Feeds) o;

        if (id != feeds.id) return false;
        if (accessType != feeds.accessType) return false;
        if (feedname != null ? !feedname.equals(feeds.feedname) : feeds.feedname != null) return false;
        if (user != feeds.user) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) accessType;
        result = 31 * result + (feedname != null ? feedname.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
