package com.taskhive.backend.controller;

import com.taskhive.backend.constants.GlobalConstants;
import com.taskhive.backend.entity.AppUser;
import com.taskhive.backend.service.AppUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

//this is just for manual testing, not other use
@CrossOrigin(origins = GlobalConstants.FRONTEND_URL, allowCredentials = "true", allowedHeaders = "*", exposedHeaders = "*")
@RestController
public class TestController {
    @Autowired
    AppUserService appUserService;

    @GetMapping("/test")
    public String getTest(HttpSession session) throws Exception {
        AppUser appUser = appUserService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        System.out.println("/test is executed");
        // System.out.println(appUser.getJoinedProjects().get(0).getPid());
        throw new Exception();
    }

    @PostMapping("/test")
    public String getTest2(HttpSession session) {
        AppUser appUser = appUserService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        System.out.println("/test is executed");
        System.out.println(appUser.getJoinedProjects().get(0).getPid());
        return "Hello from secured web page";
    }


}
