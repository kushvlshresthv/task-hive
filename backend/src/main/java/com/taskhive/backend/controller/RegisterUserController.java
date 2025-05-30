package com.taskhive.backend.controller;

import com.taskhive.backend.entity.AppUser;
import com.taskhive.backend.response.Response;
import com.taskhive.backend.service.AppUserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
@RestController
public class RegisterUserController {
    @Autowired
    AppUserService appUserService;

    @PostMapping("/register")
    public ResponseEntity<Response> register(@Valid @RequestBody AppUser user, Errors errors) {
        log.info("trying to register user with username: " + user.getUsername());

        if (errors.hasErrors()) {
            log.info("server side validation failed whiel registering the user");
            for (ObjectError error : errors.getAllErrors()) {
                log.info(error.toString());
                return new ResponseEntity<Response>(new Response("server side validation failed"), HttpStatus.UNPROCESSABLE_ENTITY);
            }

        }
       
        AppUser registeredUser = appUserService.saveNewUser(user);

        if (registeredUser.getUid() > 0) {
            log.info("user with the username: " + user.getUsername() + " has been registered successfully");
            return new ResponseEntity<Response>(new Response("user registered successfully"), HttpStatus.CREATED);
        }
        return new ResponseEntity<Response>(new Response("user could not be registered"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
