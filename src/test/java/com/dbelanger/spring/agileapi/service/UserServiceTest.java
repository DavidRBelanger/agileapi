package com.dbelanger.spring.agileapi.service;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import com.dbelanger.spring.agileapi.model.Organization;
import com.dbelanger.spring.agileapi.model.User;
import com.dbelanger.spring.agileapi.repository.UserRepository;

@ActiveProfiles("test")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private OrganizationService organizationService;

    @InjectMocks
    private UserService userService;

    private User requester;
    private Organization org;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        org = new Organization();
        org.setId(1L);
        requester = new User();
        requester.setId(5L);
        requester.setOrganization(org);
    }

    @Test
    void getUserById_success() throws Exception {
        User target = new User(); target.setOrganization(org); target.setId(5L);
        when(userRepository.findById(5L)).thenReturn(Optional.of(target));
        User got = userService.getUserById(5L, requester);
        assertThat(got).isSameAs(target);
    }

    @Test
    void getUserById_notFound() {
        when(userRepository.findById(5L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.getUserById(5L, requester)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updateUserById_updatesOwnAccount() throws Exception {
        User target = new User(); target.setOrganization(org); target.setId(5L); target.setName("old");
        when(userRepository.findById(5L)).thenReturn(Optional.of(target));
        when(userRepository.save(target)).thenReturn(target);
        User patch = new User(); patch.setName("new");
        User updated = userService.updateUserById(5L, patch, requester);
        assertThat(updated.getName()).isEqualTo("new");
    }

    @Test
    void updateUserById_otherAccountDenied() {
        User target = new User(); target.setOrganization(org); target.setId(6L);
        when(userRepository.findById(6L)).thenReturn(Optional.of(target));
        User patch = new User(); patch.setName("new");
        assertThatThrownBy(() -> userService.updateUserById(6L, patch, requester)).isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void deleteUserById_deletes() throws Exception {
        User target = new User(); target.setOrganization(org); target.setId(5L);
        when(userRepository.findById(5L)).thenReturn(Optional.of(target));
        userService.deleteUserById(5L, requester);
        verify(userRepository).delete(target);
    }
}
