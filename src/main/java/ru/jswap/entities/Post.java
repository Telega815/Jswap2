package ru.jswap.entities;

import java.sql.Date;
import java.sql.Time;
import java.util.Objects;

public class Post {
    private Long postPk;
    private String commentary;
    private Feeds feed;
    private Date date;
    private Time time;
    private long size;
    private String createdBy;

    public Post() {
    }


    public Post(Feeds feed, Date date, Time time) {
        this.feed = feed;
        this.date = date;
        this.time = time;
    }

    public Post(String commentary, Feeds feed, Date date, Time time) {
        this.commentary = commentary;
        this.feed = feed;
        this.date = date;
        this.time = time;
    }

    public Long getPostPk() {
        return postPk;
    }

    public void setPostPk(Long postPk) {
        this.postPk = postPk;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(postPk, post.postPk) &&
                Objects.equals(feed, post.feed) &&
                Objects.equals(commentary, post.commentary) &&
                Objects.equals(date, post.date) &&
                Objects.equals(time, post.time) &&
                Objects.equals(createdBy, post.createdBy) &&
                size == post.size;
    }

    @Override
    public int hashCode() {
        return Objects.hash(postPk, commentary, feed, date, time, size , createdBy);
    }


    public Feeds getFeed() {
        return feed;
    }

    public void setFeed(Feeds feed) {
        this.feed = feed;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
