package com.taskhive.backend.controller;

import com.taskhive.backend.model.Response;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", exposedHeaders = "*", allowCredentials = "true")
@RestController
public class LoginController {
    @GetMapping("/login")
    public ResponseEntity<Response> tryLogin(HttpSession session) {
        //session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.isAuthenticated());
        System.out.println(session.getId());
        System.out.println("Login Successfully Done");
        return new ResponseEntity<Response>(new Response("login successful"), HttpStatus.OK);
    }
}
