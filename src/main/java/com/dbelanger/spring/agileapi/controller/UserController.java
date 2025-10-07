package com.dbelanger.spring.agileapi.controller;

import java.nio.file.AccessDeniedException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbelanger.spring.agileapi.dto.UserResponseDto;
import com.dbelanger.spring.agileapi.dto.UserUpdateRequest;
import com.dbelanger.spring.agileapi.model.User;
import com.dbelanger.spring.agileapi.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable long id, Authentication authentication)
            throws AccessDeniedException {
        User requester = (User) authentication.getPrincipal();
        User user = userService.getUserById(id, requester);
        return ResponseEntity.ok(
                new UserResponseDto(user.getId(), user.getName(), user.getEmail(), user.getOrganization().getId()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> patchUser(@PathVariable long id,
                                                  @RequestBody UserUpdateRequest req,
                                                  Authentication authentication) throws AccessDeniedException {
        User requester = (User) authentication.getPrincipal();
        User patch = new User();
        patch.setName(req.name);
        patch.setEmail(req.email);
        User updated = userService.updateUserById(id, patch, requester);
        return ResponseEntity.ok(
                new UserResponseDto(updated.getId(), updated.getName(), updated.getEmail(), updated.getOrganization().getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id, Authentication authentication)
            throws AccessDeniedException {
        User requester = (User) authentication.getPrincipal();
        userService.deleteUserById(id, requester);
        return ResponseEntity.noContent().build();
    }
}
