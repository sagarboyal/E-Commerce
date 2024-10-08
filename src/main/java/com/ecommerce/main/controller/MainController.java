package com.ecommerce.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String getHome(){
        return "admin/homepage";
    }
    @GetMapping("/login")
    public String getLoginPage(){
        return "admin/login";
    }
//    @PostMapping("/login") // because if login successful then where it will redirected that linked in WebSecurityConfig file.
//    public String processLoginPage(){
//        return getHome();
//    }
}
