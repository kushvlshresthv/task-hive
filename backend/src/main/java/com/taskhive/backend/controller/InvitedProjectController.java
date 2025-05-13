package com.taskhive.backend.controller;

import com.taskhive.backend.constants.GlobalConstants;
import com.taskhive.backend.response.Response;
import com.taskhive.backend.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = GlobalConstants.FRONTEND_URL, allowCredentials = "true", allowedHeaders = "*", exposedHeaders = "*")

@RestController
public class InvitedProjectController {
    @Autowired
    private ProjectService projectService;


    @GetMapping("/acceptProjectInvite")
    public ResponseEntity<Response> acceptProjectInvite(@RequestHeader String pid) {

        /*
            1) Fetch the current user and verify that the pid is invited project
            2) if it is invited project, and the project hasn't been marked as over, then add the project to the joined project of the user
            3) also send an inbox to the project sender as 'invitation has been accepted'
            4) else, return the project invite has expired
         */

        return null;
    }


    @GetMapping("/rejectProjectInvite")
    public ResponseEntity<Response> rejectProjectInvite() {

        /*
            1) Fetch the current user and verify that the pid is invited project
            2) if it is invited project, and the project hasn't been marked as over, then remove the project from the inbox.
            3) also send an inbox to the project sneder as 'inbox has been rejected'
            4) else, return the project invite has expired
         */

        return null;
    }
}
