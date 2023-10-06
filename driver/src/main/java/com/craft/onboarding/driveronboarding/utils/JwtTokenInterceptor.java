package com.craft.onboarding.driveronboarding.utils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class JwtTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtTokenUtility jwtTokenUtility;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        System.out.println("in interceptor" + request.getRequestURI());


        final String requestTokenHeader = request.getHeader("Authorization");

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            String jwtToken = requestTokenHeader.substring(7); // Remove "Bearer " prefix

            try {
                jwtTokenUtility.decodeJwtToken(jwtToken); // Attempt to decode the token
                System.out.println("interceptor = true");
                return true; // Token is valid, allow the request to proceed
            } catch (ExpiredJwtException | MalformedJwtException | SignatureException e) {
                // Token is invalid or expired
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                System.out.println("interceptor = false");
                return false;
            }
        } else {
            // Token is not provided in the request header
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing JWT token");
            System.out.println("interceptor = false");
            return false;
        }
    }
}