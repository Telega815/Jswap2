package ru.jswap.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.jswap.dao.intefaces.*;
import ru.jswap.entities.Feeds;
import ru.jswap.entities.User;
import ru.jswap.objects.AccessParams;

import java.io.File;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private FeedsDAO feedsDAO;
    @Autowired
    private GroupMembersDAO groupMembersDAO;
    @Autowired
    private GroupsDAO groupsDAO;

    Pattern english = Pattern.compile("[a-zA-Z_0-9]");

    public User getUser(String username) {
        try {
            return userDAO.getUser(username);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Feeds> getFeeds(User user){
        return feedsDAO.getFeeds(user);
    }

    public Feeds getFeed (long feedId){
        return feedsDAO.getFeed(feedId);
    }

    public List<Feeds> getFeedsForWrite(User authenticatedUser, List<Feeds> currentFeeds){
        List<Feeds> feedsForWrite = new ArrayList<>();
        AccessParams accessParams = new AccessParams();
        feedsForWrite.addAll(getFeedsForWrite(currentFeeds));
        feedsForWrite.addAll(getFeeds(authenticatedUser));
        return feedsForWrite;
    }
    public List<Feeds> getFeedsForWrite(List<Feeds> currentFeeds){
        List<Feeds> feedsForWrite = new ArrayList<>();
        AccessParams accessParams = new AccessParams();
        for (Feeds feed:currentFeeds) {
            accessParams.setParams(feed.getAccesstype());
            if (accessParams.getWrite() != 2){
                feedsForWrite.add(feed);
            }
        }
        return feedsForWrite;
    }

    public Feeds getFeed(String feedname, User user){
        return feedsDAO.getFeed(feedname, user);
    }

    public boolean createUser(User user) {
        if (!validateUser(user)) return false;

        user.setPwd(passwordEncoder.encode(user.getPwd()));
        userDAO.saveUser(user);
        groupMembersDAO.saveUser(groupsDAO.getGroup("users").getId(), user.getUsername());
        return true;
    }

    private boolean validateUser(User user){
        if (this.getUser(user.getUsername()) != null){
            return false;
        }else if (user.getUsername().toUpperCase().equals("ANONYMOUS")){
            return false;
        }else if (!Pattern.matches("^[A-Za-z0-9]*$", user.getPwd())){
            return false;
        }else if (!Pattern.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", user.getEmail())){
            return false;
        }else if (user.getPincode() > 9999 || user.getPincode() < 1000){
            return false;
        }
        return true;
    }

    public boolean checkUser(User user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && user.getUsername().equals(authentication.getName());
    }

    public String getAuthenticatedUserName(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if (name.toUpperCase().equals("ANONYMOUSUSER")){
            name = "ANONYMOUS";
        }
        return name;
    }

    public Feeds newFeedWrite(User user, String feedName, short modeRead, short modeWrite, int limitSize, Boolean sizeType ){
        Feeds feed = null;
        long sileLimit;
        if (feedsDAO.getFeed(feedName, user)==null && modeRead>=0 && modeRead<=2 && modeWrite>=0 && modeWrite<=2 && limitSize>=0 && limitSize<=9999){
            feed = new Feeds();
            feed.setUser(user);
            feed.setFeedname(feedName);

            AccessParams params =new AccessParams(modeRead, modeWrite);
            feed.setAccesstype(params.getParams());
            if (sizeType) {
                sileLimit=(long)limitSize*1024*1024*1024;
            }
            else{
                sileLimit=(long)limitSize*1024*1024;
            }
            feed.setLimit(sileLimit);
            feedsDAO.saveFeed(feed);
        }
        return feed;
    }

    public boolean checkPin(User user, String pin){
        return user.getPincode() == Integer.valueOf(pin);
    }
}
