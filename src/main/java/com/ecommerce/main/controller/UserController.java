package com.ecommerce.main.controller;

import com.ecommerce.main.entity.User;
import com.ecommerce.main.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listAll(Model model){
        model.addAttribute("listUsers", userService.listAll());
        return "admin/users";
    }
    @GetMapping("/new")
    public String newUserHandler(Model model){
        User user = new User();
        user.setEnabled(true);
        model.addAttribute("user", user);
        model.addAttribute("listRoles", userService.listRoles());
        return "admin/user_form";
    }
    @PostMapping("/save")
    public String saveUserHandler(@ModelAttribute("user") User user,
                                  RedirectAttributes redirectAttributes){
        User data = userService.saveUser(user);
        if(data != null)
            redirectAttributes.addFlashAttribute("message", "The user has been saved successfully");
        else
            redirectAttributes.addFlashAttribute("message", "Something Wrong!!!");
    return "redirect:/users";
    }
}