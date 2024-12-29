package com.taskhive.backend.validators;

import com.taskhive.backend.response.Response;
import com.taskhive.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class ClientSideValidations {
    @Autowired
    UserService userService;

    @PostMapping(value = "checkUsernameAvailability")
    public ResponseEntity<Response> checkUsernameAvailability(@RequestBody String username) {
        System.out.println(username);
        if (userService.loadUserByUsername(username) != null) {
            return new ResponseEntity<Response>(new Response("false"), HttpStatus.OK);
        } else {
            return new ResponseEntity<Response>(new Response("true"), HttpStatus.OK);
        }
    }
}
