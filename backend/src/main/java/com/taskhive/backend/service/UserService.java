package com.taskhive.backend.service;

import com.taskhive.backend.model.RegisterUser;
import com.taskhive.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service

public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public RegisterUser saveNewUser(RegisterUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public RegisterUser loadUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
