package com.ecommerce.main.serviceImpl;

import com.ecommerce.main.entity.Role;
import com.ecommerce.main.entity.User;
import com.ecommerce.main.exceptions.UserNotFoundException;
import com.ecommerce.main.repository.RoleRepository;
import com.ecommerce.main.repository.UserRepository;
import com.ecommerce.main.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> listAll() {
        return userRepository.findAll();
    }

    @Override
    public List<Role> listRoles() {
        return roleRepository.findAll();
    }

    @Override
    public User saveUser(User user) {
        boolean newUser = user.getId() == null;
        if(newUser)
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        else{
            User oldUser = userRepository.findById(user.getId()).get();
            if(user.getPassword().isEmpty()) user.setPassword(oldUser.getPassword());
            else user.setPassword(
                    passwordEncoder.encode(
                            user.getPassword())
                    );
        }

        return userRepository.save(user);
    }

    @Override
    public boolean uniqueEmail(Integer id, String email) {
        User data = userRepository.getUserByEmail(email);
        if (data == null) return true;
        boolean isCreatingNew = (id == null);
        if (isCreatingNew){
            if (data != null) return false;
        }else{
            if(data.getId() != id) return false;
        }
        return true;
    }

    @Override
    public User findById(Integer id) throws UserNotFoundException{
        try {
            return userRepository.findById(id).get();
        }catch (NoSuchElementException e){
            throw new UserNotFoundException("Could not find any user with ID "+id);
        }
    }
}
