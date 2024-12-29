package com.taskhive.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskhive.backend.config.SecurityConfiguration;
import com.taskhive.backend.model.RegisterUser;
import com.taskhive.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RegisterUserController.class)
@Import(SecurityConfiguration.class)
public class RegisterUserControllerTest {
    @MockBean
    UserService userService;

    @Autowired
    MockMvc mvc;

    RegisterUser user;

    @BeforeEach
    public void init() {
        this.user = RegisterUser.builder().email("ikushalstha@gmail.com").password("password").confirmPassword("password").firstName("firstName").lastName("lastName").username("newuser").uid(100).build();
    }

    @Test
    public void RegisterUserController_Register_ReturnsResponseEntity() throws Exception {
        when(userService.saveNewUser(user)).thenReturn(user);
        mvc.perform(post("/register").contentType("application/json").content(jsonToString(user))).andExpect(status().isCreated());
    }


    @Test
    public void RegisterUserController_Register_Returns_UNRPROCESSABLE_ENTITY() throws Exception {
        user.setUsername("!@#");
        mvc.perform(post("/register").contentType("application/json").content(jsonToString(user))).andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void RegisterUserController_Register_Returns_INERNAL_SERVERERROR() throws Exception {
        user.setUid(-1);
        when(userService.saveNewUser(user)).thenReturn(user);
        mvc.perform(post("/register").contentType("application/json").content(jsonToString(user))).andExpect(status().isInternalServerError());
    }

    public static String jsonToString(RegisterUser user) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(user);
    }
}