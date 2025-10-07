package com.dbelanger.spring.agileapi.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dbelanger.spring.agileapi.model.Organization;
import com.dbelanger.spring.agileapi.model.User;
import com.dbelanger.spring.agileapi.repository.OrganizationRepository;
import com.dbelanger.spring.agileapi.repository.UserRepository;
import com.dbelanger.spring.agileapi.security.JwtService;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       OrganizationRepository organizationRepository,
                       JwtService jwtService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String email, String password, String name, String organizationName, String organizationSlug) {
        // Validate email uniqueness
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered.");
        }

        // Validate organization slug uniqueness
        if (organizationRepository.existsBySlug(organizationSlug)) {
            throw new IllegalArgumentException("Organization slug already taken. Please choose a different slug.");
        }

        // Validate slug format (alphanumeric and hyphens only)
        if (!organizationSlug.matches("^[a-z0-9-]+$")) {
            throw new IllegalArgumentException("Organization slug must contain only lowercase letters, numbers, and hyphens.");
        }

        // Create the organization FIRST
        Organization organization = new Organization();
        organization.setName(organizationName);
        organization.setSlug(organizationSlug);
        organization = organizationRepository.save(organization);

        // Create the user and link to their new organization
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setName(name);
        user.setOrganization(organization);

        return userRepository.save(user);
    }

    public AuthResponse login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password.");
        }

        String token = jwtService.generateToken(user);
        return new AuthResponse(token, "Bearer");
    }

    public record AuthResponse(String accessToken, String tokenType) {}
}
