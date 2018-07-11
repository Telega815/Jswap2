package ru.jswap.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.jswap.dao.intefaces.FeedsDAO;
import ru.jswap.dao.intefaces.FilesDAO;
import ru.jswap.entities.Feeds;
import ru.jswap.entities.User;
import ru.jswap.objects.AccessParams;
import ru.jswap.objects.PinAccess;
import ru.jswap.services.FileService;
import ru.jswap.services.UserService;

@Controller
@SessionAttributes(value = "pinAccess")
public class FeedController {
    @Autowired
    private FeedsDAO feedsDAO;
    @Autowired
    private FileService fileService;
    @Autowired
    private UserService userService;
    @Autowired
    private AccessParams accessParams;

//    @GetMapping(value = "/{username}/{feedname}")
//    public ModelAndView getFeedView(@PathVariable("username") String username,
//                                    @PathVariable("feedname") String feedname,
//                                    @SessionAttribute(value = "user", required = false) User user){
//        ModelAndView modelAndView = new ModelAndView("content/FeedPage");
//        if (user==null) user = userService.getUser(username);
//        Feeds feed = feedsDAO.getFeed(feedname, user);
//        modelAndView.addObject(feed);
//        return modelAndView;
//    }

    @GetMapping(value = "/{username}/{feedname}/checkReadAccess")
    @ResponseBody
    public String checkReadAccess(@PathVariable("username") String username,
                                    @PathVariable("feedname") String feedname,
                                    @SessionAttribute(value = "user", required = false) User user,
                                    @SessionAttribute(value = "pinAccess", required = false) PinAccess pinAccess){
        if (user==null) user = userService.getUser(username);
        Feeds feed = feedsDAO.getFeed(feedname, user);
        accessParams.setParams(feed.getAccesstype());
        return checkAccess(accessParams.getRead(), pinAccess, feed);
    }
    @GetMapping(value = "/{username}/{feedname}/checkWriteAccess")
    @ResponseBody
    public String checkWriteAccess(@PathVariable("username") String username,
                              @PathVariable("feedname") String feedname,
                              @SessionAttribute(value = "user", required = false) User user,
                              @SessionAttribute(value = "pinAccess", required = false) PinAccess pinAccess){
        if (user==null) user = userService.getUser(username);
        Feeds feed = feedsDAO.getFeed(feedname, user);
        accessParams.setParams(feed.getAccesstype());
        return checkAccess(accessParams.getWrite(), pinAccess, feed);
    }

    private String checkAccess(int rights, PinAccess pinAccess, Feeds feed){
        switch (rights){
            case 0:
                return "access-granted";
            case 1:
                if (pinAccess == null || !pinAccess.contains(feed.getUser().getId())){
                    return "pin-required";
                }else {
                    return "access-granted";
                }
            case 2:
                if (userService.checkUser(feed.getUser()))
                    return "access-granted";
                else
                    return "authentication-required";
        }
        return "Smth wrong!!!";
    }

    @GetMapping(value = "/{username}/{feedname}/delete")
    @ResponseBody
    public String deleteFeed(@PathVariable("username") String username,
                                    @PathVariable("feedname") String feedname,
                                    @SessionAttribute(value = "user", required = false) User user){
        if (user==null) user = userService.getUser(username);
        Feeds feed = feedsDAO.getFeed(feedname, user);
        if (userService.checkUser(user)){
            fileService.deletePosts(feed);
            feedsDAO.deleteFeed(feed);
            return "deleted";
        }
        return "failure";
    }

    @PostMapping(value = "/service/createFeed")
    public String createFeed(@SessionAttribute(value = "user") User user,
                                    @ModelAttribute(value = "feed") Feeds feed){
        feed.setUser(user);
        feedsDAO.saveFeed(feed);
        return "redirect:/"+ user.getUsername();
    }
}
