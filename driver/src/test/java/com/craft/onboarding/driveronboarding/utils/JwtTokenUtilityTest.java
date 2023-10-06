package com.craft.onboarding.driveronboarding.utils;

import com.craft.onboarding.driveronboarding.model.Driver;
import io.jsonwebtoken.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class JwtTokenUtilityTest {

    @Mock
    private Driver driver;

    private JwtTokenUtility jwtTokenUtility;

    private final String secret = "jahgdjhgdhjghdg7javdhv";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtTokenUtility = new JwtTokenUtility();
    }

    @Test
    void generateJwtToken_shouldGenerateValidToken() {
        // Mock driver
        when(driver.getEmail()).thenReturn("example@example.com");

        // Generate token
        String token = jwtTokenUtility.generateJwtToken(driver);

        assertNotNull(token);
        assertTrue(token.length() > 0);

        // Validate the generated token
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
            assertNotNull(claims);
            assertEquals("example@example.com", claims.getSubject());
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException e) {
            fail("Failed to parse the generated token: " + e.getMessage());
        }
    }



    @Test
    void decodeJwtToken_shouldThrowExceptionForInvalidToken() {
        String invalidToken = "InvalidTokenHere"; // Replace with an invalid token

        assertThrows(MalformedJwtException.class, () -> jwtTokenUtility.decodeJwtToken(invalidToken));
    }
}
