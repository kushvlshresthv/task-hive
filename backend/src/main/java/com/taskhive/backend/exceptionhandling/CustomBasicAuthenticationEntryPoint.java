package com.taskhive.backend.exceptionhandling;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setHeader("my-custom-header", "Authentication Failed");

        response.setContentType("application/json;charset=UTF-8");
        String jsonResponse = "{\"custom-body-key\":\"custom-body-value\"}";
        response.getWriter().write(jsonResponse);
    }
}
