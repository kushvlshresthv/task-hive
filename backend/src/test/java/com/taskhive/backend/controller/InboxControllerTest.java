package com.taskhive.backend.controller;

import com.taskhive.backend.config.SecurityConfiguration;
import com.taskhive.backend.entity.AppUser;
import com.taskhive.backend.entity.Inbox;
import com.taskhive.backend.entity.Project;
import com.taskhive.backend.response.Response;
import com.taskhive.backend.service.AppUserService;
import com.taskhive.backend.service.InboxService;
import com.taskhive.backend.service.ProjectService;
import com.taskhive.backend.testingtools.SerializerDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {InboxController.class})
@Import(SecurityConfiguration.class)

@Slf4j
public class InboxControllerTest {
    @Autowired
    MockMvc mockMvc;

    //SecurityConfiguration has UserDetailsService that requires AppUserService dependency.
    @MockBean
    AppUserService appUserService;

    @MockBean
    ProjectService projectService;

    @MockBean
    InboxService inboxService;


    AppUser testUser;
    AppUser testUser2;

    @BeforeEach
    public void init() {
        List<Project> testOwnedProjects = new ArrayList<>();
        Project testProject = Project.builder().pid(1).build();
        Project testProject2 = Project.builder().pid(2).build();
        testOwnedProjects.add(testProject);

        testUser = AppUser.builder().uid(100).ownedProjects(testOwnedProjects).build();
        testUser2 = AppUser.builder().uid(200).build();

        Mockito.when(appUserService.loadUserByUsername("TestUser")).thenReturn(testUser);
        Mockito.when(appUserService.loadUserByUsername("TestUser2")).thenReturn(testUser2);

        Mockito.when(projectService.loadProjectByPid(1)).thenReturn(testProject);
        Mockito.when(projectService.loadProjectByPid(2)).thenReturn(testProject2);
    }


    //tests that the user which sends the request is the owner of the project
    @Test
    @WithMockUser(username = "TestUser")
    public void InboxController_CreateProjectInvite_Returns_Project_Not_Owned_By_You() throws Exception {

        //request is being sent with username TestUser which has project with pid = 1 as its owned project.

        MvcResult result = mockMvc.perform(get("/createProjectInvite").header("pid", 2).header("username", "TestUser2")).andExpect(status().isNotFound()).andReturn();

        Response response = SerializerDeserializer.deserialize(result.getResponse().getContentAsString());

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getMessage()).isNotNull();


        log.info("attempting to create a project invite without owning the project");
        log.info("result: " + response.getMessage());
    }


    //tests that the owner of the project(who sends invite) is not the same user who receives the invite
    @Test
    @WithMockUser(username = "TestUser")
    public void InboxController_CreateProjectInvite_Returns_Cant_Create_Invite_For_Owned_Project() throws Exception {


        //here header has the username as TestUser and loadUserByUsername() has been mocked to return the same AppUser object in @BeforeEach

        MvcResult result = mockMvc.perform(get("/createProjectInvite").header("pid", 1).header("username", "TestUser")).andExpect(status().isNotFound()).andReturn();

        Response response = SerializerDeserializer.deserialize(result.getResponse().getContentAsString());

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getMessage()).isNotNull();

        log.info("attempting to create a project invite for an owned project ");
        log.info("result: " + response.getMessage());
    }

    //tests whether the target user with the provided username exists
    @Test
    @WithMockUser(username = "TestUser")
    public void InboxController_CreateProjectInvite_Returns_TargetUser_Does_Not_Exist() throws Exception {
        //user with the username "NoSuchUser" has not been configured in the mock environment
        MvcResult result = mockMvc.perform(get("/createProjectInvite").header("pid", 1).header("username", "NoSuchUser")).andExpect(status().isNotFound()).andReturn();

        Response response = SerializerDeserializer.deserialize(result.getResponse().getContentAsString());

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getMessage()).isNotNull();

        log.info("attempting to create a project invite for a user which does not exist");
        log.info("result: " + response.getMessage());
    }


    //tests whether the project with given pid exists
    @Test
    @WithMockUser(username = "TestUser")
    public void InboxController_CreateProjectInvite_Returns_TargetProject_Does_Not_Exist() throws Exception {

        //project with pid = 2 has not been configured in the mock environment
        MvcResult result = mockMvc.perform(get("/createProjectInvite").header("pid", 3).header("username", "TestUser2")).andExpect(status().isNotFound()).andReturn();

        Response response = SerializerDeserializer.deserialize(result.getResponse().getContentAsString());

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getMessage()).isNotNull();

        log.info("attempting to create a project invite for a project which does not exist");
        log.info("result: " + response.getMessage());
    }

    //tests the condition when the Inbox could not be saved to the database and inboxId <=0
    @Test
    @WithMockUser(username = "TestUser")
    public void InboxController_CreateProjectInvite_Returns_Project_Could_Not_Be_Saved() throws Exception {
        AppUser anotherTestUser = AppUser.builder().uid(200).build();

        Mockito.when(appUserService.loadUserByUsername("anotherTestUser")).thenReturn(anotherTestUser);

        //saveInbox returns Inbox object with id = 0 which triggers InternalServerError
        Inbox inbox = Inbox.builder().inboxId(0).build();
        Mockito.when(inboxService.saveInbox(Mockito.any())).thenReturn(inbox);

        //user with the username "antherTestUser" and project with pid = 1 has been configured for the current user.

        MvcResult result = mockMvc.perform(get("/createProjectInvite").header("pid", 1).header("username", "anotherTestUser")).andExpect(status().isInternalServerError()).andReturn();

        Response response = SerializerDeserializer.deserialize(result.getResponse().getContentAsString());

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getMessage()).isNotNull();
        log.info("inbox failed to be stored in the database");
    }


    //tests the success case

    @Test
    @WithMockUser(username = "TestUser")
    public void InboxController_CreateProjectInvite_Returns_OK() throws Exception {
        AppUser anotherTestUser = AppUser.builder().uid(200).build();

        Mockito.when(appUserService.loadUserByUsername("anotherTestUser")).thenReturn(anotherTestUser);

        Inbox inbox = Inbox.builder().inboxId(1).build();
        Mockito.when(inboxService.saveInbox(Mockito.any())).thenReturn(inbox);

        //user with the username "antherTestUser" and project with pid = 1 has been configured for the current user.

        MvcResult result = mockMvc.perform(get("/createProjectInvite").header("pid", 1).header("username", "anotherTestUser")).andExpect(status().isOk()).andReturn();

        Response response = SerializerDeserializer.deserialize(result.getResponse().getContentAsString());

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getMessage()).isNotNull();

        log.info("attempting to create a successful project invite");
        log.info("result: " + response.getMessage());
    }
}
