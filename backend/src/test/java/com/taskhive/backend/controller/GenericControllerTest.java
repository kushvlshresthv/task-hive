package com.taskhive.backend.controller;

import com.taskhive.backend.config.SecurityConfiguration;
import com.taskhive.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenericController.class)
@Import(SecurityConfiguration.class)
public class GenericControllerTest {
    @Autowired
    MockMvc mvc;

    //SecurityConfiguration has UserDetailsService that requires RegisterUserService dependency.
    @MockBean
    UserService userService;


    @Test
    @WithMockUser
    public void GenericController_IsAuthenticated_Returns_ACCEPTED() throws Exception {
        mvc.perform(get("/isAuthenticated")).andExpect(status().isAccepted());
    }

    @Test
    @WithAnonymousUser
    public void GenericController_IsAuthenticated_Return_NOT_ACCEPTABLE() throws Exception {
        mvc.perform(get("/isAuthenticated")).andExpect(status().isNotAcceptable());
    }
}
