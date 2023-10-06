package com.craft.onboarding.driveronboarding.utils;

import com.craft.onboarding.driveronboarding.model.Driver;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtility {

    private final String secret = "jahgdjhgdhjghdg7javdhv";
    private final long expirationMs = 864_000_000; // 10 days in milliseconds


    public String generateJwtToken(Driver driver) {
        Claims claims = Jwts.claims().setSubject(driver.getEmail());
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setClaims(claims)
                //.setSubject(driver.getEmail())\
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String decodeJwtToken(String authorisationHeader) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(authorisationHeader.replace("Bearer ", ""))
                .getBody()
                .getSubject();
    }
}