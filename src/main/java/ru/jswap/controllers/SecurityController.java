package ru.jswap.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.jswap.entities.User;
import ru.jswap.objects.PinAccess;
import ru.jswap.services.UserService;

@Controller
@SessionAttributes(value = "pinAccess")
public class SecurityController {

    @Autowired
    private UserService userService;

    @ModelAttribute("pinAccess")
    public PinAccess createPinAccess(){
        return new PinAccess();
    }

    @PostMapping(value = "/{username}/{feedname}/service/checkPin")
    @ResponseBody
    public String checkPin(@SessionAttribute(value = "user", required = false) User user,
                           @PathVariable("username") String username,
                           @RequestBody String pin,
                           @ModelAttribute("pinAccess") PinAccess pinAccess){
        if(user == null) user = userService.getUser(username);
        pin = pin.substring(0,pin.length()-1);
        if (userService.checkPin(user, pin)){
            pinAccess.addNewPage(user.getId());
            return "access-granted";
        }

        return "access-denied";
    }
}
