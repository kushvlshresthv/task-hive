package com.taskhive.backend.service;


import com.taskhive.backend.entity.AppUser;
import com.taskhive.backend.repository.AppUserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = {AppUserService.class})
public class AppUserServiceTest {
    AppUser user = AppUser.builder().email("ikushalstha@gmail.com").password("nopass").confirmPassword("nopass").firstName("firstName").lastName("lastName").username("newuser").build();

    @MockBean
    AppUserRepository appUserRepository;

    @MockBean
    PasswordEncoder passwordEncoder;

    @Autowired
    AppUserService appUserService;

    @Test
    public void RegisterUserService_SaveNewUser_ReturnsSavedUser() {
        System.out.println(appUserRepository.getClass().getName());
        AppUser savedUser = appUserService.saveNewUser(user);
        Assertions.assertThat(savedUser).isNotNull();
    }

    @BeforeEach
    public void init() {
        when(appUserRepository.save(user)).thenReturn(user);
        when(passwordEncoder.encode(user.getPassword())).thenReturn(new BCryptPasswordEncoder().encode(user.getPassword()));
    }

    @Test
    public void RegisterUserService_LoadUserByUsername_ReturnsFetchedUser() {
        when(appUserRepository.findByUsername("newuser")).thenReturn(user);
        AppUser registerUser = appUserService.loadUserByUsername("newuser");
        Assertions.assertThat(registerUser).isNotNull();
        Assertions.assertThat(registerUser.getUsername()).isEqualTo("newuser");
    }
}
