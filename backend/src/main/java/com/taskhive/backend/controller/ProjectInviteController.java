package com.taskhive.backend.controller;

import com.taskhive.backend.constants.GlobalConstants;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = GlobalConstants.FRONTEND_URL, allowCredentials = "true", allowedHeaders = "*", exposedHeaders = "*")
@Slf4j
@RestController
@RequestMapping("/api/invitedProjects")
public class ProjectInviteController {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private InboxService inboxService;

    /**
     * NOTE: when this route is invoked and if the inbox exists in the databaes for the current user, the Inbox is deleted for the user, regardless whether the response is ERROR or OK
     */

    @GetMapping("/acceptProjectInvite")
    public ResponseEntity<Response> acceptProjectInvite(@RequestHeader String pid, @RequestHeader String inboxId, Authentication authentication) {
        AppUser user = appUserService.loadUserByUsername(authentication.getName());

        //1) check if the inbox exists in the database for the current user
        Inbox inbox = inboxService.getInboxById(Integer.parseInt(inboxId));
        if (inbox == null || inbox.getUser() == null || inbox.getUser().getUid() != user.getUid()) {
            return new ResponseEntity<>(new Response(ResponseMessage.INBOX_DOES_NOT_EXIST), HttpStatus.NOT_ACCEPTABLE);
        }


        //2) check if the user has already joined the project
        List<Project> joinedProjects = user.getJoinedProjects();
        for (Project project : joinedProjects) {
            if (Integer.toString(project.getPid()).equals(pid)) {
                inboxService.deleteInbox(inbox);
                return new ResponseEntity<>(new Response(ResponseMessage.PROJECT_ALREADY_JOINED), HttpStatus.NOT_ACCEPTABLE);
            }
        }


        //3) check if the invitation exists and is a valid invitation
        //4) test the whether the targetProject is null and targetProject's status is COMPLETED
        //then, add the project into the user's joinedProject and save user
        //then, delete the inbox from user
        if (inbox.getPid() == Integer.parseInt(pid)
                && inbox.getTitle() == InboxInviteTitle.INVITATION) {

            Project targetProject = projectService.loadProjectByPid(Integer.parseInt(pid));

            /*
             * NOTE: invite is considered valid even if status = FALIED
             * add the project to joinedProjects and since cascade = persist, the project data is automatically saved too
             * */

            if (targetProject != null && targetProject.getStatus() != ProjectStatus.COMPLETED) {
                user.getJoinedProjects().add(targetProject);
                user.getInboxes().remove(inbox);
                appUserService.update(user);

                //create an inbox for the initiator
                AppUser initiator = appUserService.loadUserByUsername(inbox.getInitiator());
                Inbox inboxForInitiator = Inbox.builder().user
                        (initiator).title(InboxInviteTitle.PROJECT_ACCEPTED).initiator(user.getUsername()).projectName(targetProject.getProjectName()).build();
                inboxService.saveInbox(inboxForInitiator);

                log.info("Project invitation accepted for user: " + user.getUsername() + "with pid: " + pid);

                return new ResponseEntity<>(new Response(ResponseMessage.INBOX_INVITATION_ACCEPTED), HttpStatus.OK);
            }
            inboxService.deleteInbox(inbox);
            return new ResponseEntity<>(new Response(ResponseMessage.INBOX_INVITATION_INVALID), HttpStatus.NOT_ACCEPTABLE);
        }
        inboxService.deleteInbox(inbox);
        return new ResponseEntity<>(new Response(ResponseMessage.ERROR_OCCURED), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @GetMapping("/rejectProjectInvite")
    public ResponseEntity<Response> rejectProjectInvite(@RequestHeader String pid, @RequestHeader String inboxId, Authentication authentication) {
        AppUser user = appUserService.loadUserByUsername(authentication.getName());

        //1) check if the inbox exists in the database and the inbox is for the current user
        Inbox inbox = inboxService.getInboxById(Integer.parseInt(inboxId));
        if (inbox == null || inbox.getUser() == null || inbox.getUser().getUid() != user.getUid()) {
            return new ResponseEntity<>(new Response(ResponseMessage.INBOX_DOES_NOT_EXIST), HttpStatus.NOT_ACCEPTABLE);
        }

        //2) check if the user has already joined the project
        List<Project> joinedProjects = user.getJoinedProjects();
        for (Project project : joinedProjects) {
            if (Integer.toString(project.getPid()).equals(pid)) {
                inboxService.deleteInbox(inbox);
                return new ResponseEntity<>(new Response(ResponseMessage.PROJECT_ALREADY_JOINED), HttpStatus.NOT_ACCEPTABLE);
            }
        }


        //3 check if the pid in the inbox is the same as in the header and check if the inbox is an invitation
        if (inbox.getTitle() == InboxInviteTitle.INVITATION && inbox.getPid() == Integer.parseInt(pid)) {

            //create an inbox for the initiator
            Project targetProject = projectService.loadProjectByPid(Integer.parseInt(pid));

            //4 check whether the targetProject exists in the databaase
            if (targetProject == null) {
                inboxService.deleteInbox(inbox);
                return new ResponseEntity<>(new Response(ResponseMessage.INBOX_INVITATION_INVALID), HttpStatus.NOT_ACCEPTABLE);
            }

            //we could have also detached the inbox from the user and then saved and orphanRemoval would have removed the record
            inboxService.deleteInbox(inbox);

            AppUser initiator = appUserService.loadUserByUsername(inbox.getInitiator());
            Inbox inboxForInitiator = Inbox.builder().user
                    (initiator).title(InboxInviteTitle.PROJECT_REJECTED).initiator(user.getUsername()).projectName(targetProject.getProjectName()).build();
            inboxService.saveInbox(inboxForInitiator);


            log.info("Project invitation rejected by user: " + user.getUsername() + "with pid: " + pid);

            return new ResponseEntity<>(new Response(ResponseMessage.INBOX_INVITATION_DELETED), HttpStatus.OK);
        }
        inboxService.deleteInbox(inbox);
        return new ResponseEntity<>(new Response(ResponseMessage.ERROR_OCCURED), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}