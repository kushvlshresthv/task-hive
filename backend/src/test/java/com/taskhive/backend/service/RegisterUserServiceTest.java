package com.taskhive.backend.service;


import com.taskhive.backend.model.RegisterUser;
import com.taskhive.backend.repository.RegisterUserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = {RegisterUserService.class})
public class RegisterUserServiceTest {
    RegisterUser user = RegisterUser.builder().email("ikushalstha@gmail.com").password("nopass").confirmPassword("nopass").firstName("firstName").lastName("lastName").username("newuser").build();

    @MockBean
    RegisterUserRepository registerUserRepository;

    @MockBean
    PasswordEncoder passwordEncoder;

    @Autowired
    RegisterUserService registerUserService;

    @Test
    public void RegisterUserService_SaveNewUser_ReturnsSavedUser() {
        System.out.println(registerUserRepository.getClass().getName());
        RegisterUser savedUser = registerUserService.saveNewUser(user);
        Assertions.assertThat(savedUser).isNotNull();
    }

    @BeforeEach
    public void init() {
        when(registerUserRepository.save(user)).thenReturn(user);
        when(passwordEncoder.encode(user.getPassword())).thenReturn(new BCryptPasswordEncoder().encode(user.getPassword()));
    }

    @Test
    public void RegisterUserService_LoadUserByUsername_ReturnsFetchedUser() {
        when(registerUserRepository.findByUsername("newuser")).thenReturn(user);
        RegisterUser registerUser = registerUserService.loadUserByUsername("newuser");
        Assertions.assertThat(registerUser).isNotNull();
        Assertions.assertThat(registerUser.getUsername()).isEqualTo("newuser");
    }
}
