package com.ecommerce.main.service;

import com.ecommerce.main.entity.Role;
import com.ecommerce.main.entity.User;
import com.ecommerce.main.exceptions.UserNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    public static final int USER_PER_PAGE = 4;

    public List<User> listAll();
    public List<Role> listRoles();
    public User saveUser(User user);
    public boolean uniqueEmail(Integer id, String email);
    public  User deleteUser(Integer id) throws UserNotFoundException;
    public User findById(Integer id) throws UserNotFoundException;
    public void updateUserStatus(Integer id, boolean enabled);
    public Page<User> listByPage(Integer pageNum, String sortField, String sortDir);
}
