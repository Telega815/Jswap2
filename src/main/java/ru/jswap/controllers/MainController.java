package ru.jswap.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.jswap.entities.Feeds;
import ru.jswap.entities.User;
import ru.jswap.services.HtmlService;
import ru.jswap.services.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Handles requests for the application home page.
 */

@Controller
@SessionAttributes(value = "user")
public class MainController {

	private static final Logger logger = LoggerFactory.getLogger(MainController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private HtmlService htmlService;

	@GetMapping(value = "/")
	public ModelAndView mainPage(@RequestParam(value = "error", required = false) String error) {
		ModelAndView modelAndView = new ModelAndView();
		User user = new User();

		if (error != null) {
			modelAndView.addObject("error", "Couldn't find user with that name.");
		}

		modelAndView.setViewName("findUser");
		modelAndView.addObject(user);
		return modelAndView;
	}

	@GetMapping(value = "/{urlpart}")
	public ModelAndView main(@PathVariable("urlpart") String urlpart,
							 @ModelAttribute("user") User user) {
		ModelAndView modelAndView = new ModelAndView();
		String feedsHtml;
		List<Feeds> feeds;
		switch (urlpart) {
			case "favicon":
				break;
			default:
				User checkedUser = userService.getUser(urlpart);
				if (checkedUser != null) {
					feeds = userService.getFeeds(checkedUser);
					if (!feeds.isEmpty()){
						feedsHtml = htmlService.getFeedsHtml(feeds, false);
						modelAndView.addObject("feeds", feedsHtml);
					}


					modelAndView.setViewName("/content/userPage");
					modelAndView.addObject("user", checkedUser);
					modelAndView.addObject("feed", new Feeds());
					modelAndView.addObject("accessToPageContent", userService.checkUser(checkedUser));
				}else{
					modelAndView.setViewName("redirect:/?error=true");
				}
				break;
		}
		return modelAndView;
	}

	@GetMapping(value = "/service/login")
	public ModelAndView login(@RequestParam(value = "error", required = false) String error){
		ModelAndView modelAndView = new ModelAndView();
		if (error != null) {
			modelAndView.addObject("error", "Invalid username or password!");
		}
		modelAndView.setViewName("login");
		return modelAndView;
	}

	@PostMapping(value = "/service/findUser")
	public String redirectToPage(@ModelAttribute("user") User user){
		return "redirect:/"+user.getUsername();
	}

	@GetMapping(value = "/service/goToUserPage")
	public String goToUserPage(HttpServletRequest request){
		return "redirect:/"+request.getUserPrincipal().getName();
	}

	@GetMapping(value = "/service/createUser")
	public String goToRegistration(@ModelAttribute("user") User user){
		return "createUser";
	}

	@PostMapping(value = "/service/createUser")
	public String createUser(@ModelAttribute("user") User user){
//		ModelAndView modelAndView = new ModelAndView();
		userService.createUser(user);
//		modelAndView.setViewName();
		return "redirect:/";
	}



}
