package com.taskhive.backend.controller;

import com.taskhive.backend.constants.GlobalConstants;
import com.taskhive.backend.constants.InboxInviteTitle;
import com.taskhive.backend.entity.AppUser;
import com.taskhive.backend.entity.Inbox;
import com.taskhive.backend.entity.Project;
import com.taskhive.backend.response.Response;
import com.taskhive.backend.service.AppUserService;
import com.taskhive.backend.service.InboxService;
import com.taskhive.backend.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = GlobalConstants.FRONTEND_URL, allowCredentials = "true", allowedHeaders = "*", exposedHeaders = "*")

@RestController
public class InboxController {
    @Autowired
    AppUserService appUserService;

    @Autowired
    ProjectService projectService;

    @Autowired
    InboxService inboxService;

    @GetMapping("/createProjectInvite")
    public ResponseEntity<Response> createProjectInvite(@RequestHeader("pid") int pid, @RequestHeader("username") String username) {

        //1) check if the user who sent the request owns the project with the given pid
        AppUser currentUser = appUserService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        List<Project> projects = currentUser.getOwnedProjects();
        Project targetProject = projectService.loadProjectByPid(pid);

        boolean isOwned = false;

        for (Project project : projects) {
            if (project.getPid() == targetProject.getPid()) {
                isOwned = true;
                break;
            }
        }

        if (!isOwned) {
            return new ResponseEntity<Response>(new Response("this project isn't owned by you"), HttpStatus.INTERNAL_SERVER_ERROR);
        }


        //2) check if the target user exists:
        AppUser targetUser = appUserService.loadUserByUsername(username);

        if (targetUser == null) {
            return new ResponseEntity<Response>(new Response("this user doesn't exist"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String message = "@" + currentUser.getUsername() + " invited you to join project titled " + targetProject.getProjectName();

        //3) create and save the inbox in the database
        Inbox inbox = new Inbox();

        inbox.setUser(targetUser);
        inbox.setMessage(message);
        inbox.setTitle(InboxInviteTitle.INVITATION);
        inbox.setPid(targetProject.getPid());

        Inbox savedInbox = inboxService.saveInbox(inbox);

        if (savedInbox.getInbox_id() < 0) {
            return new ResponseEntity<Response>(new Response("this project invitation could not be created"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Response>(new Response("project invitation successfully created"), HttpStatus.OK);
    }
}




