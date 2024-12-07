package com.taskhive.backend.validators;

import com.taskhive.backend.service.RegisterUserService;
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
    RegisterUserService registerUserService;

    @PostMapping(value = "checkUsernameAvailability")
    public ResponseEntity<String> checkUsernameAvailability(@RequestBody String username) {
        System.out.println(username);
        if (registerUserService.loadUserByUsername(username) != null) {
            return new ResponseEntity<String>("{\"availability\":false}", HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("{\"availability\":true}", HttpStatus.OK);
        }
    }
}
