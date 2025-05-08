package com.taskhive.backend.validators;

import com.taskhive.backend.response.Response;
import com.taskhive.backend.service.AppUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
@RestController
public class ClientSideValidations {
    @Autowired
    AppUserService appUserService;

    @PostMapping(value = "checkUsernameAvailability")
    public ResponseEntity<Response> checkUsernameAvailability(@RequestBody String username) {
        log.info("checking if username: " + username + " is available");
        if (appUserService.loadUserByUsername(username) != null) {
            return new ResponseEntity<Response>(new Response("false"), HttpStatus.OK);
        } else {
            return new ResponseEntity<Response>(new Response("true"), HttpStatus.OK);
        }
    }
}
