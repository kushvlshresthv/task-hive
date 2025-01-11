package com.taskhive.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.taskhive.backend.config.SecurityConfiguration;
import com.taskhive.backend.entity.AppUser;
import com.taskhive.backend.entity.Project;
import com.taskhive.backend.service.AppUserService;
import com.taskhive.backend.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectController.class)
@Import({SecurityConfiguration.class})
public class ProjectControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    ProjectService projectService;

    Project project;
    AppUser user;

    //AppUserService is used by SecurityConfiguration
    @MockBean
    AppUserService appUserService;

    @BeforeEach
    public void init() {
        user = AppUser.builder().email("ikusshalstha@gmail.com").password("nopass").confirmPassword("nopass").firstName("firstName").lastName("lastName").username("newuser").uid(1001).build();

        project = Project.builder().projectName("new").projectDescription("description").startDate(LocalDate.now()).finishDate(LocalDate.of(2025, 2, 12)).priority("high").projectType("business").build();

        Mockito.when(projectService.createProject(project)).thenReturn(project);
    }

    //Test with valid Project data
    @Test
    @WithMockUser
    public void ProjectControllerTest_CreateProject_Return_HTTP_OK() throws Exception {
        mvc.perform(post("/createProject").contentType("application/json").content(jsonToString(project))).andExpect(status().isOk());
    }

    //Test with invalid Project Data
    @Test
    @WithMockUser
    public void ProjectControllerTest_CreateProject_Return_HTTP_INTERNAL_SERVER_ERROR() throws Exception {
        project.setProjectName(null);
        mvc.perform(post("/createProject").contentType("application/json").content(jsonToString(project))).andExpect(status().isInternalServerError());
    }

    //Test when ProjectService returns an invalid saved Project object
    @Test
    @WithMockUser

    public void ProjectControllerTest_Create_Project_Return_HTTP_INTERNAL_SERVER_ERRORR() throws Exception {
        Project invalidProject = Project.builder().pid(-1).build();
        Mockito.when(projectService.createProject(project)).thenReturn(invalidProject);
        mvc.perform(post("/createProject").contentType("application/json").content(jsonToString(project))).andExpect(status().isInternalServerError());

    }

    public static String jsonToString(Project project) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.writeValueAsString(project);
    }
}
