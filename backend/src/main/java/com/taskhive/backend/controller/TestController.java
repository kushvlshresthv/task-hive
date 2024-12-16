package com.taskhive.backend.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
    @GetMapping("/test")
    @ResponseBody
    public String getTest(HttpSession session) {
        return "Hello from secured web page";
    }
}
