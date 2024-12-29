package com.taskhive.backend.controller;

import com.taskhive.backend.response.Response;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true", allowedHeaders = "*", exposedHeaders = "*")
@RestController
public class GenericController {
    @GetMapping("/isAuthenticated")
    public ResponseEntity<Response> isAuthenticated(HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getName().equals("anonymous")) {
            if (authentication.isAuthenticated()) {
                return new ResponseEntity<Response>(new Response("true"), HttpStatus.ACCEPTED);
            }
        }
        return new ResponseEntity<Response>(new Response("false"), HttpStatus.NOT_ACCEPTABLE);
    }
}
