package com.ecommerce.main.service;

import com.ecommerce.main.entity.Role;
import com.ecommerce.main.entity.User;

import java.util.List;

public interface UserService {
    public List<User> listAll();
    public List<Role> listRoles();
    public User saveUser(User user);
    public boolean uniqueEmail(String email);
}
