package com.dbelanger.spring.agileapi.auth;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.dbelanger.spring.agileapi.model.Organization;
import com.dbelanger.spring.agileapi.model.User;
import com.dbelanger.spring.agileapi.repository.OrganizationRepository;
import com.dbelanger.spring.agileapi.repository.UserRepository;
import com.dbelanger.spring.agileapi.security.JwtService;

@ActiveProfiles("test")
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private OrganizationRepository organizationRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerCreatesUserAndReturnsToken() {
        // Mock: slug doesn't exist yet (user is creating new org)
        when(organizationRepository.existsBySlug("acme")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("pwd")).thenReturn("ENC");
        
        // Mock: save organization first
        when(organizationRepository.save(any(Organization.class))).thenAnswer(inv -> {
            Organization o = inv.getArgument(0);
            o.setId(10L);
            return o;
        });
        
        // Mock: save user
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(99L);
            return u;
        });

        User created = authService.register("test@example.com", "pwd", "Tester", "Acme Corp", "acme");
        assertThat(created.getId()).isEqualTo(99L);
        verify(organizationRepository).save(any(Organization.class));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void loginReturnsTokenWhenPasswordMatches() {
        Organization org = new Organization();
        org.setId(10L);
        User user = new User(99L, "test@example.com", "ENC", "Tester", org);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pwd", user.getPasswordHash())).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("tok2");

        AuthService.AuthResponse resp = authService.login("test@example.com", "pwd");
        assertThat(resp.accessToken()).isEqualTo("tok2");
        assertThat(resp.tokenType()).isEqualTo("Bearer");
    }
}
