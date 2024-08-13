package com.ecommerce.main.serviceImpl;

import com.ecommerce.main.entity.Role;
import com.ecommerce.main.entity.User;
import com.ecommerce.main.exceptions.UserNotFoundException;
import com.ecommerce.main.repository.RoleRepository;
import com.ecommerce.main.repository.UserRepository;
import com.ecommerce.main.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional // for enable status direct interact and modify database.
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
        return userRepository.findAll(Sort.by("id").ascending());
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
            if(data != null) return false;
        }else{
            if(data.getId() != id) return false;
        }
        return true;
    }

    @Override
    public User deleteUser(Integer id) throws UserNotFoundException {
       Long count  = userRepository.countById(id);
       User data = null;
       if(count == null || count == 0) throw new UserNotFoundException("Could not find any user with ID "+id);
       else {
           data = findById(id);
           userRepository.deleteById(id);
       }
       return data;
    }

    @Override
    public User findById(Integer id) throws UserNotFoundException{
        try {
            return userRepository.findById(id).get();
        }catch (NoSuchElementException e){
            throw new UserNotFoundException("Could not find any user with ID "+id);
        }
    }

    @Override
    public void updateUserStatus(Integer id, boolean enabled) {
        userRepository.updateEnableStatus(id, enabled);
    }

    @Override
    public Page<User> listByPage(Integer pageNum, String sortField, String sortDir, String keyword){
        Sort sort = Sort.by(sortField);
        sort =  sortDir.equals("asc")? sort.ascending(): sort.descending();
        Pageable pageable = PageRequest.of(pageNum-1, USER_PER_PAGE, sort);
        if(keyword != null) return userRepository.findAll(keyword, pageable);
        return userRepository.findAll(pageable);
    }
}
