package com.dbelanger.spring.agileapi.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dbelanger.spring.agileapi.model.Organization;
import com.dbelanger.spring.agileapi.model.User;
import com.dbelanger.spring.agileapi.security.JwtAuthenticationFilter;
import com.dbelanger.spring.agileapi.service.OrganizationService;

@WebMvcTest(controllers = OrganizationController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrganizationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrganizationService organizationService;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private User principal;
    private Organization org;

    @BeforeEach
    void setup() {
        org = new Organization(); org.setId(1L); org.setSlug("acme");
        principal = new User(); principal.setOrganization(org);
    }

    private Authentication auth() {
        return new Authentication() {
            @Override public String getName() { return "u"; }
            @Override public void setAuthenticated(boolean isAuthenticated) {}
            @Override public boolean isAuthenticated() { return true; }
            @Override public Object getPrincipal() { return principal; }
            @Override public Object getDetails() { return null; }
            @Override public Object getCredentials() { return null; }
            @Override public java.util.Collection<org.springframework.security.core.GrantedAuthority> getAuthorities(){return java.util.List.of();}
        };
    }

    @Test
    void getMyOrganization() throws Exception {
        when(organizationService.getOrganizationById(1L)).thenReturn(org);
        mockMvc.perform(get("/api/v1/organizations/me").principal(auth()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
}
