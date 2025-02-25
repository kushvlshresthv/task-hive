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


    //TODO: check whether the invite we are trying to create for a particular user already exists in the database

    @GetMapping("/createProjectInvite")
    public ResponseEntity<Response> createProjectInvite(@RequestHeader("pid") int pid, @RequestHeader("username") String username) {
        AppUser currentUser = appUserService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        List<Project> projects = currentUser.getOwnedProjects();
        //1) check if the project with the give pid exists

        Project targetProject = projectService.loadProjectByPid(pid);

        if (targetProject == null) {
            return new ResponseEntity<Response>(new Response("project with the provided pid doesn't exist"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //2) check if the target user exists:
        AppUser targetUser = appUserService.loadUserByUsername(username);

        if (targetUser == null) {
            return new ResponseEntity<Response>(new Response("this user doesn't exist"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //3) check if the target user and the current user have the same id
        if (targetUser.getUid() == currentUser.getUid()) {
            return new ResponseEntity<Response>(new Response("can't create an invite for the owner of this project"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        boolean isOwned = false;

        if (projects.isEmpty()) {
            return new ResponseEntity<Response>(new Response("this project isn't owned by you"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //4)  check if the user who sent the request owns the project with the given pid
        for (Project project : projects) {
            if (project.getPid() == targetProject.getPid()) {
                isOwned = true;
                break;
            }
        }

        if (!isOwned) {
            return new ResponseEntity<Response>(new Response("this project isn't owned by you"), HttpStatus.INTERNAL_SERVER_ERROR);
        }


        //5) create and save the inbox in the database
        String message = "@" + currentUser.getUsername() + " invited you to join project titled " + targetProject.getProjectName();

        Inbox inbox = new Inbox();

        inbox.setUser(targetUser);
        inbox.setMessage(message);
        inbox.setTitle(InboxInviteTitle.INVITATION);
        inbox.setPid(targetProject.getPid());

        Inbox savedInbox = inboxService.saveInbox(inbox);

        if (savedInbox.getInbox_id() <= 0) {
            return new ResponseEntity<Response>(new Response("this project invitation could not be created"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Response>(new Response("project invitation successfully created"), HttpStatus.OK);
    }


    @GetMapping("/getInboxes")
    public ResponseEntity<Response> getInbox() {
        AppUser user = appUserService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        List<Inbox> inboxes = user.getInboxes();

        Response response = new Response();
        response.setMainBody(inboxes);

        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }
}




