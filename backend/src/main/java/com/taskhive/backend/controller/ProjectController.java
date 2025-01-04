package com.taskhive.backend.controller;

import com.taskhive.backend.constants.GlobalConstants;
import com.taskhive.backend.entity.AppUser;
import com.taskhive.backend.entity.Project;
import com.taskhive.backend.response.Response;
import com.taskhive.backend.service.AppUserService;
import com.taskhive.backend.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = GlobalConstants.FRONTEND_URL, allowCredentials = "true", allowedHeaders = "*", exposedHeaders = "*")

@RestController
public class ProjectController {
    @Autowired
    ProjectService projectService;

    @Autowired
    AppUserService appUserService;

    @PostMapping("/createProject")
    public ResponseEntity<Response> createProject(@RequestBody Project project, Errors errors) {
        ResponseEntity<Response> responseEntity;
        AppUser user = appUserService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        project.setUser(user);
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
}
