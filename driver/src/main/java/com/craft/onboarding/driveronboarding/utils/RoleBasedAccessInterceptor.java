package com.craft.onboarding.driveronboarding.utils;


import com.craft.onboarding.driveronboarding.exception.AdminAccesIsNotGRantedException;
import com.craft.onboarding.driveronboarding.model.Driver;
import com.craft.onboarding.driveronboarding.model.Role;
import com.craft.onboarding.driveronboarding.service.DriverService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Slf4j
@Component
public class RoleBasedAccessInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtTokenUtility jwtTokenUtility;

    @Autowired
    private DriverService driverService;

    @Override
     public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        final String requestTokenHeader = request.getHeader("Authorization");

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            String jwtToken = requestTokenHeader.substring(7); // Remove "Bearer " prefix

            try {
                String email = jwtTokenUtility.decodeJwtToken(jwtToken); // Attempt to decode the token
                log.info("valid JWT");
                //For POC admin and driver is under the same entity, in real scenario both shoudl
                // have different entities implementing somthing like Person interface

                Driver driver = driverService.getDriver(email);
                log.info("driver roles "  +driver.getRoles());
                for(Role x: driver.getRoles() ){
                    log.info("Authority" + x.getAuthority());
                    if(x.getAuthority().compareTo("ROLE_ADMIN") == 0) {
                        log.info("Has Admin access, granting request access");
                        return true;
                    }

                }
                log.info("Does not have Admin access,not granting request access");
                try {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Does not have Admin Access");
                } catch (IOException e) {
                    throw new AdminAccesIsNotGRantedException("Does not have Admin Access");
                }
                log.info("Does not have Admin access, not granting request access");

                return false;// Token is valid, but dont have access to admin requests

            } catch (ExpiredJwtException | MalformedJwtException | SignatureException e) {
                // Token is invalid or expired
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                log.info("Invalid JWT");
                return false;
            }
        } else {
            // Token is not provided in the request header
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing JWT token");
            log.info("Invalid JWT");
            return false;
        }
    }
}