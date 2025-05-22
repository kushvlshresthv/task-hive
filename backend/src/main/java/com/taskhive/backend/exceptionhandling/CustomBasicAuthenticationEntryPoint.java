package com.taskhive.backend.exceptionhandling;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskhive.backend.response.Response;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;


//TODO: when /login is invoked without username and password, the commence() method is executed.(AuthenticationEntryPoint is probably set up when spring's AUTHENTICATION header is present but authentication is failed, idk)
@Slf4j
public class CustomBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        Response errorResponse = new Response();

        errorResponse.setMessage("Authentication Failed");

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(), errorResponse);

        log.error("Authentication Failed: " + authException.getMessage());
    }
}
