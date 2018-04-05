package ru.jswap.dao.intefaces;

import ru.jswap.entities.Feeds;
import ru.jswap.entities.User;

import java.util.List;

public interface FeedsDAO {
    Feeds getFeed(int id);
    Feeds getFeed(String feedname);
    List<Feeds> getFeeds(User user);
    long saveFeed(Feeds feed);

}
