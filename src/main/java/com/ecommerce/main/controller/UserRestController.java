package com.ecommerce.main.controller;

import com.ecommerce.main.service.UserService;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {
    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/users/check_email")
    public String checkDuplicateEmail(@Param("id") Integer id, @Param("email") String email){
        return userService.uniqueEmail(id, email) ? "Ok" : "Duplicated";
    }
}
