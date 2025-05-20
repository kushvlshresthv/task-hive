package com.taskhive.backend.controller;

import com.taskhive.backend.constants.GlobalConstants;
import com.taskhive.backend.constants.InboxInviteTitle;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = GlobalConstants.FRONTEND_URL, allowCredentials = "true", allowedHeaders = "*", exposedHeaders = "*")
@Slf4j
@RestController
public class InboxController {
    @Autowired
    AppUserService appUserService;

    @Autowired
    ProjectService projectService;

    @Autowired
    InboxService inboxService;


    //TODO: check whether the invite we are trying to create for a particular user already exists in the database

    @GetMapping("/createProjectInvite")
    public ResponseEntity<Response> createProjectInvite(@RequestHeader int pid, @RequestHeader String username) {
        AppUser currentUser = appUserService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        //1) check if the project with the give pid exists
        Project targetProject = projectService.loadProjectByPid(pid);

        if (targetProject == null) {
            return new ResponseEntity<Response>(new Response(ResponseMessage.TARGET_PROJECT_NOT_FOUND), HttpStatus.NOT_FOUND);
        }

        //2) check if the target user exists:
        AppUser targetUser = appUserService.loadUserByUsername(username);

        if (targetUser == null) {
            return new ResponseEntity<Response>(new Response(ResponseMessage.TARGET_USER_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }

        //3) check if the target user and the current user have the same id
        if (targetUser.getUid() == currentUser.getUid()) {
            return new ResponseEntity<Response>(new Response(ResponseMessage.CANNOT_INVITE_OWNER), HttpStatus.NOT_FOUND);
        }


        List<Project> currentUserOwnedProjects = currentUser.getOwnedProjects();
        boolean isOwned = false;

        if (currentUserOwnedProjects == null || currentUserOwnedProjects.isEmpty()) {
            return new ResponseEntity<Response>(new Response(ResponseMessage.PROJECT_NOT_OWNED_BY_CURRENT_USER), HttpStatus.NOT_FOUND);
        }

        //4)  check if the user who sent the request owns the project with the given pid
        for (Project project : currentUserOwnedProjects) {
            if (project.getPid() == targetProject.getPid()) {
                isOwned = true;
                break;
            }
        }

        if (!isOwned) {
            return new ResponseEntity<Response>(new Response(ResponseMessage.PROJECT_NOT_OWNED_BY_CURRENT_USER), HttpStatus.NOT_FOUND);
        }

        //5) check if the project has already been joined by the targetUser
        List<Project> targetUserJoinedProjects = targetUser.getJoinedProjects();

        if (targetUserJoinedProjects != null) {
            for (Project project : targetUserJoinedProjects) {
                if (project.getPid() == pid) {
                    return new ResponseEntity<>(new Response(ResponseMessage.PROJECT_ALREADY_JOINED), HttpStatus.CONFLICT);
                }
            }
        }


        //7 check if the project invite is already present in the targetUser's invite
        List<Inbox> targetUserInboxes = targetUser.getInboxes();

        if (targetUserInboxes != null) {
            for (Inbox inbox : targetUserInboxes) {
                if (inbox.getPid() == pid && inbox.getTitle() == InboxInviteTitle.INVITATION && inbox.getInitiator().equals(currentUser.getUsername())) {
                    return new ResponseEntity<>(new Response(ResponseMessage.PROJECT_INVITE_ALREADY_SENT), HttpStatus.CONFLICT);
                }
            }
        }


        //8) create and save the inbox in the database
        Inbox inbox = new Inbox();
        inbox.setUser(targetUser);
        inbox.setInitiator(currentUser.getUsername());
        inbox.setTitle(InboxInviteTitle.INVITATION);
        inbox.setPid(targetProject.getPid());
        inbox.setProjectName(targetProject.getProjectName());

        Inbox savedInbox = inboxService.saveInbox(inbox);

        if (savedInbox.getInboxId() <= 0) {
            return new ResponseEntity<Response>(new Response(ResponseMessage.PROJECT_INVITATION_CREATION_FAILED), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("Project invitation created for project: " + targetProject.getProjectName() + " from user " + currentUser.getUsername() + "for " + targetUser.getUsername());
        return new ResponseEntity<Response>(new Response(ResponseMessage.PROJECT_INVITATION_SUCCESS), HttpStatus.OK);
    }


    @GetMapping("/getInboxes")
    public ResponseEntity<Response> getInbox(Authentication authentication) {
        AppUser user = appUserService.loadUserByUsername(authentication.getName());

        List<Inbox> inboxes = user.getInboxes();
        Response response = new Response(ResponseMessage.INBOXES_FETCH_SUCCESS);
        response.setMainBody(inboxes);

        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }
}
