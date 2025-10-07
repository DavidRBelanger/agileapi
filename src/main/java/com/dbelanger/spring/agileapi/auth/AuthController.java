package com.dbelanger.spring.agileapi.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbelanger.spring.agileapi.dto.AuthLoginRequest;
import com.dbelanger.spring.agileapi.dto.AuthRegisterRequest;
import com.dbelanger.spring.agileapi.dto.UserResponseDto;
import com.dbelanger.spring.agileapi.model.User;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) { this.authService = authService; }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody AuthRegisterRequest req) {
        User user = authService.register(req.email, req.password, req.name, req.organizationName, req.organizationSlug);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UserResponseDto(user.getId(), user.getName(), user.getEmail(), user.getOrganization().getId()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthService.AuthResponse> login(@Valid @RequestBody AuthLoginRequest req) {
        return ResponseEntity.ok(authService.login(req.email, req.password));
    }
}
