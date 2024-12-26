package com.taskhive.backend.service;

import com.taskhive.backend.model.RegisterUser;
import com.taskhive.backend.repository.RegisterUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service

public class RegisterUserService {
    @Autowired
    RegisterUserRepository registerUserRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public RegisterUser saveNewUser(RegisterUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return registerUserRepository.save(user);
    }

    public RegisterUser loadUserByUsername(String username) {
        return registerUserRepository.findByUsername(username);
    }
}
