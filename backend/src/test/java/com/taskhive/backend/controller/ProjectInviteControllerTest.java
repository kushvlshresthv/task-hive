package com.taskhive.backend.controller;

import com.taskhive.backend.config.SecurityConfiguration;
import com.taskhive.backend.constants.InboxInviteTitle;
import com.taskhive.backend.constants.ProjectStatus;
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
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ProjectInviteController.class})
@Import(SecurityConfiguration.class)
@Slf4j
public class ProjectInviteControllerTest {
    @MockitoBean
    private ProjectService projectService;

    @MockitoBean
    private AppUserService appUserService;

    @MockitoBean
    private InboxService inboxService;

    @Autowired
    MockMvc mockMvc;

    /**
     * represents the 'TestUser'
     */
    AppUser testUser;

    /**
     * represents the 'TestUser2'
     */
    AppUser testUser2;


    /**
     * inbox with 'inboxID' = 'TEST_USER_INBOX_ID'
     */
    Inbox inbox;

    /**
     * project with 'pid' = 'TEST_USER_JOINED_PROJECT_ID'
     */
    Project project;

    //for test1
    /**
     * this ID is for the project already joined by Test User
     */
    final int TEST_USER_JOINED_PROJECT_ID = 1000;

    /**
     * this inbox id is from TestUser's Inboxes
     * <p>NOTE: The inbox with this id is set in the dabase during the initial setup</p>
     */
    final int TEST_USER_INBOX_ID = 1;

    //for test2
    /**
     * this inbox id is is not present in the database table
     */
    final int INBOX_ID_NOT_PRESENT_IN_DB = 5;

    /**
     * this project is in the databse but not joined by the user
     */
    final int PROJECT_ID_NOT_JOINED_BY_TEST_USER = 1001;

    //for test3:
    /**
     * this inbox id is present in DB but it is for testUser2
     */
    final int INBOX_ID_FOR_ANOTHER_USER = 6;


    //for test4:
    /**
     * this PID is for 'TestUser' > Inbox's pid property
     * NOTE: the Project with PID is not in the database in the inital setup
     */
    final int PID = 1002;

    /**
     * this PID is not for 'TestUser' > Inbox's pid property
     */
    final int WRONG_PID = 1003;

    @BeforeEach
    public void init() {

        //initial setup:
        testUser = AppUser.builder().uid(100).username("TestUser").build();


        inbox = Inbox.builder().inboxId(TEST_USER_INBOX_ID).pid(PID).user(testUser).title(InboxInviteTitle.INVITATION).build();
        ArrayList<Inbox> inboxes = new ArrayList<>();
        inboxes.add(inbox);
        testUser.setInboxes(inboxes);


        project = Project.builder().pid(TEST_USER_JOINED_PROJECT_ID).status(ProjectStatus.PLANNED).build();
        ArrayList<Project> projects = new ArrayList<>();
        projects.add(project);
        testUser.setJoinedProjects(projects);


        Mockito.when(appUserService.loadUserByUsername("TestUser")).thenReturn(testUser);

        Mockito.when(inboxService.getInboxById(TEST_USER_INBOX_ID)).thenReturn(inbox);
    }


    //1.1 this tests when a user accepts invite in a project and the inbox with the given inboxId does not exists in the database
    @Test
    @WithMockUser(username = "TestUser")
    public void AcceptProjectInvite_Returns_INBOX_DOES_NOT_EXIST_1() throws Exception {

        //setup for test2:
        Mockito.when(inboxService.getInboxById(INBOX_ID_NOT_PRESENT_IN_DB)).thenReturn(null);

        MvcResult result = mockMvc.perform(get("/api/invitedProjects/acceptProjectInvite").header("pid", PROJECT_ID_NOT_JOINED_BY_TEST_USER).header("inboxId", INBOX_ID_NOT_PRESENT_IN_DB)).andExpect(status().isNotAcceptable()).andReturn();

        Response response = SerializerDeserializer.deserialize(result.getResponse().getContentAsString());

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getMessage()).isNotNull();
        Assertions.assertThat(response.getMessage()).isEqualTo(ResponseMessage.INBOX_DOES_NOT_EXIST.getMessage());
    }


    //1.2 this tests when a user accepts invite in a project, the inbox with the given inboxId exists in the database exists, but the inbox is for some other user(not user that accepted invite)


    @Test
    @WithMockUser(username = "TestUser")
    public void AcceptProjectInvite_Returns_INBOX_DOES_NOT_EXIST_2() throws Exception {
        //setup for test3:
        //configuring an inbox for 'testUser2'
        testUser2 = AppUser.builder().uid(200).username("TestUser2").build();
        Inbox inbox2 = Inbox.builder().user(testUser2).build();
        Mockito.when(inboxService.getInboxById(INBOX_ID_FOR_ANOTHER_USER)).thenReturn(inbox2);

        MvcResult result = mockMvc.perform(get("/api/invitedProjects/acceptProjectInvite").header("pid", PROJECT_ID_NOT_JOINED_BY_TEST_USER).header("inboxId", INBOX_ID_FOR_ANOTHER_USER)).andExpect(status().isNotAcceptable()).andReturn();

        Response response = SerializerDeserializer.deserialize(result.getResponse().getContentAsString());


        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getMessage()).isNotNull();
        Assertions.assertThat(response.getMessage()).isEqualTo(ResponseMessage.INBOX_DOES_NOT_EXIST.getMessage());
    }

    //2) this tests when a user invokes accepts invite in an already joined project
    @Test
    @WithMockUser(username = "TestUser")
    public void AcceptProjectInvite_Returns_PROJECT_ALREADY_JOINED() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/invitedProjects/acceptProjectInvite").header("pid", TEST_USER_JOINED_PROJECT_ID).header("inboxId", TEST_USER_INBOX_ID)).andExpect(status().isNotAcceptable()).andReturn();

        Response response = SerializerDeserializer.deserialize(result.getResponse().getContentAsString());

        //verify that the 'deleteInbox' was called
        Mockito.verify(inboxService, Mockito.times(1)).deleteInbox(inbox);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getMessage()).isNotNull();
        Assertions.assertThat(response.getMessage()).isEqualTo(ResponseMessage.PROJECT_ALREADY_JOINED.getMessage());
    }


    //3.1) this test when a user accepts invite in a project, but the pid for the given inboxId is different from the provided 'pid'


    @Test
    @WithMockUser(username = "TestUser")
    public void AcceptProjectInvite_Returns_ERROR_OCCURED() throws Exception {

        MvcResult result = mockMvc.perform(get("/api/invitedProjects/acceptProjectInvite").header("pid", WRONG_PID).header("inboxId", TEST_USER_INBOX_ID)).andExpect(status().isInternalServerError()).andReturn();

        Response response = SerializerDeserializer.deserialize(result.getResponse().getContentAsString());

        Mockito.verify(inboxService, Mockito.times(1)).deleteInbox(inbox);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getMessage()).isNotNull();
        Assertions.assertThat(response.getMessage()).isEqualTo(ResponseMessage.ERROR_OCCURED.getMessage());
    }


    //3.2) this tests when a user accepts invite in a project, but the inbox title is not an INVITATION


    @Test
    @WithMockUser(username = "TestUser")
    public void AcceptProjectInvite_Returns_ERROR_OCCURED_2() throws Exception {
        inbox.setTitle(InboxInviteTitle.MESSAGE);

        MvcResult result = mockMvc.perform(get("/api/invitedProjects/acceptProjectInvite").header("pid", PID).header("inboxId", TEST_USER_INBOX_ID)).andExpect(status().isInternalServerError()).andReturn();

        Response response = SerializerDeserializer.deserialize(result.getResponse().getContentAsString());

        Mockito.verify(inboxService, Mockito.times(1)).deleteInbox(inbox);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getMessage()).isNotNull();
        Assertions.assertThat(response.getMessage()).isEqualTo(ResponseMessage.ERROR_OCCURED.getMessage());
    }


    //4.1) this test when a user accepts invite in a project, but the target project does not exists in the database


    @Test
    @WithMockUser(username = "TestUser")
    public void AcceptProjectInvite_Returns_INBOX_INVITATION_INVALID_1() throws Exception {
        //project wth pid = 'PID' has not been configured

        MvcResult result = mockMvc.perform(get("/api/invitedProjects/acceptProjectInvite").header("pid", PID).header("inboxId", TEST_USER_INBOX_ID)).andExpect(status().isNotAcceptable()).andReturn();

        Response response = SerializerDeserializer.deserialize(result.getResponse().getContentAsString());

        Mockito.verify(inboxService, Mockito.times(1)).deleteInbox(inbox);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getMessage()).isNotNull();
        Assertions.assertThat(response.getMessage()).isEqualTo(ResponseMessage.INBOX_INVITATION_INVALID.getMessage());
    }


    //4.2) this tests when a user accepts invite in a project, but the status of the project is COMPLETED

    @Test
    @WithMockUser(username = "TestUser")
    public void AcceptProjectInvite_Returns_INBOX_INVITATION_INVALID_2() throws Exception {
        //setup test6:
        //setting PID to a project with status = COMPLETED
        Mockito.when(projectService.loadProjectByPid(PID)).thenReturn(project);
        project.setStatus(ProjectStatus.COMPLETED);

        MvcResult result = mockMvc.perform(get("/api/invitedProjects/acceptProjectInvite").header("pid", PID).header("inboxId", TEST_USER_INBOX_ID)).andExpect(status().isNotAcceptable()).andReturn();

        Response response = SerializerDeserializer.deserialize(result.getResponse().getContentAsString());

        Mockito.verify(inboxService, Mockito.times(1)).deleteInbox(inbox);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getMessage()).isNotNull();
        Assertions.assertThat(response.getMessage()).isEqualTo(ResponseMessage.INBOX_INVITATION_INVALID.getMessage());
    }

    //5) this tests when a user accepts invite in a project, outside of the above cases
    @Test
    @WithMockUser(username = "TestUser")
    public void AcceptProjectInvite_Returns_INBOX_INVITATION_ACCEPTED() throws Exception {
        //setup for test 7
        Mockito.when(projectService.loadProjectByPid(PID)).thenReturn(project);
        project.setStatus(ProjectStatus.PLANNED);

        MvcResult result = mockMvc.perform(get("/api/invitedProjects/acceptProjectInvite").header("pid", PID).header("inboxId", TEST_USER_INBOX_ID)).andExpect(status().isOk()).andReturn();

        Response response = SerializerDeserializer.deserialize(result.getResponse().getContentAsString());

        Mockito.verify(appUserService, Mockito.times(1)).update(testUser);
        Mockito.verify(inboxService, Mockito.times(1)).saveInbox(Mockito.any());

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getMessage()).isNotNull();
        Assertions.assertThat(response.getMessage()).isEqualTo(ResponseMessage.INBOX_INVITATION_ACCEPTED.getMessage());
    }


    //1.1 this tests when a user rejects invite in a project and the inbox with the given inboxId does not exists in the database
    @Test
    @WithMockUser(username = "TestUser")
    public void RejectProjectInvite_Returns_INBOX_DOES_NOT_EXIST_1() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/invitedProjects/rejectProjectInvite").header("pid", PROJECT_ID_NOT_JOINED_BY_TEST_USER).header("inboxId", INBOX_ID_NOT_PRESENT_IN_DB)).andExpect(status().isNotAcceptable()).andReturn();

        Response response = SerializerDeserializer.deserialize(result.getResponse().getContentAsString());

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getMessage()).isNotNull();
        Assertions.assertThat(response.getMessage()).isEqualTo(ResponseMessage.INBOX_DOES_NOT_EXIST.getMessage());
    }


    //1.2 this tests when a user rejects invite in a project, the inbox with the given inboxId exists in the database exists, but the inbox is for some other user(not user that accepted invite)


    @Test
    @WithMockUser(username = "TestUser")
    public void RejectProjectInvite_Returns_INBOX_DOES_NOT_EXIST_2() throws Exception {

        MvcResult result = mockMvc.perform(get("/api/invitedProjects/rejectProjectInvite").header("pid", PROJECT_ID_NOT_JOINED_BY_TEST_USER).header("inboxId", INBOX_ID_FOR_ANOTHER_USER)).andExpect(status().isNotAcceptable()).andReturn();

        Response response = SerializerDeserializer.deserialize(result.getResponse().getContentAsString());

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getMessage()).isNotNull();
        Assertions.assertThat(response.getMessage()).isEqualTo(ResponseMessage.INBOX_DOES_NOT_EXIST.getMessage());
    }


    //2) this tests when a user invokes reject invite in an alrady joined project
    @Test
    @WithMockUser(username = "TestUser")
    public void RejectProjectInvite_Returns_PROJECT_ALREADY_JOINED() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/invitedProjects/rejectProjectInvite").header("pid", TEST_USER_JOINED_PROJECT_ID).header("inboxId", TEST_USER_INBOX_ID)).andExpect(status().isNotAcceptable()).andReturn();

        Response response = SerializerDeserializer.deserialize(result.getResponse().getContentAsString());

        Mockito.verify(inboxService, Mockito.times(1)).deleteInbox(inbox);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getMessage()).isNotNull();
        Assertions.assertThat(response.getMessage()).isEqualTo(ResponseMessage.PROJECT_ALREADY_JOINED.getMessage());
    }


    //3) this test when a user rejects invite in a project, but the pid for the given inboxId is different from the provided 'pid'

    @Test
    @WithMockUser(username = "TestUser")
    public void RejectProjectInvite_Returns_ERROR_OCCURED() throws Exception {

        MvcResult result = mockMvc.perform(get("/api/invitedProjects/rejectProjectInvite").header("pid", WRONG_PID).header("inboxId", TEST_USER_INBOX_ID)).andExpect(status().isInternalServerError()).andReturn();

        Response response = SerializerDeserializer.deserialize(result.getResponse().getContentAsString());

        Mockito.verify(inboxService, Mockito.times(1)).deleteInbox(inbox);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getMessage()).isNotNull();
        Assertions.assertThat(response.getMessage()).isEqualTo(ResponseMessage.ERROR_OCCURED.getMessage());
    }

    //3.2) this tests when a user rejects invite in a project, but the inbox title is not an INVITATION


    @Test
    @WithMockUser(username = "TestUser")
    public void RejectProjectInvite_Returns_ERROR_OCCURED_2() throws Exception {
        inbox.setTitle(InboxInviteTitle.MESSAGE);
        MvcResult result = mockMvc.perform(get("/api/invitedProjects/rejectProjectInvite").header("pid", PID).header("inboxId", TEST_USER_INBOX_ID)).andExpect(status().isInternalServerError()).andReturn();

        Response response = SerializerDeserializer.deserialize(result.getResponse().getContentAsString());

        Mockito.verify(inboxService, Mockito.times(1)).deleteInbox(inbox);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getMessage()).isNotNull();
        Assertions.assertThat(response.getMessage()).isEqualTo(ResponseMessage.ERROR_OCCURED.getMessage());
    }


    //4) this tests when a user rejects invite for a project which is not in the database
    @Test
    @WithMockUser(username = "TestUser")
    public void RejectProjectInvite_Returns_INBOX_INVITATION_INVALID() throws Exception {
        Mockito.when(projectService.loadProjectByPid(PID)).thenReturn(project);

        MvcResult result = mockMvc.perform(get("/api/invitedProjects/rejectProjectInvite").header("pid", PID).header("inboxId", TEST_USER_INBOX_ID)).andExpect(status().isOk()).andReturn();


        Response response = SerializerDeserializer.deserialize(result.getResponse().getContentAsString());

        Mockito.verify(inboxService, Mockito.times(1)).deleteInbox(inbox);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getMessage()).isNotNull();
        Assertions.assertThat(response.getMessage()).isEqualTo(ResponseMessage.INBOX_INVITATION_DELETED.getMessage());
    }


    //7) this tests when a user reject invite in a project, outside of the above cases
    @Test
    @WithMockUser(username = "TestUser")
    public void RejectProjectInvite_Returns_INBOX_INVITATION_ACCEPTED() throws Exception {
        Mockito.when(projectService.loadProjectByPid(PID)).thenReturn(project);

        MvcResult result = mockMvc.perform(get("/api/invitedProjects/rejectProjectInvite").header("pid", PID).header("inboxId", TEST_USER_INBOX_ID)).andExpect(status().isOk()).andReturn();

        Response response = SerializerDeserializer.deserialize(result.getResponse().getContentAsString());

        Mockito.verify(inboxService, Mockito.times(1)).saveInbox(Mockito.any());

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getMessage()).isNotNull();
        Assertions.assertThat(response.getMessage()).isEqualTo(ResponseMessage.INBOX_INVITATION_DELETED.getMessage());
    }
}
