package ru.jswap.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.jswap.dao.intefaces.FeedsDAO;
import ru.jswap.entities.Feeds;
import ru.jswap.entities.User;

@Controller
public class FeedController {
    @Autowired
    private FeedsDAO feedsDAO;

    @GetMapping(value = "/{username}/{feedname}")
    public ModelAndView getFeedView(@PathVariable("username") String username,
                                    @PathVariable("feedname") String feedname,
                                    @SessionAttribute(value = "user", required = false) User user){
        ModelAndView modelAndView = new ModelAndView("content/FeedPage");
        Feeds feed = feedsDAO.getFeed(feedname);
        modelAndView.addObject(feed);
        return modelAndView;
    }

    @PostMapping(value = "/service/createFeed")
    public String getCreateFeed(@SessionAttribute(value = "user") User user,
                                    @ModelAttribute(value = "feed") Feeds feed){
        feed.setAccesstype((short)0);
        feed.setUser(user);
        feedsDAO.saveFeed(feed);
        return "redirect:/"+ user.getUsername();
    }
}
