package ru.jswap.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.jswap.entities.Feeds;
import ru.jswap.entities.User;
import ru.jswap.objects.PinAccess;
import ru.jswap.services.HtmlService;
import ru.jswap.services.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
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

	@ModelAttribute
	public User createUser(){
		return new User();
	}

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
							 @SessionAttribute(value = "user", required = false) User user,
							 HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView();

		switch (urlpart) {
			case "favicon":
				break;
			default:
				User checkedUser = userService.getUser(urlpart);

				if (checkedUser == null){
					modelAndView.setViewName("redirect:/?error=true");
					return modelAndView;
				}

				boolean authenticatedAsPageOwner = userService.checkUser(checkedUser);

				List<Feeds> feeds = userService.getFeeds(checkedUser);
				String feedsHtml;
				if (!feeds.isEmpty()){
					feeds.sort(
							(o1, o2) -> {
								if (o1.getId() < o2.getId()) return -1;
								else return 1;
							});
					feedsHtml = htmlService.getFeedsHtml(feeds, authenticatedAsPageOwner);
				}else {
					feedsHtml = "<p>you don't have any feeds</p>";
				}

				modelAndView.setViewName("/content/userPage");
				modelAndView.addObject("user", checkedUser);
				modelAndView.addObject("feedsHtml", feedsHtml);
				modelAndView.addObject("pinCode", new PinAccess());
				modelAndView.addObject("accessToPageContent", authenticatedAsPageOwner);
				modelAndView.addObject("authUsername", userService.getAuthenticatedUserName());
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
		if (userService.createUser(user))
			return "redirect:/";
		else//TODO this is not a rest method
			return "error: Couldn't create user";
	}

	@PostMapping(value = "/service/checkUsername")
    @ResponseBody
    public String checkUsername(@RequestBody String username){
	    User user = userService.getUser(username.substring(0,username.length()-1));
        if (user == null){
            return "valid";
        }
	    return "This login is already taken";
    }



}
