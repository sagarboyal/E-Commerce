package com.ecommerce.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String getHome(){
        return "admin/homepage";
    }
    @GetMapping("/login")
    public String getLoginPage(){
        return"admin/login";
    }
}
