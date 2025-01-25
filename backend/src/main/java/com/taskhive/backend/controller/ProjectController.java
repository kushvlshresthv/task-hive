package com.taskhive.backend.controller;

import com.taskhive.backend.constants.GlobalConstants;
import com.taskhive.backend.entity.AppUser;
import com.taskhive.backend.entity.Project;
import com.taskhive.backend.response.Response;
import com.taskhive.backend.service.AppUserService;
import com.taskhive.backend.service.ProjectService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;




/*
/createProject gets the user from the SecurityContextHolder and adds the user to the Project saves the Project in the database

/projects gets the User from the SecurityContextHolder and gets the Projects from that User database
 */

@CrossOrigin(origins = GlobalConstants.FRONTEND_URL, allowCredentials = "true", allowedHeaders = "*", exposedHeaders = "*")

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

        List<Project> projects = user.getOwnedProjects();

        Response response = new Response();
        response.setMainBody(projects);
        response.setMessage("Projects:");

        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }


    //takes username and pid from the frontend and adds the user as a member to the given project after checking that this was invoked by a user which owns the project
    @PostMapping("/addUserToProject")
    public ResponseEntity<Response> addUserToProject(@RequestBody ReqBody reqBody) {
        AppUser user = appUserService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        List<Project> projects = user.getOwnedProjects();
        Project targetProject = projectService.loadProjectByPid(reqBody.pid);

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

        AppUser targetUser = appUserService.loadUserByUsername(reqBody.username);

        if (targetUser == null) {
            return new ResponseEntity<Response>(new Response("this user doesn't exist"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        targetProject.getJoinedUsers().add(targetUser);

        Project savedProject = projectService.saveProject(targetProject);
        if (savedProject.getPid() > 0) {
            return new ResponseEntity<Response>(new Response("user added to the project successfully"), HttpStatus.OK);
        }

        return new ResponseEntity<Response>(new Response("something went wrong"), HttpStatus.INTERNAL_SERVER_ERROR);

    }
}

//this class is for request body for /addUserToProject
@NoArgsConstructor
@Data
@AllArgsConstructor
class ReqBody {
    String username;
    int pid;
}