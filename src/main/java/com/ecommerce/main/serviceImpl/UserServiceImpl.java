package com.ecommerce.main.serviceImpl;

import com.ecommerce.main.entity.User;
import com.ecommerce.main.repository.UserRepository;
import com.ecommerce.main.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> listAll() {
        return userRepository.findAll();
    }
}
