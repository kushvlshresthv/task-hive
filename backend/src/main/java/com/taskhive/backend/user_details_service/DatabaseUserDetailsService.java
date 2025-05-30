package com.taskhive.backend.user_details_service;

import com.taskhive.backend.entity.AppUser;
import com.taskhive.backend.service.AppUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
public class DatabaseUserDetailsService implements UserDetailsService {
    @Autowired
    AppUserService appUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser registeredUser = appUserService.loadUserByUsername(username);
        log.info("UserDetailsService invoked by: " + username);
        if (registeredUser != null) {
            UserDetails user = User.withUsername(username).password(registeredUser.getPassword()).build();
            return user;
        }
        throw new UsernameNotFoundException("username not found");
    }
}
