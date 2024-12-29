package com.taskhive.backend.user_details_service;

import com.taskhive.backend.model.RegisterUser;
import com.taskhive.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class DatabaseUserDetailsService implements UserDetailsService {
    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        RegisterUser registeredUser = userService.loadUserByUsername(username);
        System.out.println("UserDetailsService invoked");
        if (registeredUser != null) {
            UserDetails user = User.withUsername(username).password(registeredUser.getPassword()).build();
            return user;
        }
        throw new UsernameNotFoundException("username not found");
    }
}
