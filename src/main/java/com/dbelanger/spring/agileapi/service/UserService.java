package com.dbelanger.spring.agileapi.service;

import com.dbelanger.spring.agileapi.model.User;
import com.dbelanger.spring.agileapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final OrganizationService organizationService;

    public UserService(UserRepository userRepository, OrganizationService organizationService) {
        this.userRepository = userRepository;
        this.organizationService = organizationService;
    }

    public User getUserById(long targetUserId, User requestingUser) throws AccessDeniedException {
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new IllegalArgumentException("User at " + targetUserId + " not found."));

        organizationService.assertUserInOrganization(requestingUser, targetUser.getOrganization());
        return targetUser;
    }

    public User updateUserById(long targetUserId, User updatedFields, User requestingUser) throws AccessDeniedException {
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new IllegalArgumentException("User at " + targetUserId + " not found."));

        organizationService.assertUserInOrganization(requestingUser, targetUser.getOrganization());

        if (requestingUser.getId() != targetUser.getId()) {
            throw new AccessDeniedException("You cannot modify another user's account.");
        }

        if (updatedFields.getEmail() != null) {
            targetUser.setEmail(updatedFields.getEmail());
        }

        if (updatedFields.getName() != null) {
            targetUser.setName(updatedFields.getName());
        }


        return userRepository.save(targetUser);
    }

    public void deleteUserById(long targetUserId, User requestingUser) throws AccessDeniedException {
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new IllegalArgumentException("User at " + targetUserId + " not found."));

        organizationService.assertUserInOrganization(requestingUser, targetUser.getOrganization());

        userRepository.delete(targetUser);
    }
}
