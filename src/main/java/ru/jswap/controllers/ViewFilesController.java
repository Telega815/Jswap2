package ru.jswap.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.jswap.dao.intefaces.PostsDAO;
import ru.jswap.entities.Feeds;
import ru.jswap.entities.Post;
import ru.jswap.entities.User;
import ru.jswap.services.HtmlService;
import ru.jswap.services.UserService;

import java.util.List;

@Controller
public class ViewFilesController {

    @Autowired
    private PostsDAO postsDAO;

    @Autowired
    private HtmlService htmlService;

    @Autowired
    private UserService userService;


    @GetMapping(value = "/{username}/{feedname}/viewFiles")
    public ModelAndView viewFiles(@SessionAttribute(value = "user",required = false) User user,
                                  @PathVariable("username") String username,
                                  @PathVariable("feedname") String feedname){
        if(user == null) user = userService.getUser(username);
        Feeds feed = userService.getFeed(feedname, user);
        StringBuilder viewString = new StringBuilder();
        ModelAndView modelAndView = new ModelAndView("/content/viewfiles");
        List<Post> posts = postsDAO.getPosts(feed);
        for (Post post: posts) {
            viewString.append(htmlService.getPostHtml(post, userService.checkUser(user)));
        }
        modelAndView.addObject("viewString", viewString);
        return modelAndView;
    }

    @PostMapping(value = "/{username}/{feedname}/getPosts")
    @ResponseBody
    public String getPosts(@SessionAttribute(value = "user",required = false) User user,
                            @PathVariable("username") String username,
                            @PathVariable("feedname") String feedname){
        if(user == null) user = userService.getUser(username);
        Feeds feed = userService.getFeed(feedname, user);
        return htmlService.getAllPostsHtml(feed, userService.checkUser(user));
    }


}
