package ru.jswap.dao.intefaces;

import ru.jswap.entities.Feeds;
import ru.jswap.entities.User;

import java.util.List;

public interface FeedsDAO {
    Feeds getFeed(long id);
    Feeds getFeed(String feedname, User user);
    List<Feeds> getFeeds(User user);
    long saveFeed(Feeds feed);
    void deleteFeed(Feeds feed);
    void updateFeed(Feeds feed);
}
