package com.taskhive.backend.controller;

import com.taskhive.backend.config.SecurityConfiguration;
import com.taskhive.backend.constants.InboxInviteTitle;
import com.taskhive.backend.entity.AppUser;
import com.taskhive.backend.entity.Inbox;
import com.taskhive.backend.entity.Project;
import com.taskhive.backend.response.Response;
import com.taskhive.backend.response.ResponseMessage;
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

        testUser = AppUser.builder().uid(100).username("TestUser").ownedProjects(testOwnedProjects).build();

        testUser2 = AppUser.builder().uid(200).username("TestUser2").build();

        Mockito.when(appUserService.loadUserByUsername("TestUser")).thenReturn(testUser);
        Mockito.when(appUserService.loadUserByUsername("TestUser2")).thenReturn(testUser2);

        Mockito.when(projectService.loadProjectByPid(1)).thenReturn(testProject);
        Mockito.when(projectService.loadProjectByPid(2)).thenReturn(testProject2);
    }


    //1) tests whether the project with given pid exists
    @Test
    @WithMockUser(username = "TestUser")
    public void InboxController_CreateProjectInvite_Returns_TargetProject_Does_Not_Exist() throws Exception {

        //project with pid = 3 has not been configured in the mock environment
        MvcResult result = mockMvc.perform(get("/createProjectInvite").header("pid", 3).header("username", "TestUser2")).andExpect(status().isNotFound()).andReturn();

        Response response = SerializerDeserializer.deserialize(result.getResponse().getContentAsString());

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getMessage()).isNotNull();
        Assertions.assertThat(response.getMessage()).isEqualTo(ResponseMessage.TARGET_PROJECT_NOT_FOUND.getMessage());
    }


    //2) tests whether the target user with the provided username exists
    @Test
    @WithMockUser(username = "TestUser")
    public void InboxController_CreateProjectInvite_Returns_TargetUser_Does_Not_Exist() throws Exception {
        //user with the username "NoSuchUser" has not been configured in the mock environment
        //NOTE: TestUser ie the User that sends the request has pid = 1 as its owned project
        MvcResult result = mockMvc.perform(get("/createProjectInvite").header("pid", 1).header("username", "NoSuchUser")).andExpect(status().isNotFound()).andReturn();

        Response response = SerializerDeserializer.deserialize(result.getResponse().getContentAsString());

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getMessage()).isNotNull();
        Assertions.assertThat(response.getMessage()).isEqualTo(ResponseMessage.TARGET_USER_NOT_FOUND.getMessage());
    }


    //3) tests what happens when user for whom invite is created is also the owner of the project
    @Test
    @WithMockUser(username = "TestUser")
    public void InboxController_CreateProjectInvite_Returns_Cant_Create_Invite_For_Owned_Project() throws Exception {


        //here header has the username as TestUser and loadUserByUsername() has been mocked to return the same AppUser object in @BeforeEach

        MvcResult result = mockMvc.perform(get("/createProjectInvite").header("pid", 1).header("username", "TestUser")).andExpect(status().isNotFound()).andReturn();

        Response response = SerializerDeserializer.deserialize(result.getResponse().getContentAsString());

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getMessage()).isNotNull();
        Assertions.assertThat(response.getMessage()).isEqualTo(ResponseMessage.CANNOT_INVITE_OWNER.getMessage());
    }


    //3) tests the case when the user who creates the request does not own the project
    @Test
    @WithMockUser(username = "TestUser")
    public void InboxController_CreateProjectInvite_Returns_Project_Not_Owned_By_You() throws Exception {

        //request is being sent with username TestUser which has project with pid = 1 as its owned project.

        MvcResult result = mockMvc.perform(get("/createProjectInvite").header("pid", 2).header("username", "TestUser2")).andExpect(status().isNotFound()).andReturn();

        Response response = SerializerDeserializer.deserialize(result.getResponse().getContentAsString());

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getMessage()).isNotNull();
        Assertions.assertThat(response.getMessage()).isEqualTo(ResponseMessage.PROJECT_NOT_OWNED_BY_CURRENT_USER.getMessage())
        ;
    }

    //5) test the case in which project has already been joined by the targetUser

    @Test
    @WithMockUser(username = "TestUser")
    public void InboxController_CreateProjectInvite_Returns_Project_Already_Joined() throws Exception {
        List<Project> targetJoinedProjects = testUser.getOwnedProjects();
        testUser2.setJoinedProjects(targetJoinedProjects);

        //request is being sent with username TestUser which has project with pid = 1 as its owned project.

        MvcResult result = mockMvc.perform(get("/createProjectInvite").header("pid", 1).header("username", "TestUser2")).andExpect(status().isConflict()).andReturn();

        Response response = SerializerDeserializer.deserialize(result.getResponse().getContentAsString());

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getMessage()).isNotNull();
        Assertions.assertThat(response.getMessage()).isEqualTo(ResponseMessage.PROJECT_ALREADY_JOINED.getMessage())
        ;
    }

    //6) tests whether the invite is already present in the targetUser's invite

    @Test
    @WithMockUser(username = "TestUser")
    public void InboxController_CreateProjectInvite_Project_Invite_Already_Sent() throws Exception {

        Inbox inbox = Inbox.builder().pid(1).title(InboxInviteTitle.INVITATION).initiator("TestUser").pid(1).build();
        testUser2.setInboxes(List.of(inbox));

        //request is being sent with username TestUser which has project with pid = 1 as its owned project.
        MvcResult result = mockMvc.perform(get("/createProjectInvite").header("pid", 1).header("username", "TestUser2")).andExpect(status().isConflict()).andReturn();

        Response response = SerializerDeserializer.deserialize(result.getResponse().getContentAsString());

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getMessage()).isNotNull();
        Assertions.assertThat(response.getMessage()).isEqualTo(ResponseMessage.PROJECT_INVITE_ALREADY_SENT.getMessage())
        ;
    }


    //7) tests the condition when the Inbox could not be saved to the database and inboxId <=0
    @Test
    @WithMockUser(username = "TestUser")
    public void InboxController_CreateProjectInvite_Returns_Project_Could_Not_Be_Saved() throws Exception {
        AppUser testUser3 = AppUser.builder().uid(300).build();

        Mockito.when(appUserService.loadUserByUsername("testUser3")).thenReturn(testUser3);

        //saveInbox returns Inbox object with id = 0 which triggers InternalServerError
        Inbox inbox = Inbox.builder().inboxId(0).build();
        Mockito.when(inboxService.saveInbox(Mockito.any())).thenReturn(inbox);

        //user with the username "antherTestUser" and project with pid = 1 has been configured for the current user.
        MvcResult result = mockMvc.perform(get("/createProjectInvite").header("pid", 1).header("username", "testUser3")).andExpect(status().isInternalServerError()).andReturn();

        Response response = SerializerDeserializer.deserialize(result.getResponse().getContentAsString());

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getMessage()).isNotNull();
        Assertions.assertThat(response.getMessage()).isEqualTo(ResponseMessage.PROJECT_INVITATION_CREATION_FAILED.getMessage());
    }


    //8)tests the success case
    @Test
    @WithMockUser(username = "TestUser")
    public void InboxController_CreateProjectInvite_Returns_OK() throws Exception {
        AppUser testUser3 = AppUser.builder().uid(300).build();

        Mockito.when(appUserService.loadUserByUsername("testUser3")).thenReturn(testUser3);

        Inbox inbox = Inbox.builder().inboxId(1).build();
        Mockito.when(inboxService.saveInbox(Mockito.any())).thenReturn(inbox);

        //user with the username "antherTestUser" and project with pid = 1 has been configured for the current user.

        //NOTE: TestUser ie the user that sends the request has pid = 1 as its owned project
        MvcResult result = mockMvc.perform(get("/createProjectInvite").header("pid", 1).header("username", "testUser3")).andExpect(status().isOk()).andReturn();

        Response response = SerializerDeserializer.deserialize(result.getResponse().getContentAsString());

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getMessage()).isNotNull();
        Assertions.assertThat(response.getMessage()).isEqualTo(ResponseMessage.PROJECT_INVITATION_SUCCESS.getMessage());
    }
}
