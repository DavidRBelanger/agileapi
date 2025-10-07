package com.dbelanger.spring.agileapi.service;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import com.dbelanger.spring.agileapi.model.Organization;
import com.dbelanger.spring.agileapi.model.User;
import com.dbelanger.spring.agileapi.repository.OrganizationRepository;

@ActiveProfiles("test")
class OrganizationServiceTest {

    @Mock
    private OrganizationRepository organizationRepository;

    @InjectMocks
    private OrganizationService organizationService;

    private Organization org;
    private User user;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        org = new Organization();
        org.setId(1L);
        org.setSlug("acme");
        user = new User();
        user.setOrganization(org);
    }

    @Test
    void getOrganizationById_found() {
        when(organizationRepository.findById(1L)).thenReturn(Optional.of(org));
        assertThat(organizationService.getOrganizationById(1L)).isSameAs(org);
    }

    @Test
    void getOrganizationById_notFound() {
        when(organizationRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> organizationService.getOrganizationById(1L)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getOrganizationBySlug_found() {
        when(organizationRepository.findBySlug("acme")).thenReturn(Optional.of(org));
        assertThat(organizationService.getOrganizationBySlug("acme")).isSameAs(org);
    }

    @Test
    void getOrganizationBySlug_notFound() {
        when(organizationRepository.findBySlug("acme")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> organizationService.getOrganizationBySlug("acme")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void slugExists_true() {
        when(organizationRepository.existsBySlug("acme")).thenReturn(true);
        assertThat(organizationService.slugExists("acme")).isTrue();
    }

    @Test
    void isUserInOrganization_true() {
        assertThat(organizationService.isUserInOrganization(user, org)).isTrue();
    }

    @Test
    void assertUserInOrganization_denied() {
        Organization other = new Organization();
        other.setId(2L);
        assertThatThrownBy(() -> organizationService.assertUserInOrganization(user, other))
                .isInstanceOf(AccessDeniedException.class);
    }
}
