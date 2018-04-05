package ru.jswap.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.jswap.dao.intefaces.FeedsDAO;
import ru.jswap.dao.intefaces.GroupMembersDAO;
import ru.jswap.dao.intefaces.GroupsDAO;
import ru.jswap.dao.intefaces.UserDAO;
import ru.jswap.entities.Feeds;
import ru.jswap.entities.User;

import java.io.File;
import java.util.List;

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

    public Feeds getFeed(String feedname){
        return feedsDAO.getFeed(feedname);
    }

    public void createUser(User user) {
        user.setPwd(passwordEncoder.encode(user.getPwd()));
        userDAO.saveUser(user);
        groupMembersDAO.saveUser(groupsDAO.getGroup("users").getId(), user.getUsername());
        String rootPath = "C:/tmpFiles";
        File dir = new File(rootPath + File.separator + user.getUsername());
        if(!dir.exists()){
            dir.mkdirs();
        }
    }

    public boolean checkUser(User user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !user.getUsername().equals(authentication.getName())){
            return false;
        }else
            return true;
    }
}
