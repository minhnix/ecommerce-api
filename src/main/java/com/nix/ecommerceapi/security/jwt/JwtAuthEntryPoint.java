package com.nix.ecommerceapi.security.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.io.PrintWriter;

public class JwtAuthEntryPoint implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthEntryPoint.class);
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        logger.error("Responding with unauthorized error. Message - {}", (Object[]) authException.getStackTrace());
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter out = response.getWriter();
        out.println("{\n" +
                "\t\"success\": " + false + "," + '\n' +
                "\t\"message\": \"" + authException.getMessage() + '\"' +
                '}');
        out.flush();
    }
}