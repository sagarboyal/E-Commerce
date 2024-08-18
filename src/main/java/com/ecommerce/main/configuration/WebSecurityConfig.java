package com.ecommerce.main.configuration;

import com.ecommerce.main.serviceImpl.FlipkartUserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(){
        return new FlipkartUserDetailsServiceImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }



    @Bean
    SecurityFilterChain configureHttpSecurity(HttpSecurity http) throws Exception {

        http.authenticationProvider(daoAuthenticationProvider());

        http.authorizeHttpRequests(auth -> auth
                                .anyRequest().authenticated() // All other requests require authentication
                )
                .formLogin(form -> form
                        .loginPage("/login") // Custom login page
                        .usernameParameter("email")
                        .permitAll() // Allow access to the login page with authentication
                        .defaultSuccessUrl("/", true) // Redirect to /home after successful login
                );
        return http.build();
    }

    @Bean
    WebSecurityCustomizer securityCustomizer (){
         // Allow access to static resources images
        return web -> web.ignoring().requestMatchers("/images/**", "/js/**", "/webjars/**", "/font%20awesome/**");
    }
}
