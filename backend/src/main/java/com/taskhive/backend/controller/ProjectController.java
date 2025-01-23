package com.taskhive.backend.controller;

import com.taskhive.backend.constants.GlobalConstants;
import com.taskhive.backend.entity.AppUser;
import com.taskhive.backend.entity.Project;
import com.taskhive.backend.response.Response;
import com.taskhive.backend.service.AppUserService;
import com.taskhive.backend.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = GlobalConstants.FRONTEND_URL, allowCredentials = "true", allowedHeaders = "*", exposedHeaders = "*")



/*
/createProject gets the user from the SecurityContextHolder and saves the project in the database

/projects gets the user from the SecurityContextHolder and gets the project from the database

 */
@RestController
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
            return new ResponseEntity<Response>(new Response("server side validation failed"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Project createdProject = projectService.createProject(project);

        if (createdProject.getPid() < 0) {
            System.out.println("something went wrong_______________");
            return new ResponseEntity<Response>(new Response("project could not be saved in the database"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Response>(new Response("project successfully saved"), HttpStatus.OK);
    }

    @GetMapping("/projects")
    public ResponseEntity<Response> getProjects() {
        AppUser user = appUserService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        
        List<Project> projects = user.getProjects();

        Response response = new Response();
        response.setMainBody(projects);
        response.setMessage("Projects:");

        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }
}
