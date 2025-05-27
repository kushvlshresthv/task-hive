package com.taskhive.backend.controller;

import com.taskhive.backend.constants.GlobalConstants;
import com.taskhive.backend.constants.InboxInviteTitle;
import com.taskhive.backend.entity.AppUser;
import com.taskhive.backend.entity.Inbox;
import com.taskhive.backend.entity.Project;
import com.taskhive.backend.response.Response;
import com.taskhive.backend.response.ResponseMessage;
import com.taskhive.backend.service.AppUserService;
import com.taskhive.backend.service.ProjectService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
/createProject gets the user from the SecurityContextHolder and adds the user to the Project saves the Project in the database

/projects gets the User from the SecurityContextHolder and gets the Projects from that User database
 */

@CrossOrigin(origins = GlobalConstants.FRONTEND_URL, allowCredentials = "true", allowedHeaders = "*", exposedHeaders = "*")

@Slf4j
@RestController
@RequestMapping("/api")
public class ProjectController {
    @Autowired
    ProjectService projectService;

    @Autowired
    AppUserService appUserService;


    @PostMapping("/createProject")
    public ResponseEntity<Response> createProject(@Valid @RequestBody Project project, Errors errors) {
        ResponseEntity<Response> responseEntity;
        AppUser user = appUserService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        project.setUser(user);

        //server side validations:
        if (errors.hasErrors()) {
            log.info("server side validation failed while creating a new project");
            return new ResponseEntity<Response>(new Response("server side validation failed"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Project createdProject = projectService.createProject(project);

        if (createdProject.getPid() < 0) {

            log.info("something went wrong...");
            return new ResponseEntity<Response>(new Response("project could not be saved in the database"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Response>(new Response("project successfully saved"), HttpStatus.OK);
    }

    @GetMapping("/projects")
    public ResponseEntity<Response> getProjects() {
        AppUser user = appUserService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        List<Project> projects = user.getOwnedProjects();

        Response response = new Response();
        response.setMainBody(projects);
        response.setMessage("Projects:");

        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    //only returns the owned Project
    @GetMapping("getOwnedProjectById")
    public ResponseEntity<Response> getOwnedProjectById(@RequestHeader int pid) {
        //check if the user has been owned project with the given pid
        AppUser user = appUserService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        //check through the owned project
        List<Project> projects = user.getOwnedProjects();

        if (projects != null) {
            for (Project project : projects) {
                if (project.getPid() == pid) {
                    Response response = new Response();
                    response.setMainBody(project);
                    response.setMessage(ResponseMessage.PROJECT_FOUND);
                    return new ResponseEntity<Response>(response, HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(new Response(ResponseMessage.PROJECT_NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    //only returns the invited projects
    @GetMapping("/getInvitedProjectById")
    public ResponseEntity<Response> getInvitedProjectById(@RequestHeader String pid, @RequestHeader String inboxId, Authentication authentication) {
        AppUser user = appUserService.loadUserByUsername(authentication.getName());

        //check through the invited projects
        List<Inbox> inboxes = user.getInboxes();
        if (inboxes != null) {
            for (Inbox inbox : inboxes) {
                if (inbox.getPid() == Integer.parseInt(pid) && inbox.getInboxId() == Integer.parseInt(inboxId) && inbox.getTitle().equals(InboxInviteTitle.INVITATION)) {

                    Project project = projectService.loadProjectByPid(inbox.getPid());
                    Response response = new Response(project, ResponseMessage.PROJECT_FOUND);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            }
        }

        return new ResponseEntity<>(new Response(ResponseMessage.PROJECT_NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getInvitedProjects")
    public ResponseEntity<Response> getInvitedProjects() {

        return null;
    }

}