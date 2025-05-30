package com.taskhive.backend.controller;

import com.taskhive.backend.constants.GlobalConstants;
import com.taskhive.backend.entity.AppUser;
import com.taskhive.backend.service.AppUserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

//this is just for manual testing, not other use
@Slf4j
@CrossOrigin(origins = GlobalConstants.FRONTEND_URL, allowCredentials = "true", allowedHeaders = "*", exposedHeaders = "*")
@RestController
public class TestController {
    @Autowired
    AppUserService appUserService;

    @GetMapping("/test")
    public String getTest(HttpSession session) throws Exception {
        AppUser appUser = appUserService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        log.info("/test is executed");
        throw new Exception("exception from /test");
    }

    @PostMapping("/test")
    public String getTest2(HttpSession session) {
        AppUser appUser = appUserService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        log.info("/test is executed");
        log.info(String.valueOf(appUser.getJoinedProjects().get(0).getPid()));
        return "Hello from secured web page";
    }
}
