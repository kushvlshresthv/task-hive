package com.taskhive.backend.controller;

import com.taskhive.backend.response.Response;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true", exposedHeaders = "*", allowedHeaders = "*")
@RestController
public class LoginController {
    @GetMapping("/login")
    public ResponseEntity<Response> tryLogin(HttpSession session) {
        System.out.println("Login Successfully Done");
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        System.out.println(session.getId());
        return new ResponseEntity<Response>(new Response("login successful"), HttpStatus.OK);
    }
}
