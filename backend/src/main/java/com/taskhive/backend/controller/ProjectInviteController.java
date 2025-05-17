package com.taskhive.backend.controller;

import com.taskhive.backend.constants.GlobalConstants;
import com.taskhive.backend.constants.InboxInviteTitle;
import com.taskhive.backend.constants.ProjectStatus;
import com.taskhive.backend.entity.AppUser;
import com.taskhive.backend.entity.Inbox;
import com.taskhive.backend.entity.Project;
import com.taskhive.backend.response.Response;
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

    @GetMapping("/acceptProjectInvite")
    public ResponseEntity<Response> acceptProjectInvite(@RequestHeader String pid, @RequestHeader String inboxId, Authentication authentication) {
        AppUser user = appUserService.loadUserByUsername(authentication.getName());

        //1) check if the user has already joined the project
        List<Project> joinedProjects = user.getJoinedProjects();
        for (Project project : joinedProjects) {
            if (Integer.toString(project.getPid()).equals(pid)) {
                return new ResponseEntity<>(new Response("Project already joined"), HttpStatus.NOT_ACCEPTABLE);
            }
        }

        //2) check if the inbox exists in the database for the current user
        Inbox inbox = inboxService.getInboxById(Integer.parseInt(inboxId));
        if (inbox == null || inbox.getUser() == null || inbox.getUser().getUid() != user.getUid()) {
            return new ResponseEntity<>(new Response("Inbox does not exist"), HttpStatus.NOT_ACCEPTABLE);
        }

        //2) check if the invitation exists and is a valid invitation
        //3) add the project into the user's joinedProject and save user
        //4) delete the inbox from user
        if (inbox.getPid() == Integer.parseInt(pid)
                && inbox.getTitle() == InboxInviteTitle.INVITATION) {

            Project targetProject = projectService.loadProjectByPid(Integer.parseInt(pid));

            /*
             * NOTE: invite is considered valid even if status = FALIED
             * add the project to joinedProjects and since cascade = persist, the project data is automatically saved too
             * */

            if (targetProject.getStatus() != ProjectStatus.COMPLETED) {
                user.getJoinedProjects().add(targetProject);
                user.getInboxes().remove(inbox);
                appUserService.update(user);

                log.info("Project invitation accepted for user: " + user.getUsername() + "with pid: " + pid);
                return new ResponseEntity<>(new Response("Project invitation accepted"), HttpStatus.OK);
            }
            return new ResponseEntity<>(new Response("Project invitation expired"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new Response("Error Occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @GetMapping("/rejectProjectInvite")
    public ResponseEntity<Response> rejectProjectInvite(Authentication authentication) {
        AppUser user = appUserService.loadUserByUsername(authentication.getName());


       /*
            1) Fetch the current user and verify that the pid is invited project
            2) if it is invited project, and the project hasn't been marked as over, then remove the project from the inbox.
            3) also send an inbox to the project sneder as 'inbox has been rejected'
            4) else, return the project invite has expired
         */

        return null;
    }
}
