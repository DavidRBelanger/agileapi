package com.dbelanger.spring.agileapi.security;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.dbelanger.spring.agileapi.model.Organization;
import com.dbelanger.spring.agileapi.model.User;

import io.jsonwebtoken.Claims;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    @Test
    void generateAndValidateToken() {
        Organization org = new Organization();
        org.setId(1L);
        User user = new User(42L, "user@example.com", "hash", "Test User", org);
        String token = jwtService.generateToken(user);
        Claims claims = jwtService.validateTokenAndGetClaims(token);
        assertThat(claims.getSubject()).isEqualTo("42");
        assertThat(claims.get("organizationId", Long.class)).isEqualTo(1L);
        assertThat(claims.getExpiration()).isAfter(new Date());
    }
}
