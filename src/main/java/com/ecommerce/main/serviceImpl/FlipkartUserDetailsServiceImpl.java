package com.ecommerce.main.serviceImpl;

import com.ecommerce.main.entity.User;
import com.ecommerce.main.repository.UserRepository;
import com.ecommerce.main.security.FlipkartUserDetails;
import com.ecommerce.main.service.FlipkartUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class FlipkartUserDetailsServiceImpl implements FlipkartUserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.getUserByEmail(email);
        if(user == null) throw new UsernameNotFoundException("Could not find user with email: " + email);
        return new FlipkartUserDetails(user);
    }
}
