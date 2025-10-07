package com.dbelanger.spring.agileapi.security;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;

import com.dbelanger.spring.agileapi.model.Organization;
import com.dbelanger.spring.agileapi.model.User;
import com.dbelanger.spring.agileapi.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter filter;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @Test
    void setsAuthenticationWhenValidToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer ABC");
        Claims claims = new DefaultClaims();
        claims.setSubject("42");
        when(jwtService.validateTokenAndGetClaims("ABC")).thenReturn(claims);
        Organization org = new Organization(); org.setId(1L);
        User u = new User(); u.setId(42L); u.setOrganization(org);
        when(userRepository.findById(42L)).thenReturn(java.util.Optional.of(u));

        filter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void skipsWhenNoHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);
        filter.doFilterInternal(request, response, filterChain);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }
}
